package com.CptFranck.SportsPeak.unit.services;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleStillUsedInExerciseException;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.impl.MuscleServiceImpl;
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

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.createTestMuscleList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MuscleServiceImplTest {

    @InjectMocks
    private MuscleServiceImpl muscleServiceImpl;

    @Mock
    private MuscleRepository muscleRepository;

    @Mock
    private ExerciseService exerciseService;

    private MuscleEntity muscle;

    @BeforeEach
    void setUp() {
        muscle = createTestMuscle(1L);
    }

    @Test
    void findAll_ValidUse_ReturnListOfMuscleEntity() {
        List<MuscleEntity> muscleList = createTestMuscleList(false);
        when(muscleRepository.findAll()).thenReturn(muscleList);

        List<MuscleEntity> muscleFound = muscleServiceImpl.findAll();

        Assertions.assertEquals(muscleList, muscleFound);
    }

    @Test
    void findOne_InvalidMuscleId_ThrowMuscleNotFoundException() {
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(MuscleNotFoundException.class, () -> muscleServiceImpl.findOne(muscle.getId() + 1));
    }

    @Test
    void findOne_ValidMuscleId_ReturnMuscleEntity() {
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));

        MuscleEntity muscleFound = muscleServiceImpl.findOne(muscle.getId());

        Assertions.assertEquals(muscle, muscleFound);
    }

    @Test
    void findMany_ValidMuscleIds_ReturnSetOfMuscleEntity() {
        List<MuscleEntity> muscleList = createTestMuscleList(false);
        Set<Long> muscleIds = muscleList.stream().map(MuscleEntity::getId).collect(Collectors.toSet());
        when(muscleRepository.findAllById(Mockito.anyIterable())).thenReturn(muscleList);

        Set<MuscleEntity> muscleFound = muscleServiceImpl.findMany(muscleIds);
        Assertions.assertEquals(new HashSet<>(muscleList), muscleFound);
    }

    @Test
    void save_AddNewMuscle_ReturnMuscleEntity() {
        MuscleEntity unsavedMuscle = createTestMuscle(null);
        when(muscleRepository.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);

        MuscleEntity muscleSaved = muscleServiceImpl.save(unsavedMuscle);

        Assertions.assertEquals(muscle, muscleSaved);
    }

    @Test
    void save_UpdateMuscleWithInvalidId_ReturnMuscleEntity() {
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(MuscleNotFoundException.class, () -> muscleServiceImpl.save(muscle));
    }

    @Test
    void save_UpdateMuscle_ReturnMuscleEntity() {
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));
        when(muscleRepository.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);

        MuscleEntity muscleSaved = muscleServiceImpl.save(muscle);

        Assertions.assertEquals(muscle, muscleSaved);
    }

    @Test
    void delete_InvalidMuscleId_ThrowMuscleNotFoundException() {
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(MuscleNotFoundException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void delete_MuscleStillUsed_ThrowMuscleNotFoundException() {
        muscle.getExercises().add(createTestExercise(1L));
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));

        assertThrows(MuscleStillUsedInExerciseException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));

        assertAll(() -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        when(muscleRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean muscleFound = muscleServiceImpl.exists(muscle.getId());

        Assertions.assertTrue(muscleFound);
    }
}
