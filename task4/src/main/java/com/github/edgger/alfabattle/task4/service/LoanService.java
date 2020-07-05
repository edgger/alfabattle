package com.github.edgger.alfabattle.task4.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edgger.alfabattle.task4.dto.CreditHistoryDto;
import com.github.edgger.alfabattle.task4.dto.LoanDto;
import com.github.edgger.alfabattle.task4.exception.LoanNotFoundException;
import com.github.edgger.alfabattle.task4.exception.PersonNotFoundException;
import com.github.edgger.alfabattle.task4.model.Loan;
import com.github.edgger.alfabattle.task4.model.LoanRaw;
import com.github.edgger.alfabattle.task4.model.Person;
import com.github.edgger.alfabattle.task4.repository.LoanRepository;
import com.github.edgger.alfabattle.task4.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Slf4j
@Service
public class LoanService {

    private final ElasticsearchRestTemplate esTemplate;
    private final LoanRepository loanRepository;
    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public LoanService(ElasticsearchRestTemplate esTemplate,
                       LoanRepository loanRepository,
                       PersonRepository personRepository,
                       ObjectMapper objectMapper) {
        this.esTemplate = esTemplate;
        this.loanRepository = loanRepository;
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
    }

    public void test() {

        NativeSearchQuery getAllQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).build();

        SearchHits<Loan> search = esTemplate.search(getAllQuery, Loan.class);

//        esTemplate.save()
    }

    public void loadLoans() throws IOException {
        ClassPathResource loansResource = new ClassPathResource("loans.json");
        try (JsonParser jsonParser = objectMapper.getFactory().createParser(loansResource.getInputStream())) {
            JsonToken loansToken = jsonParser.nextToken();
            if (jsonParser.nextValue() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected content to be an array");
            }
            List<Loan> loanList = new ArrayList<>();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                LoanRaw loanRaw = objectMapper.readValue(jsonParser, LoanRaw.class);
                Person person = personRepository.findById(loanRaw.getPersonId())
                        .orElseThrow(PersonNotFoundException::new);
                Loan loan = new Loan(loanRaw.getLoan(),
                        loanRaw.getAmount() * 100,
                        person.getDocId(),
                        loanRaw.getStartDate(),
                        loanRaw.getPeriod() * 12);
                loanList.add(loan);
            }
            loanRepository.saveAll(loanList);
            log.info("Saved {} loans", loanList.size());
        }
    }

    public LoanDto getLoan(String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(LoanNotFoundException::new);
        return getLoanDto(loan);
    }

    public static LoanDto getLoanDto(Loan loan) {
        return new LoanDto(loan.getLoan(),
                loan.getAmount(),
                loan.getDocument(),
                loan.getStartDate(),
                loan.getPeriod());
    }

    public CreditHistoryDto getCreditHistory(String docId) {
        List<Loan> loans = loanRepository.findAllByDocument(docId);
        double sumAmount = loans.stream().mapToDouble(Loan::getAmount).sum();
        List<LoanDto> loanDtos = loans.stream()
                .map(LoanService::getLoanDto)
                .collect(Collectors.toList());
        return new CreditHistoryDto(loans.size(), sumAmount, loanDtos);
    }

    public List<LoanDto> getCreditClosed() {
        return StreamSupport.stream(loanRepository.findAll().spliterator(), false)
                .filter(loan -> loan.getStartDate()
                        .plus(loan.getPeriod(), ChronoUnit.MONTHS)
                        .isBefore(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())))
                .map(LoanService::getLoanDto)
                .collect(Collectors.toList());
    }
}
