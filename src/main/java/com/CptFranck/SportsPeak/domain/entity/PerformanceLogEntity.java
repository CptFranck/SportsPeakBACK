package com.CptFranck.SportsPeak.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "performance_log")
public class PerformanceLogEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "performance_log_id_seq")
    @SequenceGenerator(name = "performance_log_id_seq", sequenceName = "performance_log_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "set_index", nullable = false)
    private Integer setIndex;

    @Column(name = "repetition_number", nullable = false)
    private Integer repetitionNumber;

    @Column(name = "weight", nullable = false)
    private Float weight;

    @Column(name = "weight_unit", length = 50, nullable = false)
    private String weightUnit;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @ManyToOne
    @JoinColumn(name = "target_set_id", nullable = false)
    private TargetSetEntity targetSet;
}
