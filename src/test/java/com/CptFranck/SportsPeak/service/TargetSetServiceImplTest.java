package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.repositories.TargetSetRepository;
import com.CptFranck.SportsPeak.service.impl.TargetSetServiceImpl;
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
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSetList;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TargetSetServiceImplTest {

    @Mock
    private TargetSetRepository targetSetRepository;

    @InjectMocks
    private TargetSetServiceImpl targetSetServiceImpl;

    private UserEntity user;

    private ExerciseEntity exercise;

    private ProgExerciseEntity progExercise;

    private TargetSetEntity targetSet;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
        exercise = createTestExercise(1L);
        progExercise = createTestProgExercise(1L, user, exercise);
        targetSet = createTestTargetSet(1L, progExercise, null);
    }


    @Test
    void TargetSetService_Save_Success() {
        TargetSetEntity unsavedTargetSet = createTestTargetSet(null, progExercise, null);
        when(targetSetRepository.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);

        TargetSetEntity targetSetSaved = targetSetServiceImpl.save(unsavedTargetSet);

        Assertions.assertEquals(targetSet, targetSetSaved);
    }

    @Test
    void TargetSetService_FindAll_Success() {
        List<TargetSetEntity> targetSetList = createTestTargetSetList(true, progExercise);
        when(targetSetRepository.findAll()).thenReturn(targetSetList);

        List<TargetSetEntity> targetSetFound = targetSetServiceImpl.findAll();

        Assertions.assertEquals(targetSetList, targetSetFound);
    }

    @Test
    void TargetSetService_FindOne_Success() {
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));

        Optional<TargetSetEntity> targetSetFound = targetSetServiceImpl.findOne(targetSet.getId());

        Assertions.assertTrue(targetSetFound.isPresent());
        Assertions.assertEquals(targetSet, targetSetFound.get());
    }

    @Test
    void TargetSetService_FindMany_Success() {
        List<TargetSetEntity> targetSetList = createTestTargetSetList(true, progExercise);
        Set<Long> targetSetIds = targetSetList.stream().map(TargetSetEntity::getId).collect(Collectors.toSet());
        when(targetSetRepository.findAllById(Mockito.anyIterable())).thenReturn(targetSetList);

        Set<TargetSetEntity> targetSetFound = targetSetServiceImpl.findMany(targetSetIds);
        Assertions.assertEquals(new HashSet<>(targetSetList), targetSetFound);
    }

    @Test
    void TargetSetService_FindAllByProgExerciseId_Success() {
        List<TargetSetEntity> targetSetList = createTestTargetSetList(true, progExercise);
        when(targetSetRepository.findAllByProgExerciseId(Mockito.any(Long.class))).thenReturn(targetSetList);

        List<TargetSetEntity> targetSetFound = targetSetServiceImpl.findAllByProgExerciseId(progExercise.getId());
        Assertions.assertEquals(targetSetList, targetSetFound);
    }

    @Test
    void TargetSetService_UpdatePreviousUpdateState_Success() {
        TargetSetEntity targetSetUpdate = createTestTargetSet(2L, progExercise, null);
        targetSet.setTargetSetUpdate(targetSetUpdate);
        when(targetSetRepository.findByTargetSetUpdateId(targetSet.getId())).thenReturn(Optional.of(targetSetUpdate));

        assertAll(() -> targetSetServiceImpl.updatePreviousUpdateState(targetSet.getId(), TargetSetState.HIDDEN));
    }

    @Test
    void TargetSetService_UpdatePreviousUpdateState_TargetSetNotFoundSuccess() {
        TargetSetEntity targetSet = createTestTargetSet(2L, progExercise, null);
        when(targetSetRepository.findByTargetSetUpdateId(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertAll(() -> targetSetServiceImpl.updatePreviousUpdateState(targetSet.getId(), TargetSetState.HIDDEN));
    }

    @Test
    void TargetSetService_Exists_Success() {
        when(targetSetRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean targetSetFound = targetSetServiceImpl.exists(targetSet.getId());

        Assertions.assertTrue(targetSetFound);
    }

    @Test
    void TargetSetService_Delete_Success() {
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));

        assertAll(() -> targetSetServiceImpl.delete(targetSet.getId()));
    }

    @Test
    void TargetSetService_Delete_Unsuccessful() {
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TargetSetNotFoundException.class, () -> targetSetServiceImpl.delete(targetSet.getId()));
    }
}
