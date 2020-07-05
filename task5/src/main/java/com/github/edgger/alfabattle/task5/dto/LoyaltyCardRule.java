package com.github.edgger.alfabattle.task5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyCardRule {
    private double discount;
    private int shopId;
}

