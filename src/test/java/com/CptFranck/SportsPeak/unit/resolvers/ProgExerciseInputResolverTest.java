package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputNewProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExerciseTrustLabel;
import com.CptFranck.SportsPeak.resolver.ProgExerciseInputResolver;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.*;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProgExerciseInputResolverTest {

    @InjectMocks
    private ProgExerciseInputResolver progExerciseInputResolver;

    @Mock
    private ProgExerciseService progExerciseService;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private UserService userService;

    private UserEntity user;
    private ExerciseEntity exercise;
    private ProgExerciseEntity progExercise;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
        exercise = createTestExercise(1L);
        progExercise = createTestProgExercise(1L, user, exercise);
    }

    @Test
    void resolveInput_InvalidNewInputVisibilityLabel_ThrowLabelMatchNotFoundException() {
        InputNewProgExercise inputNewProgExercise = createTestInputNewProgExercise(user.getId(), exercise.getId(), true);
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(exercise);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> progExerciseInputResolver.resolveInput(inputNewProgExercise));
    }

    @Test
    void resolveInput_ValidNewInput_ReturnsProgExercise() {
        InputNewProgExercise inputNewProgExercise = createTestInputNewProgExercise(user.getId(), exercise.getId(), false);
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(exercise);

        ProgExerciseEntity progExerciseResolved = progExerciseInputResolver.resolveInput(inputNewProgExercise);

        Assertions.assertEquals(inputNewProgExercise.getName(), progExerciseResolved.getName());
        Assertions.assertEquals(inputNewProgExercise.getNote(), progExerciseResolved.getNote());
        Assertions.assertEquals(inputNewProgExercise.getVisibility(), progExerciseResolved.getVisibility().label);
        Assertions.assertEquals(inputNewProgExercise.getCreatorId(), progExerciseResolved.getCreator().getId());
        Assertions.assertEquals(inputNewProgExercise.getExerciseId(), progExerciseResolved.getExercise().getId());
    }

    @Test
    void resolveInput_InvalidInputVisibilityLabel_ThrowLabelMatchNotFoundException() {
        InputProgExercise inputNewProgExercise = createTestInputProgExercise(1L, exercise.getId(), true);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(progExercise);
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(exercise);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> progExerciseInputResolver.resolveInput(inputNewProgExercise));
    }

    @Test
    void resolveInput_ValidInput_ReturnsProgExercise() {
        InputProgExercise inputNewProgExercise = createTestInputProgExercise(1L, exercise.getId(), false);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(progExercise);
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(exercise);

        ProgExerciseEntity progExerciseResolved = progExerciseInputResolver.resolveInput(inputNewProgExercise);

        Assertions.assertEquals(inputNewProgExercise.getId(), progExerciseResolved.getId());
        Assertions.assertEquals(inputNewProgExercise.getName(), progExerciseResolved.getName());
        Assertions.assertEquals(inputNewProgExercise.getNote(), progExerciseResolved.getNote());
        Assertions.assertEquals(inputNewProgExercise.getVisibility(), progExerciseResolved.getVisibility().label);
        Assertions.assertEquals(inputNewProgExercise.getExerciseId(), progExerciseResolved.getExercise().getId());
    }

    @Test
    void resolveInput_InvalidInputTrustLabel_ThrowLabelMatchNotFoundException() {
        InputProgExerciseTrustLabel inputProgExerciseTrustLabel = createTestInputProgExerciseTrustLabel(1L, true);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(progExercise);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> progExerciseInputResolver.resolveInput(inputProgExerciseTrustLabel));
    }

    @Test
    void resolveInput_ValidInputTrustLabel_ReturnsProgExerciseTrustLabel() {
        InputProgExerciseTrustLabel inputProgExerciseTrustLabel = createTestInputProgExerciseTrustLabel(1L, false);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(progExercise);

        ProgExerciseEntity progExerciseResolved = progExerciseInputResolver.resolveInput(inputProgExerciseTrustLabel);

        Assertions.assertEquals(inputProgExerciseTrustLabel.getId(), progExerciseResolved.getId());
        Assertions.assertEquals(inputProgExerciseTrustLabel.getTrustLabel(), progExerciseResolved.getTrustLabel().label);
    }
}
