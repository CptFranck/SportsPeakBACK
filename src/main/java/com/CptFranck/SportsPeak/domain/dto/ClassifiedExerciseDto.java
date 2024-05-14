package com.CptFranck.SportsPeak.domain.dto;

import com.CptFranck.SportsPeak.domain.entity.compositeKey.ClassifiedExerciseKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ClassifiedExerciseDto {

    private ClassifiedExerciseKey id;

    private ExerciseDto exercise;

    private ExerciseTypeDto exerciseType;
}
