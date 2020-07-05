package com.github.edgger.alfabattle.task4.controller;

import com.github.edgger.alfabattle.task4.dto.CreditHistoryDto;
import com.github.edgger.alfabattle.task4.dto.LoanDto;
import com.github.edgger.alfabattle.task4.dto.PersonDto;
import com.github.edgger.alfabattle.task4.dto.StatusDto;
import com.github.edgger.alfabattle.task4.exception.LoanNotFoundException;
import com.github.edgger.alfabattle.task4.exception.PersonNotFoundException;
import com.github.edgger.alfabattle.task4.service.LoanService;
import com.github.edgger.alfabattle.task4.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class LoansController {

    private final LoanService loanService;
    private final PersonService personService;

    @Autowired
    public LoansController(LoanService loanService,
                           PersonService personService) {
        this.loanService = loanService;
        this.personService = personService;
    }

    @PostMapping("/loans/loadPersons")
    public ResponseEntity<StatusDto> loadPersons() throws IOException {
        personService.loadPersons();
        return ResponseEntity.ok(new StatusDto("OK"));
    }

    @PostMapping("/loans/loadLoans")
    public ResponseEntity<StatusDto> loadLoans() throws IOException {
        loanService.loadLoans();
        return ResponseEntity.ok(new StatusDto("OK"));
    }

    @GetMapping("/loans/getPerson/{docId}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable("docId") String docId) {
        PersonDto personDto = personService.getPerson(docId);
        return ResponseEntity.ok(personDto);
    }

    @GetMapping("/loans/getLoan/{loanId}")
    public ResponseEntity<LoanDto> getLoan(@PathVariable("loanId") String loanId) {
        LoanDto loanDto = loanService.getLoan(loanId);
        return ResponseEntity.ok(loanDto);
    }

    @GetMapping("/loans/creditHistory/{docId}")
    public ResponseEntity<CreditHistoryDto> getCreditHistory(@PathVariable("docId") String docId) {
        CreditHistoryDto historyDto = loanService.getCreditHistory(docId);
        return ResponseEntity.ok(historyDto);
    }

    @GetMapping("/loans/creditClosed")
    public ResponseEntity<List<LoanDto>> getCreditClosed() {
        List<LoanDto> loanDtos = loanService.getCreditClosed();
        return ResponseEntity.ok(loanDtos);
    }

    @GetMapping("/loans/loansSortByPersonBirthday")
    public ResponseEntity<List<PersonDto>> getLoansSortByPersonBirthday() {
        List<PersonDto> personDtos = personService.getLoansSortByPersonBirthday();
        return ResponseEntity.ok(personDtos);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StatusDto handlePersonNotFound(PersonNotFoundException e) {
        return new StatusDto("person not found");
    }

    @ExceptionHandler(LoanNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StatusDto handleLoanNotFound(LoanNotFoundException e) {
        return new StatusDto("loan not found");
    }

}
