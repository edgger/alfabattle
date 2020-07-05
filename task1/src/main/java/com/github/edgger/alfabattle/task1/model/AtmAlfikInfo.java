package com.github.edgger.alfabattle.task1.model;

import com.github.edgger.alfabattle.task1.model.openapi.ATMDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmAlfikInfo {
    private ATMDetails atmDetails;
    private Long alfik;
}
