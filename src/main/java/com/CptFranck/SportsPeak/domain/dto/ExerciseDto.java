package com.CptFranck.SportsPeak.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class ExerciseDto {

    private Long id;

    private String name;

    private String description;

    private String goal;

    private Set<MuscleDto> muscles;

    private Set<ExerciseTypeDto> exerciseTypes;
}
