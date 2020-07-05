package com.github.edgger.alfabattle.task5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {
    private int shopId;
    private boolean loyaltyCard;
    private List<ItemPosition> positions = new ArrayList<>();
}

