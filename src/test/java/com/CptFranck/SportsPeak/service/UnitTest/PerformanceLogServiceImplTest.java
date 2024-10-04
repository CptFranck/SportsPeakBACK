package com.CptFranck.SportsPeak.service.UnitTest;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.repositories.PerformanceLogRepository;
import com.CptFranck.SportsPeak.service.impl.PerformanceLogServiceImpl;
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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.createNewTestPerformanceLogList;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PerformanceLogServiceImplTest {

    @Mock
    private PerformanceLogRepository performanceLogRepository;

    @InjectMocks
    private PerformanceLogServiceImpl performanceLogServiceImpl;

    private TargetSetEntity targetSet;

    @BeforeEach
    void setUp() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        targetSet = createTestTargetSet(1L, progExercise, null);
    }

    @Test
    void performanceLogService_Save_Success() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(null, targetSet);
        PerformanceLogEntity performanceLogSavedInRepository = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogSavedInRepository);

        PerformanceLogEntity performanceLogSaved = performanceLogServiceImpl.save(performanceLog);

        Assertions.assertEquals(performanceLogSavedInRepository, performanceLogSaved);
    }

    @Test
    void performanceLogService_FindAll_Success() {
        List<PerformanceLogEntity> performanceLogList = createNewTestPerformanceLogList(targetSet);
        when(performanceLogRepository.findAll()).thenReturn(performanceLogList);

        List<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findAll();

        Assertions.assertEquals(performanceLogList, performanceLogFound);
    }

    @Test
    void performanceLogService_FindOne_Success() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));

        Optional<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findOne(performanceLog.getId());

        Assertions.assertTrue(performanceLogFound.isPresent());
        Assertions.assertEquals(performanceLog, performanceLogFound.get());
    }

    @Test
    void performanceLogService_FindMany_Success() {
        List<PerformanceLogEntity> performanceLogList = createNewTestPerformanceLogList(targetSet);
        Set<Long> performanceLogIds = performanceLogList.stream().map(PerformanceLogEntity::getId).collect(Collectors.toSet());
        when(performanceLogRepository.findAllById(Mockito.anyIterable())).thenReturn(performanceLogList);

        Set<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findMany(performanceLogIds);
        Assertions.assertEquals(new HashSet<>(performanceLogList), PerformanceLogFound);
    }

    @Test
    void performanceLogService_FindAllByTargetSetId_Success() {
        List<PerformanceLogEntity> performanceLogList = createNewTestPerformanceLogList(targetSet);
        when(performanceLogRepository.findAllByTargetSetId(Mockito.any(Long.class))).thenReturn(performanceLogList);

        List<PerformanceLogEntity> performanceLogFound = performanceLogServiceImpl.findAllByTargetSetId(targetSet.getId());
        Assertions.assertEquals(performanceLogList, performanceLogFound);
    }

    @Test
    void PerformanceLogService_Exists_Success() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean performanceLogFound = performanceLogServiceImpl.exists(performanceLog.getId());

        Assertions.assertTrue(performanceLogFound);
    }

    @Test
    void PerformanceLogService_Delete_Success() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));

        assertAll(() -> performanceLogServiceImpl.delete(performanceLog.getId()));
    }

    @Test
    void PerformanceLogService_Delete_Unsuccessful() {
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(PerformanceLogNotFoundException.class, () -> performanceLogServiceImpl.delete(performanceLog.getId()));
    }
}
