package com.CptFranck.SportsPeak.domain.dto;

import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {

    private Long id;

    private String name;

    private String description;

    private String goal;

    @EqualsAndHashCode.Exclude
    private Set<MuscleDto> muscles;

    @EqualsAndHashCode.Exclude
    private Set<ExerciseTypeDto> exerciseTypes;

    @EqualsAndHashCode.Exclude
    private Set<ProgExerciseDto> progExercises;
}
