package com.CptFranck.SportsPeak.service.IntegrationTest;

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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.createNewTestPerformanceLogList;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
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

    @BeforeEach
    void setUp() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(1L, user, exercise));
        targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
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
        PerformanceLogEntity performanceLog = performanceLogRepository.save(createTestPerformanceLog(null, targetSet));

        PerformanceLogEntity performanceLogSaved = performanceLogServiceImpl.save(performanceLog);

        assertEqualPerformanceLog(performanceLog, performanceLogSaved);
    }

    @Test
    void performanceLogService_FindAll_Success() {
        List<PerformanceLogEntity> performanceLogList = StreamSupport.stream(
                performanceLogRepository.saveAll(createNewTestPerformanceLogList(targetSet)).spliterator(),
                false).toList();

        List<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findAll();

        assertEqualPerformanceLogList(performanceLogList, performanceLogFound);
    }

    @Test
    void performanceLogService_FindOne_Success() {
        PerformanceLogEntity performanceLogSaved = performanceLogServiceImpl.save(createTestPerformanceLog(null, targetSet));

        Optional<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findOne(performanceLogSaved.getId());

        Assertions.assertTrue(performanceLogFound.isPresent());
        assertEqualPerformanceLog(performanceLogSaved, performanceLogFound.get());
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
        List<PerformanceLogEntity> performanceLogList = StreamSupport.stream(
                performanceLogRepository.saveAll(createNewTestPerformanceLogList(targetSet)).spliterator(),
                false).toList();

        List<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findAllByTargetSetId(targetSet.getId());

        assertEqualPerformanceLogList(performanceLogList, performanceLogFound);
    }

    @Test
    void PerformanceLogService_Exists_Success() {
        PerformanceLogEntity performanceLog = performanceLogServiceImpl.save(createTestPerformanceLog(null, targetSet));

        boolean performanceLogFound = performanceLogServiceImpl.exists(performanceLog.getId());

        Assertions.assertTrue(performanceLogFound);
    }

    @Test
    void PerformanceLogService_Delete_Success() {
        PerformanceLogEntity performanceLog = performanceLogServiceImpl.save(createTestPerformanceLog(null, targetSet));

        assertAll(() -> performanceLogServiceImpl.delete(performanceLog.getId()));
    }

    @Test
    void PerformanceLogService_Delete_Unsuccessful() {
        PerformanceLogEntity performanceLog = performanceLogServiceImpl.save(createTestPerformanceLog(null, targetSet));
        performanceLogRepository.delete(performanceLog);

        assertThrows(PerformanceLogNotFoundException.class, () -> performanceLogServiceImpl.delete(performanceLog.getId()));
    }

    private void assertEqualPerformanceLogList(
            List<PerformanceLogEntity> expectedPerformanceLogList,
            List<PerformanceLogEntity> PerformanceLogListObtained
    ) {
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
        Assertions.assertEquals(expected.getLogDate().getHour(), actual.getLogDate().getHour());
        Assertions.assertEquals(expected.getLogDate().getMinute(), actual.getLogDate().getMinute());
    }
}
