package com.github.edgger.alfabattle.task5.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "name")
    private String name;

}
