package com.CptFranck.SportsPeak.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class MuscleDto {

    private Long id;

    private String name;

    private String function;

    private Set<ExerciseDto> exercises;
}
