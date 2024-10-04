package com.CptFranck.SportsPeak.service.UnitTest;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleStillUsedInExerciseException;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscleList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MuscleServiceImplTest {

    @Mock
    private MuscleRepository muscleRepository;

    @InjectMocks
    private MuscleServiceImpl muscleServiceImpl;

    private MuscleEntity muscle;

    @BeforeEach
    void setUp() {
        muscle = createTestMuscle(1L);
    }

    @Test
    void muscleService_Save_Success() {
        MuscleEntity unsavedMuscle = createTestMuscle(null);
        when(muscleRepository.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);

        MuscleEntity muscleSaved = muscleServiceImpl.save(unsavedMuscle);

        Assertions.assertEquals(muscle, muscleSaved);
    }

    @Test
    void muscleService_FindAll_Success() {
        List<MuscleEntity> muscleList = createTestMuscleList(false);
        when(muscleRepository.findAll()).thenReturn(muscleList);

        List<MuscleEntity> muscleFound = muscleServiceImpl.findAll();

        Assertions.assertEquals(muscleList, muscleFound);
    }

    @Test
    void muscleService_FindOne_Success() {
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));

        Optional<MuscleEntity> muscleFound = muscleServiceImpl.findOne(muscle.getId());

        Assertions.assertTrue(muscleFound.isPresent());
        Assertions.assertEquals(muscle, muscleFound.get());
    }

    @Test
    void muscleService_FindMany_Success() {
        List<MuscleEntity> muscleList = createTestMuscleList(false);
        Set<Long> muscleIds = muscleList.stream().map(MuscleEntity::getId).collect(Collectors.toSet());
        when(muscleRepository.findAllById(Mockito.anyIterable())).thenReturn(muscleList);

        Set<MuscleEntity> muscleFound = muscleServiceImpl.findMany(muscleIds);
        Assertions.assertEquals(new HashSet<>(muscleList), muscleFound);
    }

    @Test
    void muscleService_Exists_Success() {
        when(muscleRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean muscleFound = muscleServiceImpl.exists(muscle.getId());

        Assertions.assertTrue(muscleFound);
    }

    @Test
    void muscleService_Delete_Unsuccessful() {
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(MuscleNotFoundException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void muscleService_Delete_Unsuccessful_Exception() {
        muscle.getExercises().add(createTestExercise(1L));
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));

        assertThrows(MuscleStillUsedInExerciseException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void muscleService_Delete_Success() {
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));

        assertAll(() -> muscleServiceImpl.delete(muscle.getId()));
    }
}
