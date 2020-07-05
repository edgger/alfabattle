package com.github.edgger.alfabattle.task3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDtoWithPredicting {

    private Long id;
    private String address;
    private Double lat;
    private Double lon;
    private String title;
    private Integer dayOfWeek;
    private Integer hourOfDay;
    private Long predicting;

}

