package com.github.edgger.alfabattle.task2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentAnalytic {

    private String userId;

    private Double totalSum;

    private Map<String, PaymentCategoryInfo> analyticInfo;

}

