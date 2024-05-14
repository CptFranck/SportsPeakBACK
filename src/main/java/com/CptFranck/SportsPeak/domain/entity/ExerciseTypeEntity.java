package com.CptFranck.SportsPeak.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "exercise_type")
public class ExerciseTypeEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_type_id_seq")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "goal", columnDefinition = "TEXT", nullable = false)
    private String goal;
}
