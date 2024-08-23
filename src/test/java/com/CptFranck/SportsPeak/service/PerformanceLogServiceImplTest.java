package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.repositories.PerformanceLogRepository;
import com.CptFranck.SportsPeak.service.impl.PerformanceLogServiceImpl;
import org.junit.jupiter.api.Assertions;
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
    PerformanceLogRepository performanceLogRepository;

    @InjectMocks
    PerformanceLogServiceImpl performanceLogServiceImpl;

    private TargetSetEntity getTargetSetEntity() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        return createTestTargetSet(1L, progExercise, null);
    }

    @Test
    void performanceLogService_Save_Success() {
        TargetSetEntity targetSet = getTargetSetEntity();
        PerformanceLogEntity PerformanceLog = createTestPerformanceLog(null, targetSet);
        PerformanceLogEntity PerformanceLogSavedInRepository = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(PerformanceLogSavedInRepository);

        PerformanceLogEntity PerformanceLogSaved = performanceLogServiceImpl.save(PerformanceLog);

        Assertions.assertEquals(PerformanceLogSavedInRepository, PerformanceLogSaved);
    }

    @Test
    void performanceLogService_FindAll_Success() {
        TargetSetEntity targetSet = getTargetSetEntity();
        List<PerformanceLogEntity> PerformanceLogList = createNewTestPerformanceLogList(targetSet);
        when(performanceLogRepository.findAll()).thenReturn(PerformanceLogList);

        List<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findAll();

        Assertions.assertEquals(PerformanceLogList, PerformanceLogFound);
    }

    @Test
    void performanceLogService_FindOne_Success() {
        TargetSetEntity targetSet = getTargetSetEntity();
        PerformanceLogEntity PerformanceLogEntity = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(PerformanceLogEntity));

        Optional<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findOne(PerformanceLogEntity.getId());

        Assertions.assertTrue(PerformanceLogFound.isPresent());
        Assertions.assertEquals(PerformanceLogEntity, PerformanceLogFound.get());
    }

    @Test
    void performanceLogService_FindMany_Success() {
        TargetSetEntity targetSet = getTargetSetEntity();
        List<PerformanceLogEntity> PerformanceLogList = createNewTestPerformanceLogList(targetSet);
        Set<Long> PerformanceLogIds = PerformanceLogList.stream().map(PerformanceLogEntity::getId).collect(Collectors.toSet());
        when(performanceLogRepository.findAllById(Mockito.anyIterable())).thenReturn(PerformanceLogList);

        Set<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findMany(PerformanceLogIds);
        Assertions.assertEquals(new HashSet<>(PerformanceLogList), PerformanceLogFound);
    }

    @Test
    void performanceLogService_FindAllByTargetSetId_Success() {
        TargetSetEntity targetSet = getTargetSetEntity();
        List<PerformanceLogEntity> PerformanceLogList = createNewTestPerformanceLogList(targetSet);
        when(performanceLogRepository.findAllByTargetSetId(Mockito.any(Long.class))).thenReturn(PerformanceLogList);

        List<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findAllByTargetSetId(targetSet.getId());
        Assertions.assertEquals(PerformanceLogList, PerformanceLogFound);
    }

    @Test
    void PerformanceLogService_Exists_Success() {
        TargetSetEntity targetSet = getTargetSetEntity();
        PerformanceLogEntity PerformanceLogEntity = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean PerformanceLogFound = performanceLogServiceImpl.exists(PerformanceLogEntity.getId());

        Assertions.assertTrue(PerformanceLogFound);
    }

    @Test
    void PerformanceLogService_Delete_Success() {
        TargetSetEntity targetSet = getTargetSetEntity();
        PerformanceLogEntity PerformanceLogEntity = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(PerformanceLogEntity));

        assertAll(() -> performanceLogServiceImpl.delete(PerformanceLogEntity.getId()));
    }

    @Test
    void PerformanceLogService_Delete_Unsuccessful() {
        TargetSetEntity targetSet = getTargetSetEntity();
        PerformanceLogEntity PerformanceLogEntity = createTestPerformanceLog(1L, targetSet);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(PerformanceLogNotFoundException.class, () -> performanceLogServiceImpl.delete(PerformanceLogEntity.getId()));
    }
}
