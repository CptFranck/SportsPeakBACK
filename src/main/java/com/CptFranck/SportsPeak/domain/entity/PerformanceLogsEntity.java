package com.CptFranck.SportsPeak.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "performance_logs")
public class PerformanceLogsEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "performance_logs_id_seq")
    @SequenceGenerator(name = "performance_logs_id_seq", sequenceName = "performance_logs_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "repetition_number", nullable = false)
    private Integer repetitionNumber;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "weight_unit", length = 50, nullable = false)
    private String weightUnit;

    @Column(name = "rest_time", nullable = false)
    private Duration restTime;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @ManyToOne
    @Column(name = "target_exercise_set_id", nullable = false)
    private TargetExerciseSetEntity targetExerciseSet;
}
