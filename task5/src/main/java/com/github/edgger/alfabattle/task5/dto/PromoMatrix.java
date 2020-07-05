package com.github.edgger.alfabattle.task5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoMatrix {
    private List<LoyaltyCardRule> loyaltyCardRules = new ArrayList<>();
    private List<ItemCountRule> itemCountRules = new ArrayList<>();
    private List<ItemGroupRule> itemGroupRules = new ArrayList<>();
}

