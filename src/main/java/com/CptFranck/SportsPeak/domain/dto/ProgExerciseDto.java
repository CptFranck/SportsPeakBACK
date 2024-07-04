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

    private String note;

    private Boolean visibility;

    private String label;

    private Set<UserDto> users;

    private UserEntity creator;

    private ExerciseDto exercise;

    @EqualsAndHashCode.Exclude
    private Set<TargetExerciseSetDto> targetExerciseSets;
}
