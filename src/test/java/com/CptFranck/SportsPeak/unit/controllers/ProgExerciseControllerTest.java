package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.ProgExerciseController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputNewProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExerciseTrustLabel;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.resolver.ProgExerciseInputResolver;
import com.CptFranck.SportsPeak.service.ProgExerciseManager;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.*;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUserDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgExerciseControllerTest {

    @InjectMocks
    private ProgExerciseController progExerciseController;

    @Mock
    private Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper;

    @Mock
    private ProgExerciseInputResolver progExerciseInputResolver;

    @Mock
    private ProgExerciseManager progExerciseManager;

    @Mock
    private ProgExerciseService progExerciseService;

    private ProgExerciseEntity progExercise;
    private ProgExerciseDto progExerciseDto;

    @BeforeEach
    void init() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        UserDto userDto = createTestUserDto(1L);
        ExerciseDto exerciseDto = createTestExerciseDto(1L);
        progExercise = createTestProgExercise(1L, user, exercise);
        progExerciseDto = createTestProgExerciseDto(1L, userDto, exerciseDto);
    }

    @Test
    void getProgExercises_ValidUse_ReturnListOfProgExerciseDto() {
        when(progExerciseService.findAll()).thenReturn(List.of(progExercise));
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        List<ProgExerciseDto> progExerciseDtos = progExerciseController.getProgExercises();

        Assertions.assertEquals(List.of(this.progExerciseDto), progExerciseDtos);
    }

    @Test
    void getProgExerciseById_ValidInput_ReturnProgExerciseDto() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(progExercise);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        ProgExerciseDto progExerciseDto = progExerciseController.getProgExerciseById(1L);

        Assertions.assertEquals(this.progExerciseDto, progExerciseDto);
    }

    @Test
    void addProgExercise_ValidInput_ReturnProgExerciseDto() {
        when(progExerciseInputResolver.resolveInput(Mockito.any(InputNewProgExercise.class))).thenReturn(progExercise);
        when(progExerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        ProgExerciseDto progExerciseDto = progExerciseController.addProgExercise(
                createTestInputNewProgExercise(1L, 1L, false));

        Assertions.assertEquals(this.progExerciseDto, progExerciseDto);
    }

    @Test
    void modifyProgExercise_ValidInput_ReturnProgExerciseDto() {
        when(progExerciseInputResolver.resolveInput(Mockito.any(InputProgExercise.class))).thenReturn(progExercise);
        when(progExerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        ProgExerciseDto progExerciseDto = progExerciseController.modifyProgExercise(
                createTestInputProgExercise(1L, 1L, false));

        Assertions.assertEquals(this.progExerciseDto, progExerciseDto);
    }

    @Test
    void modifyProgExerciseTrustLabel_ValidInput_ReturnProgExerciseDto() {
        when(progExerciseInputResolver.resolveInput(Mockito.any(InputProgExerciseTrustLabel.class))).thenReturn(progExercise);
        when(progExerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        ProgExerciseDto progExerciseDto = progExerciseController.modifyProgExerciseTrustLabel(
                createTestInputProgExerciseTrustLabel(1L, false));

        Assertions.assertEquals(this.progExerciseDto, progExerciseDto);
    }

    @Test
    void deleteProgExercise_ValidInput_ReturnProgExerciseId() {
        Long id = progExerciseController.deleteProgExercise(1L);

        Assertions.assertEquals(1L, id);
    }
}