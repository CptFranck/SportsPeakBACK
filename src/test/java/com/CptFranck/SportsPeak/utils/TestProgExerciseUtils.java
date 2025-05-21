package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputNewProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExerciseTrustLabel;
import org.junit.jupiter.api.Assertions;

import java.util.HashSet;
import java.util.List;

public class TestProgExerciseUtils {

    public static ProgExerciseEntity createTestProgExercise(Long id, UserEntity creator, ExerciseEntity exercise) {
        return new ProgExerciseEntity(
                id,
                "Prog Exercise name",
                "Prog Exercise note",
                VisibilityLabel.PRIVATE,
                TrustLabel.UNVERIFIED,
                new HashSet<>(),
                creator,
                exercise,
                new HashSet<>()
        );
    }

    public static List<ProgExerciseEntity> createNewTestProgExerciseList(UserEntity creator, ExerciseEntity exercise) {
        return List.of(
                createTestProgExercise(null, creator, exercise),
                createTestProgExercise(null, creator, exercise)
        );
    }

    public static ProgExerciseDto createTestProgExerciseDto(Long id, UserDto creator, ExerciseDto exercise) {
        return new ProgExerciseDto(
                id,
                "Prog Exercise name",
                "Prog Exercise note",
                VisibilityLabel.PRIVATE.label,
                TrustLabel.UNVERIFIED.label,
                creator,
                exercise,
                new HashSet<>()
        );
    }

    public static InputNewProgExercise createTestInputNewProgExercise(Long creatorId, Long exerciseId, boolean wrongLabel) {
        String label = wrongLabel ? TrustLabel.TRUSTED.label : VisibilityLabel.PRIVATE.label;
        return new InputNewProgExercise(
                "Prog Exercise name",
                "Prog Exercise note",
                label,
                creatorId,
                exerciseId
        );
    }

    public static InputProgExercise createTestInputProgExercise(Long id, Long exerciseId, boolean wrongLabel) {
        String label = wrongLabel ? TrustLabel.TRUSTED.label : VisibilityLabel.PRIVATE.label;
        return new InputProgExercise(
                id,
                "Prog Exercise name",
                "Prog Exercise note",
                label,
                exerciseId
        );
    }

    public static InputProgExerciseTrustLabel createTestInputProgExerciseTrustLabel(Long id, boolean wrongLabel) {
        String label = wrongLabel ? VisibilityLabel.PRIVATE.label : TrustLabel.TRUSTED.label;
        return new InputProgExerciseTrustLabel(
                id,
                label
        );
    }

    public static void assertEqualProgExercise(ProgExerciseEntity expected, ProgExerciseEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getVisibility(), actual.getVisibility());
        Assertions.assertEquals(expected.getTrustLabel(), actual.getTrustLabel());
    }

    public static void assertProgExerciseDtoAndEntity(ProgExerciseEntity progExercise, ProgExerciseDto progExerciseDto) {
        Assertions.assertNotNull(progExerciseDto);
        Assertions.assertEquals(progExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(progExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(progExercise.getVisibility().label, progExerciseDto.getVisibility());
        Assertions.assertEquals(progExercise.getTrustLabel().label, progExerciseDto.getTrustLabel());
        Assertions.assertEquals(progExercise.getCreator().getId(), progExerciseDto.getCreator().getId());
        Assertions.assertEquals(progExercise.getExercise().getId(), progExerciseDto.getExercise().getId());
        Assertions.assertEquals(progExercise.getTargetSets().size(), progExerciseDto.getTargetSets().size());
    }

    public static void assertProgExerciseDtoAndInputNew(InputNewProgExercise inputNewProgExercise, ProgExerciseDto progExerciseDto) {
        Assertions.assertNotNull(progExerciseDto);
        Assertions.assertEquals(inputNewProgExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(inputNewProgExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(inputNewProgExercise.getVisibility(), progExerciseDto.getVisibility());
        Assertions.assertEquals(inputNewProgExercise.getCreatorId(), progExerciseDto.getCreator().getId());
        Assertions.assertEquals(inputNewProgExercise.getExerciseId(), progExerciseDto.getExercise().getId());
    }

    public static void assertProgExerciseDtoAndInput(InputProgExercise inputProgExercise, ProgExerciseDto progExerciseDto) {
        Assertions.assertNotNull(progExerciseDto);
        Assertions.assertEquals(inputProgExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(inputProgExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(inputProgExercise.getVisibility(), progExerciseDto.getVisibility());
        Assertions.assertEquals(inputProgExercise.getExerciseId(), progExerciseDto.getExercise().getId());
    }
}
