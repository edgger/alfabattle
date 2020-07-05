package com.github.edgger.alfabattle.task4.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edgger.alfabattle.task4.dto.LoanDto;
import com.github.edgger.alfabattle.task4.dto.PersonDto;
import com.github.edgger.alfabattle.task4.exception.PersonNotFoundException;
import com.github.edgger.alfabattle.task4.model.Person;
import com.github.edgger.alfabattle.task4.model.PersonRaw;
import com.github.edgger.alfabattle.task4.repository.LoanRepository;
import com.github.edgger.alfabattle.task4.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final LoanRepository loanRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PersonService(PersonRepository personRepository,
                         LoanRepository loanRepository,
                         ObjectMapper objectMapper) {
        this.personRepository = personRepository;
        this.loanRepository = loanRepository;
        this.objectMapper = objectMapper;
    }

    public void loadPersons() throws IOException {
        ClassPathResource personsResource = new ClassPathResource("persons.json");
        try (JsonParser jsonParser = objectMapper.getFactory().createParser(personsResource.getInputStream())) {
            JsonToken personsToken = jsonParser.nextToken();
            if (jsonParser.nextValue() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected content to be an array");
            }
            List<Person> personList = new ArrayList<>();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                PersonRaw personRaw = objectMapper.readValue(jsonParser, PersonRaw.class);
                Person person = new Person(personRaw.getId(),
                        personRaw.getDocId(),
                        personRaw.getFio(),
                        personRaw.getBirthday(),
                        personRaw.getSalary() * 100,
                        personRaw.getGender());
                personList.add(person);
            }
            personRepository.saveAll(personList);
            log.info("Saved {} persons", personList.size());
        }
    }

    public PersonDto getPerson(String docId) {
        Person person = personRepository.findByDocId(docId)
                .orElseThrow(PersonNotFoundException::new);
        return getPersonDto(person);
    }

    public List<PersonDto> getLoansSortByPersonBirthday() {
        Iterable<Person> personIterable = personRepository.findAll(Sort.by(Sort.Direction.DESC, "birthday"));
        return StreamSupport.stream(personIterable.spliterator(), false)
                .map(this::getPersonDtoWithLoans)
                .collect(Collectors.toList());
    }

    public static PersonDto getPersonDto(Person person) {
        return new PersonDto(person.getDocId(),
                person.getFio(),
                person.getBirthday(),
                person.getSalary(), //todo fix "salary": 58295.00000000001
                person.getGender(),
                null);
    }

    private PersonDto getPersonDtoWithLoans(Person person) {
        List<LoanDto> loanDtos = loanRepository.findAllByDocument(person.getDocId()).stream()
                .map(LoanService::getLoanDto)
                .collect(Collectors.toList());

        return new PersonDto(person.getDocId(),
                person.getFio(),
                person.getBirthday(),
                person.getSalary(),
                person.getGender(),
                loanDtos);
    }
}
