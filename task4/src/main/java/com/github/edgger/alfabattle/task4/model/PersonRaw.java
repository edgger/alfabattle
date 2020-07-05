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
public class PersonRaw {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("DocId")
    private String docId;

    @JsonProperty("FIO")
    private String fio;

    @JsonFormat(pattern = "M/d/yyyy")
    @JsonProperty("Birthday")
    private LocalDate birthday;

    @JsonProperty("Salary")
    private Double salary;

    @JsonProperty("Gender")
    private PersonGender gender;
}