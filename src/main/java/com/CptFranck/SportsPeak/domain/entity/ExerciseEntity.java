package com.CptFranck.SportsPeak.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "exercise")
public class ExerciseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_id_seq")
    private Long id;

    @ManyToMany
    @JoinTable(name = "classified_exercise",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_type_id"))
    private Set<ExerciseTypeEntity> exerciseTypeEntities;

    @ManyToMany
    @JoinTable(name = "solicited_muscle",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "muscle_id"))
    private Set<MuscleEntity> muscleEntities;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "goal", columnDefinition = "TEXT", nullable = false)
    private String goal;
}
