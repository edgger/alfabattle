package com.github.edgger.alfabattle.task4.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRaw {

    @JsonProperty("Loan")
    private String loan;

    @JsonProperty("PersonId")
    private String personId;

    @JsonProperty("Amount")
    private Double amount;

    @JsonFormat(pattern = "M/d/yyyy")
    @JsonProperty("StartDate")
    private LocalDate startDate;

    @JsonProperty("Period")
    private Integer period;
}