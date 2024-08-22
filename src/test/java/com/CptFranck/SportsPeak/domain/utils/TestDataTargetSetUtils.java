package com.CptFranck.SportsPeak.domain.utils;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class TestDataTargetSetUtils {

    public static TargetSetEntity createTestTargetSet(ProgExerciseEntity progExercise) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new TargetSetEntity(
                6L,
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
                null,
                new HashSet<PerformanceLogEntity>()
        );
    }

    public static TargetSetEntity createNewTestTargetSet(ProgExerciseEntity progExercise) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new TargetSetEntity(
                null,
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
                null,
                new HashSet<PerformanceLogEntity>()
        );
    }

    public static List<TargetSetEntity> createNewTestTargetSets(ProgExerciseEntity progExercise) {
        return List.of(createNewTestTargetSet(progExercise), createNewTestTargetSet(progExercise));
    }

    public static TargetSetEntity createTestTargetSetUpdate(ProgExerciseEntity progExercise, TargetSetEntity update) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new TargetSetEntity(
                6L,
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

    public static TargetSetDto createTestTargetSetDto(ProgExerciseDto progExercise) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new TargetSetDto(
                6L,
                1,
                5,
                10,
                0f,
                WeightUnit.KILOGRAMME.label,
                InputDuration.DurationToInputDuration(effortTime),
                InputDuration.DurationToInputDuration(restTime),
                creationDate,
                TargetSetState.USED.label,
                progExercise,
                null,
                new HashSet<PerformanceLogDto>()
        );
    }

    public static TargetSetDto createTestTargetSetUpdateDto(ProgExerciseDto progExercise, TargetSetDto update) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        return new TargetSetDto(
                6L,
                1,
                5,
                10,
                0f,
                WeightUnit.KILOGRAMME.label,
                InputDuration.DurationToInputDuration(effortTime),
                InputDuration.DurationToInputDuration(restTime),
                creationDate,
                TargetSetState.USED.label,
                progExercise,
                update,
                new HashSet<PerformanceLogDto>()
        );
    }
}
