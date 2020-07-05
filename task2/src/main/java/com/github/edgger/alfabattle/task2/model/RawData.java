package com.github.edgger.alfabattle.task2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawData {

    private String ref;
    private Integer categoryId;
    private String userId;
    private String recipientId;
    private String desc;
    private Double amount;

}