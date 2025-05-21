package com.CptFranck.SportsPeak.integration.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputNewProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExerciseTrustLabel;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.resolver.ProgExerciseInputResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.*;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ProgExerciseInputResolverIT {

    @Autowired
    private ProgExerciseInputResolver progExerciseInputResolver;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;
    private ExerciseEntity exercise;

    @BeforeEach
    void setUp() {
        user = userRepository.save(createTestUser(null));
        exercise = exerciseRepository.save(createTestExercise(null));
    }

    @AfterEach
    void afterEach() {
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void resolveInput_InvalidNewInputVisibilityLabel_ThrowLabelMatchNotFoundException() {
        InputNewProgExercise inputNewProgExercise = createTestInputNewProgExercise(user.getId(), exercise.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> progExerciseInputResolver.resolveInput(inputNewProgExercise));
    }

    @Test
    void resolveInput_ValidNewInput_ReturnsProgExercise() {
        InputNewProgExercise inputNewProgExercise = createTestInputNewProgExercise(user.getId(), exercise.getId(), false);

        ProgExerciseEntity progExerciseResolved = progExerciseInputResolver.resolveInput(inputNewProgExercise);

        Assertions.assertEquals(inputNewProgExercise.getName(), progExerciseResolved.getName());
        Assertions.assertEquals(inputNewProgExercise.getNote(), progExerciseResolved.getNote());
        Assertions.assertEquals(inputNewProgExercise.getVisibility(), progExerciseResolved.getVisibility().label);
        Assertions.assertEquals(inputNewProgExercise.getCreatorId(), progExerciseResolved.getCreator().getId());
        Assertions.assertEquals(inputNewProgExercise.getExerciseId(), progExerciseResolved.getExercise().getId());
    }

    @Test
    void resolveInput_InvalidInputVisibilityLabel_ThrowLabelMatchNotFoundException() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        InputProgExercise inputNewProgExercise = createTestInputProgExercise(progExercise.getId(), exercise.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> progExerciseInputResolver.resolveInput(inputNewProgExercise));
    }

    @Test
    void resolveInput_ValidInput_ReturnsProgExercise() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        InputProgExercise inputNewProgExercise = createTestInputProgExercise(progExercise.getId(), exercise.getId(), false);

        ProgExerciseEntity progExerciseResolved = progExerciseInputResolver.resolveInput(inputNewProgExercise);

        Assertions.assertEquals(inputNewProgExercise.getId(), progExerciseResolved.getId());
        Assertions.assertEquals(inputNewProgExercise.getName(), progExerciseResolved.getName());
        Assertions.assertEquals(inputNewProgExercise.getNote(), progExerciseResolved.getNote());
        Assertions.assertEquals(inputNewProgExercise.getVisibility(), progExerciseResolved.getVisibility().label);
        Assertions.assertEquals(inputNewProgExercise.getExerciseId(), progExerciseResolved.getExercise().getId());
    }

    @Test
    void resolveInput_InvalidInputTrustLabel_ThrowLabelMatchNotFoundException() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        InputProgExerciseTrustLabel inputProgExerciseTrustLabel = createTestInputProgExerciseTrustLabel(progExercise.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> progExerciseInputResolver.resolveInput(inputProgExerciseTrustLabel));
    }

    @Test
    void resolveInput_ValidInputTrustLabel_ReturnsProgExerciseTrustLabel() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        InputProgExerciseTrustLabel inputProgExerciseTrustLabel = createTestInputProgExerciseTrustLabel(progExercise.getId(), false);

        ProgExerciseEntity progExerciseResolved = progExerciseInputResolver.resolveInput(inputProgExerciseTrustLabel);

        Assertions.assertEquals(inputProgExerciseTrustLabel.getId(), progExerciseResolved.getId());
        Assertions.assertEquals(inputProgExerciseTrustLabel.getTrustLabel(), progExerciseResolved.getTrustLabel().label);
    }
}
