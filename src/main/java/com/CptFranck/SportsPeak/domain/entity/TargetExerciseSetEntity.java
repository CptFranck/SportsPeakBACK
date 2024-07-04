package com.CptFranck.SportsPeak.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "target_exercise_set")
public class TargetExerciseSetEntity {
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

    @Column(name = "weight_unit", length = 50, nullable = false)
    private String weightUnit;

    @Column(name = "physical_exertion_unit_time", nullable = false)
    private Duration physicalExertionUnitTime;

    @Column(name = "rest_time", nullable = false)
    private Duration restTime;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @ManyToOne
    @Column(name = "prog_exercise_id", nullable = false)
    private ProgExerciseEntity progExercise;

    @OneToOne
    @Column(name = "target_exercise_set_update_id")
    private TargetExerciseSetEntity exerciseSet;

    @OneToMany(mappedBy = "targetExerciseSet")
    private Set<PerformanceLogsEntity> setPerformanceLogs;
}
