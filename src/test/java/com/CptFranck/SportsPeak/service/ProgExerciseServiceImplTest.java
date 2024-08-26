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
        ProgExerciseEntity progExercise = createTestProgExercise(null, user, exercise);
        ProgExerciseEntity progExerciseSavedInRepository = createTestProgExercise(1L, user, exercise);
        when(progExerciseRepository.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseSavedInRepository);

        ProgExerciseEntity progExerciseSaved = progExerciseService.save(progExercise);

        Assertions.assertEquals(progExerciseSavedInRepository, progExerciseSaved);
    }

    @Test
    void progExerciseService_FindAll_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        List<ProgExerciseEntity> progExerciseList = createNewTestProgExerciseList(user, exercise);
        when(progExerciseRepository.findAll()).thenReturn(progExerciseList);

        List<ProgExerciseEntity> progExerciseFound = progExerciseService.findAll();

        Assertions.assertEquals(progExerciseList, progExerciseFound);
    }

    @Test
    void progExerciseService_FindOne_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        when(progExerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));

        Optional<ProgExerciseEntity> progExerciseFound = progExerciseService.findOne(progExercise.getId());

        Assertions.assertTrue(progExerciseFound.isPresent());
        Assertions.assertEquals(progExercise, progExerciseFound.get());
    }

    @Test
    void progExerciseService_FindMany_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        List<ProgExerciseEntity> progExerciseList = createNewTestProgExerciseList(user, exercise);
        Set<Long> progExerciseIds = progExerciseList.stream().map(ProgExerciseEntity::getId).collect(Collectors.toSet());
        when(progExerciseRepository.findAllById(Mockito.anyIterable())).thenReturn(progExerciseList);

        Set<ProgExerciseEntity> progExerciseFound = progExerciseService.findMany(progExerciseIds);

        Assertions.assertEquals(new HashSet<>(progExerciseList), progExerciseFound);
    }

    @Test
    void progExerciseService_Exists_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        when(progExerciseRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean progExerciseFound = progExerciseService.exists(progExercise.getId());

        Assertions.assertTrue(progExerciseFound);
    }

    @Test
    void progExerciseService_Delete_Success() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        when(progExerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));

        assertAll(() -> progExerciseService.delete(progExercise.getId()));
    }

    @Test
    void progExerciseService_Delete_Unsuccessful() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        when(progExerciseRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ProgExerciseNotFoundException.class, () -> progExerciseService.delete(progExercise.getId()));
    }
}
