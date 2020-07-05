package com.github.edgger.alfabattle.task3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BranchDto {

    private Long id;
    private String address;
    private Double lat;
    private Double lon;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long distance;

    public BranchDto(Long id, String address, Double lat, Double lon, String title) {
        this.id = id;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.title = title;
    }

    public BranchDto(Long id, String address, Double lat, Double lon, String title, Long distance) {
        this.id = id;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.title = title;
        this.distance = distance;
    }
}

