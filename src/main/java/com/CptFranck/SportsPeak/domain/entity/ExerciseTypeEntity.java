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
@Table(name = "exercise_type")
public class ExerciseTypeEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_type_id_seq")
    @SequenceGenerator(name = "exercise_type_id_seq", sequenceName = "exercise_type_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "goal", columnDefinition = "TEXT", nullable = false)
    private String goal;

    @ManyToMany(mappedBy = "exerciseTypes")
    private Set<ExerciseEntity> exercises;
}
