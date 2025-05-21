package com.CptFranck.SportsPeak.integration.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.domain.input.targetSet.AbstractTargetSetInput;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSetState;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repository.TargetSetRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.resolver.TargetSetInputResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.*;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class TargetSetInputResolverIT {

    @Autowired
    private TargetSetInputResolver targetSetInputResolver;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    private ProgExerciseEntity progExercise;
    private TargetSetEntity targetSet;

    @BeforeEach
    void setUp() {
        UserEntity creator = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, creator, exercise));
        targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
    }

    @AfterEach
    void afterEach() {
        targetSetRepository.deleteAll();
        progExerciseRepository.deleteAll();
        exerciseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void resolveInput_InvalidProgExerciseId_ThrowProgExerciseNotFoundException() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(1L, null, true);

        Assertions.assertThrows(ProgExerciseNotFoundException.class, () -> targetSetInputResolver.resolveInput(inputNewTargetSet));
    }

    @Test
    void resolveInput_InvalidNewInputLabel_ThrowLabelMatchNotFoundException() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null, true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> targetSetInputResolver.resolveInput(inputNewTargetSet));
    }

    @Test
    void resolveInput_ValidInputNewTargetSet_ReturnTargetSetEntity() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null, false);

        TargetSetEntity targetSetSaved = targetSetInputResolver.resolveInput(inputNewTargetSet);

        assertTargetSetInputAndEntity(inputNewTargetSet, targetSetSaved);
    }

    @Test
    void resolveInput_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        InputTargetSet inputTargetSet = createTestInputTargetSet(1L, false);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetInputResolver.resolveInput(inputTargetSet));
    }

    @Test
    void resolveInput_ValidInputPerformanceLog_ReturnExerciseTypeEntity() {
        InputTargetSet inputTargetSet = createTestInputTargetSet(targetSet.getId(), false);

        TargetSetEntity targetSetSaved = targetSetInputResolver.resolveInput(inputTargetSet);

        assertTargetSetInputAndEntity(inputTargetSet, targetSetSaved);
    }

    @Test
    void resolveInput_InvalidInputTargetStateLabel_ThrowLabelMatchNotFoundException() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(1L, true);
        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> targetSetInputResolver.resolveInput(inputTargetSetState));
    }

    @Test
    void resolveInput_ValidInputTargetState_ReturnTargetSetState() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(1L, false);

        TargetSetState targetSetStateResolved = targetSetInputResolver.resolveInput(inputTargetSetState);

        Assertions.assertEquals(inputTargetSetState.getState(), targetSetStateResolved.label);
    }

    private void assertTargetSetInputAndEntity(AbstractTargetSetInput expectedTargetSet, TargetSetEntity actualTargetSet) {
        if (expectedTargetSet instanceof InputTargetSet inputTargetSet)
            Assertions.assertEquals(inputTargetSet.getId(), actualTargetSet.getId());
        if (expectedTargetSet instanceof InputNewTargetSet inputNewTargetSet) {
            Assertions.assertEquals(inputNewTargetSet.getCreationDate(), actualTargetSet.getCreationDate());
            Assertions.assertEquals(inputNewTargetSet.getProgExerciseId(), actualTargetSet.getProgExercise().getId());
            if (inputNewTargetSet.getTargetSetUpdateId() != null)
                Assertions.assertEquals(inputNewTargetSet.getTargetSetUpdateId(), actualTargetSet.getTargetSetUpdate().getId());
        }
        Assertions.assertEquals(expectedTargetSet.getIndex(), actualTargetSet.getIndex());
        Assertions.assertEquals(expectedTargetSet.getSetNumber(), actualTargetSet.getSetNumber());
        Assertions.assertEquals(expectedTargetSet.getRepetitionNumber(), actualTargetSet.getRepetitionNumber());
        Assertions.assertEquals(expectedTargetSet.getWeight(), actualTargetSet.getWeight());
        Assertions.assertEquals(expectedTargetSet.getWeightUnit(), actualTargetSet.getWeightUnit().label);
        Assertions.assertEquals(expectedTargetSet.getPhysicalExertionUnitTime().InputDurationToDuration(),
                actualTargetSet.getPhysicalExertionUnitTime());
        Assertions.assertEquals(expectedTargetSet.getRestTime().InputDurationToDuration(),
                actualTargetSet.getRestTime());
    }
}
