package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.TargetSetController;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.*;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TargetSetControllerTest {

    @InjectMocks
    private TargetSetController targetSetController;

    @Mock
    private Mapper<TargetSetEntity, TargetSetDto> TargetSetMapper;

    @Mock
    private TargetSetService targetSetService;

    @Mock
    private PerformanceLogService performanceLogService;

    @Mock
    private ProgExerciseService progExerciseService;

    private TargetSetEntity targetSet;
    private TargetSetDto targetSetDto;
    private ProgExerciseEntity progExercise;

    @BeforeEach
    void init() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        progExercise = createTestProgExercise(1L, user, exercise);
        targetSet = createTestTargetSet(1L, progExercise, null);
        progExercise.getTargetSets().add(targetSet);
        targetSetDto = createTestTargetSetDto(1L, null);
    }

    @Test
    void TargetSetController_GetTargetSets_Success() {
        when(targetSetService.findAll()).thenReturn(List.of(targetSet));
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        List<TargetSetDto> targetSetDtos = targetSetController.getTargetSets();

        Assertions.assertEquals(List.of(targetSetDto), targetSetDtos);
    }

    @Test
    void TargetSetController_GetTargetSetById_UnsuccessfulDoesNotExist() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> targetSetController.getTargetSetById(1L)
        );
    }

    @Test
    void TargetSetController_GetTargetSetById_Success() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        TargetSetDto targetSetDto = targetSetController.getTargetSetById(1L);

        Assertions.assertEquals(this.targetSetDto, targetSetDto);
    }

    @Test
    void TargetSetController_GetTargetSetsByTargetId_Success() {
        when(targetSetService.findAllByProgExerciseId(Mockito.any(Long.class))).thenReturn(List.of(targetSet));
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        List<TargetSetDto> targetSetDtos = targetSetController.getTargetSetsByProgExerciseId(1L);

        Assertions.assertNotNull(targetSetDtos);
    }

    @Test
    void TargetSetController_AddTargetSet_UnsuccessfulProgExerciseNotFound() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ProgExerciseNotFoundException.class,
                () -> targetSetController.addTargetSet(
                        createTestInputNewTargetSet(1L, null)
                )
        );
    }

    @Test
    void TargetSetController_AddTargetSet_Success() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        TargetSetDto targetSetDto = targetSetController.addTargetSet(
                createTestInputNewTargetSet(1L, null)
        );

        Assertions.assertEquals(this.targetSetDto, targetSetDto);
    }

    @Test
    void TargetSetController_AddTargetSetWithUpdate_UnsuccessfulTargetSetNotFound() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> targetSetController.addTargetSet(
                        createTestInputNewTargetSet(1L, 1L)
                )
        );
    }

    @Test
    void TargetSetController_AddTargetSetWithUpdate_Success() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        TargetSetDto targetSetDto = targetSetController.addTargetSet(
                createTestInputNewTargetSet(1L, 1L)
        );

        Assertions.assertEquals(this.targetSetDto, targetSetDto);
    }

    @Test
    void TargetSetController_ModifyTargetSet_UnsuccessfulTargetSetNotFound() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> targetSetController.modifyTargetSet(
                        createTestInputTargetSet(1L)
                )
        );
    }

    @Test
    void TargetSetController_ModifyTargetSet_Success() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        TargetSetDto targetSetDto = targetSetController.modifyTargetSet(
                createTestInputTargetSet(1L)
        );

        Assertions.assertEquals(this.targetSetDto, targetSetDto);
    }

    @Test
    void TargetSetController_ModifyTargetSetState_UnsuccessfulNotFound() {
        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> targetSetController.modifyTargetSetState(
                        createTestInputInputTargetSetState(1L, false)
                )
        );
    }

    @Test
    void TargetSetController_ModifyTargetSetState_UnsuccessfulWrongLabel() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> targetSetController.modifyTargetSetState(
                        createTestInputInputTargetSetState(1L, true)
                )
        );
    }

    @Test
    void TargetSetController_ModifyTargetSetState_Success() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        List<TargetSetDto> targetSetDtos = targetSetController.modifyTargetSetState(
                createTestInputInputTargetSetState(1L, false)
        );

        Assertions.assertEquals(List.of(targetSetDto), targetSetDtos);
    }

    @Test
    void TargetSetController_DeleteTargetSet_UnsuccessfulTargetSetNotFound() {
        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(false);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.deleteTargetSet(1L));
    }

    @Test
    void TargetSetController_DeleteTargetSet_Success() {
        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(performanceLogService.findAllByTargetSetId(Mockito.any())).thenReturn(
                List.of(createTestPerformanceLog(1L, targetSet))
        );

        Long id = targetSetController.deleteTargetSet(1L);

        Assertions.assertEquals(1L, id);
    }
}