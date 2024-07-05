package com.CptFranck.SportsPeak.domain.dto;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgExerciseDto {
    private Long id;

    private String name;

    private String note;

    private String visibility;

    private String trustLabel;

    private UserEntity creator;

    private ExerciseDto exercise;

    @EqualsAndHashCode.Exclude
    private Set<TargetSetDto> targetExerciseSets;
}
