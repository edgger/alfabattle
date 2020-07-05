package com.github.edgger.alfabattle.task3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "queue_log")
public class QueueLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "start_time_of_wait")
    private LocalTime startWait;

    @Column(name = "end_time_of_wait")
    private LocalTime endWait;

    @Column(name = "end_time_of_service")
    private LocalTime endService;

    @JoinColumn(name = "branches_id")
    @ManyToOne
    private Branch branch;
}
