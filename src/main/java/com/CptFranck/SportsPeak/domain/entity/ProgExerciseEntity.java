package com.CptFranck.SportsPeak.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "note", columnDefinition = "TEXT", nullable = false)
    private String note;

    @Column(name = "visibility", nullable = false)
    private Boolean visibility;

    @ManyToOne
    private ExerciseEntity exercise;

    @ManyToOne
    private UserEntity creator;
}
