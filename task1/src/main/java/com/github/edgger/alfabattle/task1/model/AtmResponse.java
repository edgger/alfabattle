package com.github.edgger.alfabattle.task1.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AtmResponse {
    private Integer deviceId;
    private Double latitude;
    private Double longitude;
    private String location;
    private String city;
    private Boolean payments;
}
