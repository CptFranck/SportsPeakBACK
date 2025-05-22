package com.CptFranck.SportsPeak.integration.resolvers;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repository.TargetSetRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.resolver.PerformanceLogInputResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.createTestInputNewPerformanceLog;
import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.createTestInputPerformanceLog;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class PerformanceLogInputResolverIT {

    @Autowired
    private PerformanceLogInputResolver performanceLogInputResolver;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    private TargetSetEntity targetSet;

    @BeforeEach
    void setUp() {
        UserEntity creator = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, creator, exercise));
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
    void resolveInput_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        InputNewPerformanceLog newPerformanceLog = createTestInputPerformanceLog(null, 1L, false);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> performanceLogInputResolver.resolveInput(newPerformanceLog));
    }

    @Test
    void resolveInput_InvalidNewInputLabel_ThrowLabelMatchNotFoundException() {
        InputNewPerformanceLog newPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> performanceLogInputResolver.resolveInput(newPerformanceLog));
    }

    @Test
    void resolveInput_ValidInputNewPerformanceLog_ReturnPerformanceLogEntity() {
        InputNewPerformanceLog newPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId(), false);

        PerformanceLogEntity performanceLogSaved = performanceLogInputResolver.resolveInput(newPerformanceLog);

        assertPerformanceLogInputAndEntity(newPerformanceLog, performanceLogSaved);
    }

    @Test
    void resolveInput_ValidInputPerformanceLog_ReturnPerformanceLogEntity() {
        InputPerformanceLog newPerformanceLog = createTestInputPerformanceLog(1L, targetSet.getId(), false);

        PerformanceLogEntity performanceLogSaved = performanceLogInputResolver.resolveInput(newPerformanceLog);

        Assertions.assertEquals(newPerformanceLog.getId(), performanceLogSaved.getId());
        assertPerformanceLogInputAndEntity(newPerformanceLog, performanceLogSaved);
    }

    private void assertPerformanceLogInputAndEntity(InputNewPerformanceLog expectedPerformanceLog, PerformanceLogEntity actualPerformanceLog) {
        Assertions.assertEquals(expectedPerformanceLog.getSetIndex(), actualPerformanceLog.getSetIndex());
        Assertions.assertEquals(expectedPerformanceLog.getRepetitionNumber(), actualPerformanceLog.getRepetitionNumber());
        Assertions.assertEquals(expectedPerformanceLog.getWeight(), actualPerformanceLog.getWeight());
        Assertions.assertEquals(expectedPerformanceLog.getWeightUnit(), actualPerformanceLog.getWeightUnit().label);
        Assertions.assertEquals(expectedPerformanceLog.getLogDate(), actualPerformanceLog.getLogDate());
        Assertions.assertEquals(expectedPerformanceLog.getTargetSetId(), actualPerformanceLog.getTargetSet().getId());
    }
}
