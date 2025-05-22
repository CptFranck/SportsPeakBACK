package com.CptFranck.SportsPeak.unit.services.services;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.repository.PerformanceLogRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.PerformanceLogServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.createNewTestPerformanceLogList;
import static com.CptFranck.SportsPeak.utils.PerformanceLogTestUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PerformanceLogServiceImplTest {

    @InjectMocks
    private PerformanceLogServiceImpl performanceLogServiceImpl;

    @Mock
    private PerformanceLogRepository performanceLogRepository;

    private TargetSetEntity targetSet;

    @BeforeEach
    void setUp() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        targetSet = createTestTargetSet(1L, progExercise, null);
    }

    @Test
    void findAll_ValidUse_ReturnListOfPerformanceLogEntity() {
        List<PerformanceLogEntity> performanceLogList = createNewTestPerformanceLogList(targetSet);
        when(performanceLogRepository.findAll()).thenReturn(performanceLogList);

        List<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findAll();

        Assertions.assertEquals(performanceLogList, performanceLogFound);
    }

    @Test
    void findOne_InvalidPerformanceLogId_ThrowPerformanceLogNotFoundException() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));

        PerformanceLogEntity performanceLogFound = performanceLogServiceImpl.findOne(performanceLog.getId());

        Assertions.assertEquals(performanceLog, performanceLogFound);
    }

    @Test
    void findOne_ValidPerformanceLogId_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));

        PerformanceLogEntity performanceLogFound = performanceLogServiceImpl.findOne(performanceLog.getId());

        Assertions.assertEquals(performanceLog, performanceLogFound);
    }

    @Test
    void findMany_ValidPerformanceLogIds_ReturnSetOfPerformanceLogEntity() {
        List<PerformanceLogEntity> performanceLogList = createNewTestPerformanceLogList(targetSet);
        Set<Long> performanceLogIds = performanceLogList.stream().map(PerformanceLogEntity::getId).collect(Collectors.toSet());
        when(performanceLogRepository.findAllById(Mockito.anyIterable())).thenReturn(performanceLogList);

        Set<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findMany(performanceLogIds);
        Assertions.assertEquals(new HashSet<>(performanceLogList), PerformanceLogFound);
    }

    @Test
    void findAllByTargetSetId_ValidPerformanceLogIds_ReturnSetOfPerformanceLogEntity() {
        List<PerformanceLogEntity> performanceLogList = createNewTestPerformanceLogList(targetSet);
        when(performanceLogRepository.findAllByTargetSetId(Mockito.any(Long.class))).thenReturn(performanceLogList);

        List<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findAllByTargetSetId(targetSet.getId());
        Assertions.assertEquals(performanceLogList, performanceLogFound);
    }

    @Test
    void save_AddNewPerformanceLog_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(null, targetSet);
        PerformanceLogEntity performanceLogSavedInRepository = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogSavedInRepository);

        PerformanceLogEntity performanceLogSaved = performanceLogServiceImpl.save(performanceLog);

        Assertions.assertEquals(performanceLogSavedInRepository, performanceLogSaved);
    }

    @Test
    void save_UpdatePerformanceLogWithInvalidId_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        assertThrows(PerformanceLogNotFoundException.class, () -> performanceLogServiceImpl.save(performanceLog));
    }

    @Test
    void save_UpdatePerformanceLog_ReturnPerformanceLogEntity() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        when(performanceLogRepository.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);

        PerformanceLogEntity performanceLogSaved = performanceLogServiceImpl.save(performanceLog);

        Assertions.assertEquals(performanceLog, performanceLogSaved);
    }

    @Test
    void delete_InvalidPerformanceLogId_ThrowPerformanceLogNotFoundException() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(PerformanceLogNotFoundException.class, () -> performanceLogServiceImpl.delete(performanceLog.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));

        assertAll(() -> performanceLogServiceImpl.delete(performanceLog.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean performanceLogFound = performanceLogServiceImpl.exists(performanceLog.getId());

        Assertions.assertTrue(performanceLogFound);
    }
}
