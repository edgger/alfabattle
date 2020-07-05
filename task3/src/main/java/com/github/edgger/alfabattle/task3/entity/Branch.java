package com.github.edgger.alfabattle.task3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String title;

    @Column
    private String address;

    @Column
    private Double lon;

    @Column
    private Double lat;
    
    @OneToMany(mappedBy = "branch")
    private Set<QueueLog> queueLog;
}
