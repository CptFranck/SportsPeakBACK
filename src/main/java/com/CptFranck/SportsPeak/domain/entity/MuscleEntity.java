package com.CptFranck.SportsPeak.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "muscle")
public class MuscleEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "muscle_id_seq")
    @SequenceGenerator(name = "muscle_id_seq", sequenceName = "muscle_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    private String function;

    @Column(name = "function", columnDefinition = "TEXT", nullable = false)
    @ManyToMany(mappedBy = "muscles")
    private Set<ExerciseEntity> exercises;
}
