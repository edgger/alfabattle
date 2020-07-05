package com.github.edgger.alfabattle.task2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentStats {

    private Integer maxAmountCategoryId;

    private Integer minAmountCategoryId;

    private Integer oftenCategoryId;

    private Integer rareCategoryId;

}

