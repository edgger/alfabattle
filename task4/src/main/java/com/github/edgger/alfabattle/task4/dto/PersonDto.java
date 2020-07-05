package com.github.edgger.alfabattle.task4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.edgger.alfabattle.task4.model.PersonGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    @JsonProperty("docid")
    private String docId;

    private String fio;

    private LocalDate birthday;

    private Double salary;

    private PersonGender gender;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LoanDto> loans;
}