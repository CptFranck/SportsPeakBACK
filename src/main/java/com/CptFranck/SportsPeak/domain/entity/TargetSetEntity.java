package com.CptFranck.SportsPeak.domain.entity;

import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "target_set")
public class TargetSetEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "target_set_id_seq")
    @SequenceGenerator(name = "target_set_id_seq", sequenceName = "target_set_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "index", nullable = false)
    private Integer index;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "repetition_number", nullable = false)
    private Integer repetitionNumber;

    @Column(name = "weight", nullable = false)
    private Float weight;

    @Column(name = "weight_unit", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;

    @Column(name = "physical_exertion_unit_time", nullable = false, columnDefinition = "interval")
    private Duration physicalExertionUnitTime;

    @Column(name = "rest_time", nullable = false)
    private Duration restTime;

    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "prog_exercise_id", nullable = false)
    private ProgExerciseEntity progExercise;

    @OneToOne
    @JoinColumn(name = "target_set_update_id")
    private TargetSetEntity targetSetUpdate;

    @OneToMany(mappedBy = "targetSet")
    private Set<PerformanceLogEntity> performanceLogs;
}
