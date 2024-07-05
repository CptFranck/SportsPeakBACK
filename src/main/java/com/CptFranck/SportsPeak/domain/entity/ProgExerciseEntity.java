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
@Table(name = "prog_exercise")
public class ProgExerciseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prog_exercise_id_seq")
    @SequenceGenerator(name = "prog_exercise_id_seq", sequenceName = "prog_exercise_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "note", columnDefinition = "TEXT", nullable = false)
    private String note;

    @Column(name = "visibility", nullable = false)
    private String visibility;

    @Column(name = "trust_label", nullable = false)
    private String trustLabel;

    @ManyToMany(mappedBy = "progExercises")
    private Set<UserEntity> users;

    @ManyToOne
    @Column(name = "creator_id", nullable = false)
    private UserEntity creator;

    @ManyToOne
    @Column(name = "exercise_id", nullable = false)
    private ExerciseEntity exercise;

    @OneToMany(mappedBy = "progExercise")
    private Set<TargetSetEntity> targetSets;
}
