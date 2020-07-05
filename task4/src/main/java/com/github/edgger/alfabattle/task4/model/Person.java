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
@Document(indexName = "person")
public class Person {

    @Id
    private String id;

    @Field
    private String docId;

    @Field
    private String fio;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate birthday;

    @Field
    private Double salary;

    @Field
    private PersonGender gender;
}