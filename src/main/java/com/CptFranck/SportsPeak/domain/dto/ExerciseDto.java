package com.CptFranck.SportsPeak.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {

    private Long id;

    private String name;

    private String description;

    private String goal;

    private Set<MuscleDto> muscles;

    private Set<ExerciseTypeDto> exerciseTypes;

    private Set<ProgExerciseDto> progExercises;
}
