package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.repositories.*;
import com.CptFranck.SportsPeak.service.impl.PerformanceLogServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.utils.TestDateTimeUtils.assertDatetimeWithTimestamp;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.createNewTestPerformanceLogList;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class PerformanceLogServiceImplIntTest {


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
    public void afterEach() {
        this.performanceLogRepository.deleteAll();
        this.targetSetRepository.deleteAll();
        this.progExerciseRepository.deleteAll();
        this.userRepository.deleteAll();
        this.exerciseRepository.deleteAll();
    }

    @Test
    void performanceLogService_Save_Success() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(null, targetSet);

        PerformanceLogEntity performanceLogSaved = performanceLogServiceImpl.save(performanceLog);

        assertEqualPerformanceLog(performanceLog, performanceLogSaved);
    }

    @Test
    void performanceLogService_FindAll_Success() {
        performanceLogRepository.deleteAll();
        List<PerformanceLogEntity> performanceLogList = StreamSupport.stream(
                performanceLogRepository.saveAll(createNewTestPerformanceLogList(targetSet)).spliterator(),
                false).toList();

        List<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findAll();

        assertEqualPerformanceLogList(performanceLogList, performanceLogFound);
    }

    @Test
    void performanceLogService_FindOne_Success() {
        Optional<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findOne(performanceLog.getId());

        Assertions.assertTrue(performanceLogFound.isPresent());
        assertEqualPerformanceLog(performanceLog, performanceLogFound.get());
    }

    @Test
    void performanceLogService_FindMany_Success() {
        List<PerformanceLogEntity> performanceLogList = StreamSupport.stream(
                performanceLogRepository.saveAll(createNewTestPerformanceLogList(targetSet)).spliterator(),
                false).toList();
        Set<Long> performanceLogIds = performanceLogList.stream().map(PerformanceLogEntity::getId).collect(Collectors.toSet());

        Set<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findMany(performanceLogIds);

        assertEqualPerformanceLogList(performanceLogList, PerformanceLogFound.stream().toList());
    }

    @Test
    void performanceLogService_FindAllByTargetSetId_Success() {
        performanceLogRepository.deleteAll();
        List<PerformanceLogEntity> performanceLogList = StreamSupport.stream(
                performanceLogRepository.saveAll(createNewTestPerformanceLogList(targetSet)).spliterator(),
                false).toList();

        List<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findAllByTargetSetId(targetSet.getId());

        assertEqualPerformanceLogList(performanceLogList, performanceLogFound);
    }

    @Test
    void PerformanceLogService_Exists_Success() {
        boolean performanceLogFound = performanceLogServiceImpl.exists(performanceLog.getId());

        Assertions.assertTrue(performanceLogFound);
    }

    @Test
    void PerformanceLogService_Delete_Success() {
        assertAll(() -> performanceLogServiceImpl.delete(performanceLog.getId()));
    }

    @Test
    void PerformanceLogService_Delete_Unsuccessful() {
        performanceLogRepository.delete(performanceLog);

        assertThrows(PerformanceLogNotFoundException.class, () -> performanceLogServiceImpl.delete(performanceLog.getId()));
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

    private void assertEqualPerformanceLog(PerformanceLogEntity expected, PerformanceLogEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getSetIndex(), actual.getSetIndex());
        Assertions.assertEquals(expected.getRepetitionNumber(), actual.getRepetitionNumber());
        Assertions.assertEquals(expected.getWeight(), actual.getWeight());
        Assertions.assertEquals(expected.getWeightUnit(), actual.getWeightUnit());
        assertDatetimeWithTimestamp(expected.getLogDate(), actual.getLogDate());
    }
}
