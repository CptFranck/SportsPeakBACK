package com.CptFranck.SportsPeak.integration.services.services;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.repository.*;
import com.CptFranck.SportsPeak.service.serviceImpl.PerformanceLogServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.assertEqualPerformanceLog;
import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class PerformanceLogServiceImplIT {

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

    @Autowired
    private PerformanceLogServiceImpl performanceLogServiceImpl;

    private TargetSetEntity targetSet;
    private PerformanceLogEntity performanceLog;

    @BeforeEach
    void setUp() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        performanceLog = performanceLogRepository.save(createTestPerformanceLog(null, targetSet));
    }

    @AfterEach
    void afterEach() {
        this.performanceLogRepository.deleteAll();
        this.targetSetRepository.deleteAll();
        this.progExerciseRepository.deleteAll();
        this.userRepository.deleteAll();
        this.exerciseRepository.deleteAll();
    }

    @Test
    void findAll_ValidUse_ReturnListOfPerformanceLogEntity() {
        List<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findAll();

        assertEqualPerformanceLogList(List.of(performanceLog), performanceLogFound);
    }

    @Test
    void findOne_InvalidPerformanceLogId_ThrowPerformanceLogNotFoundException() {
        performanceLogRepository.delete(performanceLog);

        assertThrows(PerformanceLogNotFoundException.class, () -> performanceLogServiceImpl.findOne(performanceLog.getId()));
    }

    @Test
    void findOne_ValidPerformanceLogId_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLogFound = performanceLogServiceImpl.findOne(performanceLog.getId());

        assertEqualPerformanceLog(performanceLog, performanceLogFound);
    }

    @Test
    void findMany_ValidPerformanceLogIds_ReturnSetOfPerformanceLogEntity() {
        Set<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findMany(Set.of(performanceLog.getId()));

        assertEqualPerformanceLogList(List.of(performanceLog), PerformanceLogFound.stream().toList());
    }

    @Test
    void findAllByTargetSetId_ValidPerformanceLogIds_ReturnSetOfPerformanceLogEntity() {
        List<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findAllByTargetSetId(targetSet.getId());

        assertEqualPerformanceLogList(List.of(performanceLog), performanceLogFound);
    }

    @Test
    void save_AddNewPerformanceLog_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(null, targetSet);

        PerformanceLogEntity performanceLogSaved = performanceLogServiceImpl.save(performanceLog);

        assertEqualPerformanceLog(performanceLog, performanceLogSaved);
    }

    @Test
    void save_UpdatePerformanceLogWithInvalidId_ReturnPerformanceLogEntity() {
        PerformanceLogEntity unsavedPerformanceLog = createTestPerformanceLog(performanceLog.getId(), targetSet);
        performanceLogRepository.delete(performanceLog);

        assertThrows(PerformanceLogNotFoundException.class, () -> performanceLogServiceImpl.save(unsavedPerformanceLog));
    }

    @Test
    void save_UpdatePerformanceLog_ReturnPerformanceLogEntity() {
        PerformanceLogEntity unsavedPerformanceLog = createTestPerformanceLog(performanceLog.getId(), targetSet);

        PerformanceLogEntity performanceLogSaved = performanceLogServiceImpl.save(unsavedPerformanceLog);

        assertEqualPerformanceLog(performanceLog, performanceLogSaved);
    }

    @Test
    void delete_InvalidPerformanceLogId_ThrowPerformanceLogNotFoundException() {
        performanceLogRepository.delete(performanceLog);

        assertThrows(PerformanceLogNotFoundException.class, () -> performanceLogServiceImpl.delete(performanceLog.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        assertAll(() -> performanceLogServiceImpl.delete(performanceLog.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        boolean performanceLogFound = performanceLogServiceImpl.exists(performanceLog.getId());

        Assertions.assertTrue(performanceLogFound);
    }

    private void assertEqualPerformanceLogList(
            List<PerformanceLogEntity> expectedPerformanceLogList,
            List<PerformanceLogEntity> PerformanceLogListObtained
    ) {
        Assertions.assertEquals(expectedPerformanceLogList.size(), PerformanceLogListObtained.size());
        expectedPerformanceLogList.forEach(performanceLogFound -> assertEqualPerformanceLog(
                PerformanceLogListObtained.stream().filter(
                        performanceLog -> Objects.equals(performanceLog.getId(), performanceLogFound.getId())
                ).toList().getFirst(),
                performanceLogFound)
        );
    }
}
