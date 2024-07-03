package com.CptFranck.SportsPeak.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prog_ex_set")
public class SetPerformanceLogsEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prog_ex_set_id_seq")
    @SequenceGenerator(name = "prog_ex_set_id_seq", sequenceName = "prog_ex_set_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "repetition_number", nullable = false)
    private Integer repetitionNumber;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "exercise_time", nullable = false)
    private Duration exerciseTime;

    @Column(name = "rest_time", nullable = false)
    private Duration restTime;

    @ManyToOne
    private ProgExSetEntity progExSet;
}
