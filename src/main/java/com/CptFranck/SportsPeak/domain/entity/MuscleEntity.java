package com.CptFranck.SportsPeak.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
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

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "function", columnDefinition = "TEXT", nullable = false)
    private String function;

    @ManyToMany(mappedBy = "muscles")
    private Set<ExerciseEntity> exercises;
}
