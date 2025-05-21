package com.CptFranck.SportsPeak.unit.services.managers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import com.CptFranck.SportsPeak.service.managerImpl.ExerciseManagerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.createTestExerciseType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseManagerImplTest {

    @InjectMocks
    private ExerciseManagerImpl exerciseManager;

    @Mock
    private ExerciseTypeService exerciseTypeService;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private MuscleService muscleService;

    @Test
    void saveExerciseType_AddNewExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity exerciseType = createTestExerciseType(null);
        when(exerciseTypeService.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseType);

        ExerciseTypeEntity exerciseTypeResolved = exerciseManager.saveExerciseType(exerciseType);

        Assertions.assertEquals(exerciseType, exerciseTypeResolved);
    }

    @Test
    void saveExerciseType_UpdateExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity exerciseType = createTestExerciseType(1L);
        when(exerciseTypeService.findOne(Mockito.any(Long.class))).thenReturn(exerciseType);
        when(exerciseTypeService.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseType);

        ExerciseTypeEntity exerciseTypeResolved = exerciseManager.saveExerciseType(exerciseType);

        Assertions.assertEquals(exerciseType, exerciseTypeResolved);
    }

    @Test
    void saveMuscle_AddNewMuscle_ReturnMuscleEntity() {
        MuscleEntity muscle = createTestMuscle(null);
        when(muscleService.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);

        MuscleEntity muscleResolved = exerciseManager.saveMuscle(muscle);

        Assertions.assertEquals(muscle, muscleResolved);
    }

    @Test
    void saveMuscle_UpdateMuscle_ReturnMuscleEntity() {
        MuscleEntity muscle = createTestMuscle(1L);
        when(muscleService.findOne(Mockito.any(Long.class))).thenReturn(muscle);
        when(muscleService.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);

        MuscleEntity muscleResolved = exerciseManager.saveMuscle(muscle);

        Assertions.assertEquals(muscle, muscleResolved);
    }
}
