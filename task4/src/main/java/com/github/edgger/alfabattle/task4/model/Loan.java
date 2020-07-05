package com.github.edgger.alfabattle.task4.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "loan")
public class Loan {

    @Id
    private String loan;

    @Field
    private Double amount;

    @Field(name = "doc")
    private String document;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate startDate;

    @Field
    private Integer period;
}