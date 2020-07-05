package com.github.edgger.alfabattle.task2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCategoryInfo {

    private Double max;

    private Double min;

    private Double sum;
}

