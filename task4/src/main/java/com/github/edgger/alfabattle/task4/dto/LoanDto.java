package com.github.edgger.alfabattle.task4.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDto {

    private String loan;

    private Double amount;

    private String document;

    @JsonProperty("startdate")
    private LocalDate startDate;

    private Integer period;
}