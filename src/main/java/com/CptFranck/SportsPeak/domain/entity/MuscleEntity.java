package com.CptFranck.SportsPeak.domain.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "muscle")
public class MuscleEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "muscle_id_seq")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    private String function;

    @Column(name = "function", columnDefinition = "TEXT", nullable = false)
    @ManyToMany(mappedBy = "muscles")
    private Set<ExerciseEntity> exercises;
}
