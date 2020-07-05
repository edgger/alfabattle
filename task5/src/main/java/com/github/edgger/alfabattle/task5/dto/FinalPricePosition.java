package com.github.edgger.alfabattle.task5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalPricePosition {
    private String id;
    private String name;
    private BigDecimal price;
    private BigDecimal regularPrice;
}

