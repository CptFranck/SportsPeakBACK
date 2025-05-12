package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.TargetSetController;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolvers.TargetSetInputResolver;
import com.CptFranck.SportsPeak.service.ProgExerciseManager;
import com.CptFranck.SportsPeak.service.TargetSetManager;
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

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
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
    private TargetSetInputResolver targetSetInputResolver;

    @Mock
    private ProgExerciseManager progExerciseManager;

    @Mock
    private TargetSetManager targetSetManager;

    @Mock
    private TargetSetService targetSetService;

    private TargetSetEntity targetSet;
    private TargetSetDto targetSetDto;

    @BeforeEach
    void init() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        targetSet = createTestTargetSet(1L, progExercise, null);
        progExercise.getTargetSets().add(targetSet);
        targetSetDto = createTestTargetSetDto(1L, null);
    }

    @Test
    void getTargetSets_ValidUse_ReturnListOfTargetSetDto() {
        when(targetSetService.findAll()).thenReturn(List.of(targetSet));
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        List<TargetSetDto> targetSetDtos = targetSetController.getTargetSets();

        Assertions.assertEquals(List.of(targetSetDto), targetSetDtos);
    }

    @Test
    void getTargetSetById_ValidInput_ReturnTargetSetDto() {
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        TargetSetDto targetSetDto = targetSetController.getTargetSetById(1L);

        Assertions.assertEquals(this.targetSetDto, targetSetDto);
    }

    @Test
    void getTargetSetsByProgExerciseId_ValidInput_ReturnListOfTargetSetDto() {
        when(targetSetService.findAllByProgExerciseId(Mockito.any(Long.class))).thenReturn(List.of(targetSet));
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        List<TargetSetDto> targetSetDtos = targetSetController.getTargetSetsByProgExerciseId(1L);

        Assertions.assertNotNull(targetSetDtos);
    }

    @Test
    void addTargetSet_ValidInput_ReturnTargetSetDto() {
        when(targetSetInputResolver.resolveInput(Mockito.any(InputNewTargetSet.class))).thenReturn(targetSet);
        when(progExerciseManager.saveTargetSet(Mockito.any(TargetSetEntity.class), Mockito.any(Long.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        TargetSetDto targetSetDto = targetSetController.addTargetSet(createTestInputNewTargetSet(1L, 1L));

        Assertions.assertEquals(this.targetSetDto, targetSetDto);
    }

    @Test
    void modifyTargetSet_ValidInput_ReturnTargetSetDto() {
        when(targetSetInputResolver.resolveInput(Mockito.any(InputTargetSet.class))).thenReturn(targetSet);
        when(progExerciseManager.saveTargetSet(Mockito.any(TargetSetEntity.class), Mockito.isNull())).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        TargetSetDto targetSetDto = targetSetController.modifyTargetSet(createTestInputTargetSet(1L));

        Assertions.assertEquals(this.targetSetDto, targetSetDto);
    }

    @Test
    void modifyTargetSetState_InvalidLabelInput_ThrowLabelMatchNotFoundException() {
        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> targetSetController.modifyTargetSetState(createTestInputInputTargetSetState(1L, true)));
    }

    @Test
    void modifyTargetSetState_validInput_ReturnListOfTargetSetDto() {
        when(targetSetService.updateTargetStates(Mockito.any(Long.class), Mockito.any(TargetSetState.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        List<TargetSetDto> targetSetDtos = targetSetController.modifyTargetSetState(
                createTestInputInputTargetSetState(1L, false));

        Assertions.assertEquals(List.of(targetSetDto), targetSetDtos);
    }

    @Test
    void deleteTargetSet_ValidInput_ReturnExerciseId() {
        Long id = targetSetController.deleteTargetSet(1L);
        Assertions.assertEquals(1L, id);
    }
}