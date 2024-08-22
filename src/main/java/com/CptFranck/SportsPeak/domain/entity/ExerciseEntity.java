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
@Table(name = "exercise")
public class ExerciseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_id_seq")
    @SequenceGenerator(name = "exercise_id_seq", sequenceName = "exercise_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "goal", columnDefinition = "TEXT", nullable = false)
    private String goal;

    @ManyToMany
    @JoinTable(name = "solicited_muscles",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "muscle_id"))
    private Set<MuscleEntity> muscles;

    @ManyToMany
    @JoinTable(name = "classified_exercises",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_type_id"))
    private Set<ExerciseTypeEntity> exerciseTypes;

    @OneToMany(mappedBy = "exercise")
    private Set<ProgExerciseEntity> progExercises;
}
