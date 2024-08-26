package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.CptFranck.SportsPeak.service.impl.MuscleServiceImpl;
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

import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscleList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MuscleServiceImplTest {

    @Mock
    MuscleRepository muscleRepository;

    @InjectMocks
    MuscleServiceImpl muscleServiceImpl;

    @Test
    void muscleService_Save_Success() {
        MuscleEntity muscle = createTestMuscle(null);
        MuscleEntity muscleSavedInRepository = createTestMuscle(1L);
        when(muscleRepository.save(Mockito.any(MuscleEntity.class))).thenReturn(muscleSavedInRepository);

        MuscleEntity muscleSaved = muscleServiceImpl.save(muscle);

        Assertions.assertEquals(muscleSavedInRepository, muscleSaved);
    }

    @Test
    void muscleService_FindAll_Success() {
        List<MuscleEntity> muscleList = createTestMuscleList();
        when(muscleRepository.findAll()).thenReturn(muscleList);

        List<MuscleEntity> muscleFound = muscleServiceImpl.findAll();

        Assertions.assertEquals(muscleList, muscleFound);
    }

    @Test
    void muscleService_FindOne_Success() {
        MuscleEntity muscle = createTestMuscle(1L);
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));

        Optional<MuscleEntity> muscleFound = muscleServiceImpl.findOne(muscle.getId());

        Assertions.assertTrue(muscleFound.isPresent());
        Assertions.assertEquals(muscle, muscleFound.get());
    }

    @Test
    void muscleService_FindMany_Success() {
        List<MuscleEntity> muscleList = createTestMuscleList();
        Set<Long> muscleIds = muscleList.stream().map(MuscleEntity::getId).collect(Collectors.toSet());
        when(muscleRepository.findAllById(Mockito.anyIterable())).thenReturn(muscleList);

        Set<MuscleEntity> muscleFound = muscleServiceImpl.findMany(muscleIds);
        Assertions.assertEquals(new HashSet<>(muscleList), muscleFound);
    }

    @Test
    void muscleService_Exists_Success() {
        MuscleEntity muscle = createTestMuscle(1L);
        when(muscleRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean muscleFound = muscleServiceImpl.exists(muscle.getId());

        Assertions.assertTrue(muscleFound);
    }

    @Test
    void muscleService_Delete_Success() {
        MuscleEntity muscle = createTestMuscle(1L);
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));

        assertAll(() -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void muscleService_Delete_Unsuccessful() {
        MuscleEntity muscle = createTestMuscle(1L);
        when(muscleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(MuscleNotFoundException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }
}
