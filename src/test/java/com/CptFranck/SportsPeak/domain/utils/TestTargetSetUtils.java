package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSetState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class TestTargetSetUtils {

    public static TargetSetEntity createTestTargetSet(Long id, ProgExerciseEntity progExercise, TargetSetEntity update) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new TargetSetEntity(
                id,
                1,
                5,
                10,
                0f,
                WeightUnit.KILOGRAMME,
                effortTime,
                restTime,
                creationDate,
                TargetSetState.USED,
                progExercise,
                update,
                new HashSet<PerformanceLogEntity>()
        );
    }

    public static List<TargetSetEntity> createTestTargetSetList(boolean withId, ProgExerciseEntity progExercise) {
        Long IdOne = null;
        Long IdTwo = null;
        if (withId) {
            IdOne = 1L;
            IdTwo = 1L;
        }
        return List.of(
                createTestTargetSet(IdOne, progExercise, null),
                createTestTargetSet(IdTwo, progExercise, null)
        );
    }

    public static TargetSetDto createTestTargetSetDto(Long id, TargetSetDto update) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new TargetSetDto(
                id,
                1,
                5,
                10,
                0f,
                WeightUnit.KILOGRAMME.label,
                InputDuration.DurationToInputDuration(effortTime),
                InputDuration.DurationToInputDuration(restTime),
                creationDate,
                TargetSetState.USED.label,
                update,
                new HashSet<PerformanceLogDto>()
        );
    }

    public static InputNewTargetSet createTestInputNewTargetSet(Long progExerciseId, Long targetSetUpdateId) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new InputNewTargetSet(
                1,
                5,
                10,
                0f,
                WeightUnit.KILOGRAMME.label,
                InputDuration.DurationToInputDuration(effortTime),
                InputDuration.DurationToInputDuration(restTime),
                creationDate,
                progExerciseId,
                targetSetUpdateId
        );
    }

    public static InputTargetSet createTestInputTargetSet(Long id) {
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new InputTargetSet(
                id,
                1,
                5,
                10,
                0f,
                WeightUnit.KILOGRAMME.label,
                InputDuration.DurationToInputDuration(effortTime),
                InputDuration.DurationToInputDuration(restTime)
        );
    }

    public static InputTargetSetState createTestInputInputTargetSetState(Long id, boolean wrongLabel) {
        String label = wrongLabel ? VisibilityLabel.PRIVATE.label : TargetSetState.USED.label;
        return new InputTargetSetState(
                id,
                label
        );
    }
}
