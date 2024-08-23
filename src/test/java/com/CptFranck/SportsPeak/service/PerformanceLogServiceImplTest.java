package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
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

import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.createTestPerformanceLogList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PerformanceLogServiceImplTest {

    @Mock
    PerformanceLogRepository performanceLogRepository;

    @InjectMocks
    PerformanceLogServiceImpl performanceLogServiceImpl;

    @Test
    void performanceLogService_Save_Success() {
        PerformanceLogEntity PerformanceLogEntity = createTestPerformanceLog(null);
        PerformanceLogEntity PerformanceLogSavedInRepository = createTestPerformanceLog(1L);
        when(performanceLogRepository.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(PerformanceLogSavedInRepository);

        PerformanceLogEntity PerformanceLogSaved = performanceLogServiceImpl.save(PerformanceLogEntity);

        Assertions.assertEquals(PerformanceLogSavedInRepository, PerformanceLogSaved);
    }

    @Test
    void performanceLogService_FindAll_Success() {
        List<PerformanceLogEntity> PerformanceLogList = createTestPerformanceLogList();
        when(performanceLogRepository.findAll()).thenReturn(PerformanceLogList);

        List<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findAll();

        Assertions.assertEquals(PerformanceLogList, PerformanceLogFound);
    }

    @Test
    void performanceLogService_FindOne_Success() {
        PerformanceLogEntity PerformanceLogEntity = createTestPerformanceLog(1L);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(PerformanceLogEntity));

        Optional<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findOne(PerformanceLogEntity.getId());

        Assertions.assertTrue(PerformanceLogFound.isPresent());
        Assertions.assertEquals(PerformanceLogEntity, PerformanceLogFound.get());
    }

    @Test
    void performanceLogService_FindMany_Success() {
        List<PerformanceLogEntity> PerformanceLogList = createTestPerformanceLogList();
        Set<Long> PerformanceLogIds = PerformanceLogList.stream().map(PerformanceLogEntity::getId).collect(Collectors.toSet());
        when(performanceLogRepository.findAllById(Mockito.anyIterable())).thenReturn(PerformanceLogList);

        Set<PerformanceLogEntity> PerformanceLogFound = performanceLogServiceImpl.findMany(PerformanceLogIds);
        Assertions.assertEquals(new HashSet<>(PerformanceLogList), PerformanceLogFound);
    }

    @Test
    void PerformanceLogService_Exists_Success() {
        PerformanceLogEntity PerformanceLogEntity = createTestPerformanceLog(1L);
        when(performanceLogRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean PerformanceLogFound = performanceLogServiceImpl.exists(PerformanceLogEntity.getId());

        Assertions.assertTrue(PerformanceLogFound);
    }

    @Test
    void PerformanceLogService_Delete_Success() {
        PerformanceLogEntity PerformanceLogEntity = createTestPerformanceLog(1L);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(PerformanceLogEntity));

        assertAll(() -> performanceLogServiceImpl.delete(PerformanceLogEntity.getId()));
    }

    @Test
    void PerformanceLogService_Delete_Unsuccessful() {
        PerformanceLogEntity PerformanceLogEntity = createTestPerformanceLog(1L);
        when(performanceLogRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(PerformanceLogNotFoundException.class, () -> performanceLogServiceImpl.delete(PerformanceLogEntity.getId()));
    }
}
