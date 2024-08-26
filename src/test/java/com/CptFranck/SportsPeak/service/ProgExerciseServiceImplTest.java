package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.service.impl.ProgExerciseServiceImpl;
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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createNewTestProgExerciseList;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProgExerciseServiceImplTest {

    @Mock
    ProgExerciseRepository progExerciseRepository;

    @InjectMocks
    ProgExerciseServiceImpl progExerciseService;


    @Test
    void progExerciseService_Save_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity ProgExerciseEntity = createTestProgExercise(null, user, exercise);
        ProgExerciseEntity ProgExerciseSavedInRepository = createTestProgExercise(1L, user, exercise);
        when(progExerciseRepository.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(ProgExerciseSavedInRepository);

        ProgExerciseEntity ProgExerciseSaved = progExerciseService.save(ProgExerciseEntity);

        Assertions.assertEquals(ProgExerciseSavedInRepository, ProgExerciseSaved);
    }

    @Test
    void progExerciseService_FindAll_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        List<ProgExerciseEntity> ProgExerciseList = createNewTestProgExerciseList(user, exercise);
        when(progExerciseRepository.findAll()).thenReturn(ProgExerciseList);

        List<ProgExerciseEntity> ProgExerciseFound = progExerciseService.findAll();

        Assertions.assertEquals(ProgExerciseList, ProgExerciseFound);
    }

    @Test
    void progExerciseService_FindOne_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity ProgExerciseEntity = createTestProgExercise(1L, user, exercise);
        when(progExerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(ProgExerciseEntity));

        Optional<ProgExerciseEntity> ProgExerciseFound = progExerciseService.findOne(ProgExerciseEntity.getId());

        Assertions.assertTrue(ProgExerciseFound.isPresent());
        Assertions.assertEquals(ProgExerciseEntity, ProgExerciseFound.get());
    }

    @Test
    void progExerciseService_FindMany_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        List<ProgExerciseEntity> ProgExerciseList = createNewTestProgExerciseList(user, exercise);
        Set<Long> ProgExerciseIds = ProgExerciseList.stream().map(ProgExerciseEntity::getId).collect(Collectors.toSet());
        when(progExerciseRepository.findAllById(Mockito.anyIterable())).thenReturn(ProgExerciseList);

        Set<ProgExerciseEntity> ProgExerciseFound = progExerciseService.findMany(ProgExerciseIds);

        Assertions.assertEquals(new HashSet<>(ProgExerciseList), ProgExerciseFound);
    }

    @Test
    void progExerciseService_Exists_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity ProgExerciseEntity = createTestProgExercise(1L, user, exercise);
        when(progExerciseRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean ProgExerciseFound = progExerciseService.exists(ProgExerciseEntity.getId());

        Assertions.assertTrue(ProgExerciseFound);
    }

    @Test
    void progExerciseService_Delete_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity ProgExerciseEntity = createTestProgExercise(1L, user, exercise);
        when(progExerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(ProgExerciseEntity));

        assertAll(() -> progExerciseService.delete(ProgExerciseEntity.getId()));
    }

    @Test
    void progExerciseService_Delete_Unsuccessful() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity ProgExerciseEntity = createTestProgExercise(1L, user, exercise);
        when(progExerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ProgExerciseNotFoundException.class, () -> progExerciseService.delete(ProgExerciseEntity.getId()));
    }
}
