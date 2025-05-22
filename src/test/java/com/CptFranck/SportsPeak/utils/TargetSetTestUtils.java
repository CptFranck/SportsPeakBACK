package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSetState;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.utils.DateTimeTestUtils.assertDatetimeWithTimestamp;
import static com.CptFranck.SportsPeak.utils.TestInputDuration.assertInputDuration;

public class TargetSetTestUtils {

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
                new HashSet<>()
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
                new HashSet<>()
        );
    }

    public static InputNewTargetSet createTestInputNewTargetSet(Long progExerciseId, Long targetSetUpdateId, boolean wrongLabel) {
        LocalDateTime creationDate = LocalDateTime.now();
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        String label = wrongLabel ? VisibilityLabel.PRIVATE.label : WeightUnit.KILOGRAMME.label;

        return new InputNewTargetSet(
                1,
                5,
                10,
                0f,
                label,
                InputDuration.DurationToInputDuration(effortTime),
                InputDuration.DurationToInputDuration(restTime),
                creationDate,
                progExerciseId,
                targetSetUpdateId
        );
    }

    public static InputTargetSet createTestInputTargetSet(Long id, boolean wrongLabel) {
        Duration effortTime = Duration.ofHours(0).plusMinutes(0).plusSeconds(5);
        Duration restTime = Duration.ofHours(0).plusMinutes(4).plusSeconds(0);
        String label = wrongLabel ? VisibilityLabel.PRIVATE.label : WeightUnit.KILOGRAMME.label;

        return new InputTargetSet(
                id,
                1,
                5,
                10,
                0f,
                label,
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

    public static void assertEqualsTargetSet(TargetSetEntity expected, TargetSetEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getIndex(), actual.getIndex());
        Assertions.assertEquals(expected.getSetNumber(), actual.getSetNumber());
        Assertions.assertEquals(expected.getRepetitionNumber(), actual.getRepetitionNumber());
        Assertions.assertEquals(expected.getWeight(), actual.getWeight());
        Assertions.assertEquals(expected.getWeightUnit(), actual.getWeightUnit());
        Assertions.assertEquals(expected.getPhysicalExertionUnitTime(), actual.getPhysicalExertionUnitTime());
        Assertions.assertEquals(expected.getRestTime(), actual.getRestTime());
        assertDatetimeWithTimestamp(expected.getCreationDate(), actual.getCreationDate());
    }

    public static void assertTargetSetDtoAndEntity(TargetSetEntity targetSetEntity, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(targetSetEntity.getId(), targetSetDto.getId());
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit().label, targetSetDto.getWeightUnit());
        Assertions.assertEquals(targetSetEntity.getPhysicalExertionUnitTime(), targetSetDto.getPhysicalExertionUnitTime().InputDurationToDuration());
        Assertions.assertEquals(targetSetEntity.getRestTime(), targetSetDto.getRestTime().InputDurationToDuration());
        assertDatetimeWithTimestamp(targetSetEntity.getCreationDate(), targetSetDto.getCreationDate());
        Assertions.assertEquals(targetSetEntity.getState().label, targetSetDto.getState());
        if (Objects.nonNull(targetSetEntity.getTargetSetUpdate()) || Objects.nonNull(targetSetDto.getTargetSetUpdate()))
            Assertions.assertEquals(targetSetEntity.getTargetSetUpdate().getId(), targetSetDto.getTargetSetUpdate().getId());
        Assertions.assertEquals(targetSetEntity.getPerformanceLogs().size(), targetSetDto.getPerformanceLogs().size());
    }

    public static void assertTargetSetDtoAndInputNew(InputNewTargetSet inputNewTargetSet, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(inputNewTargetSet.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(inputNewTargetSet.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(inputNewTargetSet.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(inputNewTargetSet.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(inputNewTargetSet.getWeightUnit(), targetSetDto.getWeightUnit());
        assertInputDuration(inputNewTargetSet.getPhysicalExertionUnitTime(), targetSetDto.getPhysicalExertionUnitTime());
        assertInputDuration(inputNewTargetSet.getRestTime(), targetSetDto.getRestTime());
        assertDatetimeWithTimestamp(inputNewTargetSet.getCreationDate(), targetSetDto.getCreationDate());
    }

    public static void assertTargetSetDtoAndInput(InputTargetSet targetSetEntity, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(targetSetEntity.getId(), targetSetDto.getId());
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit(), targetSetDto.getWeightUnit());
        assertInputDuration(targetSetEntity.getPhysicalExertionUnitTime(), targetSetDto.getPhysicalExertionUnitTime());
        assertInputDuration(targetSetEntity.getRestTime(), targetSetDto.getRestTime());
    }
}
