package com.CptFranck.SportsPeak.controller.UnitTest;

import com.CptFranck.SportsPeak.controller.MuscleController;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.MuscleService;
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

import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MuscleControllerTest {

    @InjectMocks
    private MuscleController muscleController;

    @Mock
    private Mapper<MuscleEntity, MuscleDto> muscleMapper;

    @Mock
    private MuscleService muscleService;

    @Mock
    private ExerciseService exerciseService;

    private MuscleEntity muscle;
    private MuscleDto muscleDto;

    @BeforeEach
    void init() {
        muscle = createTestMuscle(1L);
        muscleDto = createTestMuscleDto(1L);
    }

    @Test
    void MuscleController_GetMuscles_Success() {
        when(muscleService.findAll()).thenReturn(List.of(muscle));
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

        List<MuscleDto> muscleDtos = muscleController.getMuscles();

        Assertions.assertNotNull(muscleDtos);
    }

    @Test
    void MuscleController_GetMuscleById_Unsuccessful() {
        when(muscleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        MuscleDto muscleDto = muscleController.getMuscleById(1L);

        Assertions.assertNull(muscleDto);
    }

    @Test
    void MuscleController_GetMuscleById_Success() {
        when(muscleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

        MuscleDto muscleDto = muscleController.getMuscleById(1L);

        Assertions.assertNotNull(muscleDto);
    }

    @Test
    void MuscleController_AddMuscle_Success() {
        Set<ExerciseEntity> exercises = new HashSet<>();
        when(exerciseService.findMany(Mockito.anySet())).thenReturn(exercises);
        when(muscleService.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

        MuscleDto muscleDto = muscleController.addMuscle(createTestInputNewMuscle());

        Assertions.assertNotNull(muscleDto);
    }

    @Test
    void MuscleController_ModifyMuscle_UnsuccessfulDoesNotExist() {
        MuscleDto muscleDto = muscleController.modifyMuscle(createTestInputMuscle(1L));

        Assertions.assertNull(muscleDto);
    }

    @Test
    void MuscleController_ModifyMuscle_Success() {
        Set<ExerciseEntity> exercises = new HashSet<>();
        when(muscleService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(exerciseService.findMany(Mockito.anySet())).thenReturn(exercises);
        when(muscleService.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

        MuscleDto muscleDto = muscleController.modifyMuscle(createTestInputMuscle(1L));

        Assertions.assertNotNull(muscleDto);
    }

    @Test
    void MuscleController_DeleteMuscle_Success() {
        Long id = muscleController.deleteMuscle(1L);

        Assertions.assertNotNull(id);
    }
}