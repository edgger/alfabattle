package com.github.edgger.alfabattle.task4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditHistoryDto {
    private Integer countLoan;
    private Double sumAmountLoans;
    private List<LoanDto> loans;
}