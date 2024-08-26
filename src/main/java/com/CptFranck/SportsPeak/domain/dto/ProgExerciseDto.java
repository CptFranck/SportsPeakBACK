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
public class ProgExerciseDto {
    private Long id;

    private String name;

    private String note;

    private String visibility;

    private String trustLabel;

    private UserDto creator;

    private ExerciseDto exercise;

    private Set<TargetSetDto> targetSets;
}
