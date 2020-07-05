package com.github.edgger.alfabattle.task5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCountRule {
    private String itemId;
    private int shopId;
    private int triggerQuantity;
    private int bonusQuantity;
}

