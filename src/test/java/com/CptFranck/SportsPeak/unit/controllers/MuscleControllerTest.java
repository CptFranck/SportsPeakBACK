package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.MuscleController;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolvers.MuscleInputResolver;
import com.CptFranck.SportsPeak.service.MuscleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MuscleControllerTest {

    @InjectMocks
    private MuscleController muscleController;

    @Mock
    private MuscleService muscleService;

    @Mock
    private MuscleInputResolver muscleInputResolver;

    @Mock
    private Mapper<MuscleEntity, MuscleDto> muscleMapper;

    private MuscleEntity muscle;
    private MuscleDto muscleDto;

    @BeforeEach
    void init() {
        muscle = createTestMuscle(1L);
        muscleDto = createTestMuscleDto(1L);
    }

    @Test
    void getMuscles_ValidUse_ReturnListOfMuscleDto() {
        when(muscleService.findAll()).thenReturn(List.of(muscle));
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

        List<MuscleDto> muscleDtos = muscleController.getMuscles();

        Assertions.assertEquals(List.of(muscleDto), muscleDtos);
    }

    @Test
    void getMuscleById_ValidMuscleId_ReturnMuscleDto() {
        when(muscleService.findOne(Mockito.any(Long.class))).thenReturn(muscle);
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

        MuscleDto muscleDto = muscleController.getMuscleById(1L);

        Assertions.assertEquals(this.muscleDto, muscleDto);
    }

    @Test
    void addMuscle_ValidInput_ReturnMuscleDto() {
        when(muscleInputResolver.resolveInput(Mockito.any(InputNewMuscle.class))).thenReturn(muscle);
        when(muscleService.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

        MuscleDto muscleDto = muscleController.addMuscle(createTestInputNewMuscle());

        Assertions.assertEquals(this.muscleDto, muscleDto);
    }

    @Test
    void modifyMuscle_ValidInput_ReturnMuscleDto() {
        when(muscleInputResolver.resolveInput(Mockito.any(InputMuscle.class))).thenReturn(muscle);
        when(muscleService.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

        MuscleDto muscleDto = muscleController.modifyMuscle(createTestInputMuscle(1L));

        Assertions.assertEquals(this.muscleDto, muscleDto);
    }

    @Test
    void deleteMuscle_ValidInput_ReturnMuscleId() {
        Long id = muscleController.deleteMuscle(1L);
        Assertions.assertEquals(1L, id);
    }
}