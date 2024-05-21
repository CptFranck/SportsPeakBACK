package com.CptFranck.SportsPeak.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuscleDto {

    private Long id;

    private String name;

    private String description;

    private String function;

    private Set<ExerciseDto> exercises;
}
