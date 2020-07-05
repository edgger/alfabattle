package com.github.edgger.alfabattle.task5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalPriceReceipt {
    private BigDecimal total;
    private BigDecimal discount;
    private List<FinalPricePosition> positions;
}

