package com.CptFranck.SportsPeak.domain.dto;

import com.CptFranck.SportsPeak.domain.entity.compositeKey.SolicitedMuscleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SolicitedMuscleDto {

    private SolicitedMuscleKey id;
    ;

    private ExerciseDto exercise;

    private MuscleDto muscle;
}
