package com.CptFranck.SportsPeak.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "exercise_type")
public class ExerciseTypeEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_type_id_seq")
    private Long id;

    @ManyToMany(mappedBy = "exerciseTypeEntities")
    private Set<ExerciseEntity> exerciseEntities;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "goal", columnDefinition = "TEXT", nullable = false)
    private String goal;
}
