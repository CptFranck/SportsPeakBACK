package com.CptFranck.SportsPeak.integration.services.managers;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.repository.*;
import com.CptFranck.SportsPeak.service.managerImpl.TargetSetManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.assertEqualPerformanceLog;
import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class TargetSetManagerImplIT {

    @Autowired
    private TargetSetManagerImpl targetSetManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private PerformanceLogRepository performanceLogRepository;

    private ProgExerciseEntity progExercise;
    private TargetSetEntity targetSet;

    @BeforeEach
    void setUp() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
    }

    @AfterEach
    void afterEach() {
        performanceLogRepository.deleteAll();
        targetSetRepository.deleteAll();
        progExerciseRepository.deleteAll();
        userRepository.deleteAll();
        exerciseRepository.deleteAll();
    }

    @Test
    void savePerformanceLog_AddNewPerformanceLog_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(null, targetSet);

        PerformanceLogEntity performanceLogResolved = targetSetManager.savePerformanceLog(performanceLog);

        assertEqualPerformanceLog(performanceLog, performanceLogResolved);
    }

    @Test
    void savePerformanceLog_InvalidPerformanceId_ThrowPerformanceLogNotFoundException() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);

        Assertions.assertThrows(PerformanceLogNotFoundException.class, () -> targetSetManager.savePerformanceLog(performanceLog));
    }

    @Test
    void savePerformanceLog_UpdatePerformanceLog_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLog = performanceLogRepository.save(createTestPerformanceLog(null, targetSet));

        PerformanceLogEntity performanceLogResolved = targetSetManager.savePerformanceLog(performanceLog);

        assertEqualPerformanceLog(performanceLog, performanceLogResolved);
    }

    @Test
    void deleteTargetSet_InvalidId_Void() {
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetManager.deleteTargetSet(targetSet.getId()));
    }

    @Test
    void deleteTargetSet_ValidUse_Void() {
        TargetSetEntity targetSet2 = targetSetRepository.save(createTestTargetSet(null, progExercise, targetSet));
        TargetSetEntity targetSet3 = targetSetRepository.save(createTestTargetSet(null, progExercise, targetSet2));
        performanceLogRepository.save(createTestPerformanceLog(null, targetSet2));

        assertAll(() -> targetSetManager.deleteTargetSet(targetSet2.getId()));

        boolean targetSetFound = performanceLogRepository.existsById(targetSet2.getId());
        targetSet = targetSetRepository.findById(targetSet.getId()).orElseThrow();
        targetSet3 = targetSetRepository.findById(targetSet3.getId()).orElseThrow();
        Assertions.assertFalse(targetSetFound);
        Assertions.assertEquals(targetSet3.getTargetSetUpdate().getId(), targetSet.getId());
    }
}
