package com.github.edgger.alfabattle.task2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTemplate {

    private Double amount;

    private Integer categoryId;

    private String recipientId;

}

