package com.CptFranck.SportsPeak.unit.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.repositories.TargetSetRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.TargetSetServiceImpl;
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
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSetList;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TargetSetServiceImplTest {

    @Mock
    private TargetSetRepository targetSetRepository;

    @InjectMocks
    private TargetSetServiceImpl targetSetServiceImpl;

    private ProgExerciseEntity progExercise;

    private TargetSetEntity targetSet;

    @BeforeEach
    void setUp() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        progExercise = createTestProgExercise(1L, user, exercise);
        targetSet = createTestTargetSet(1L, progExercise, null);
    }

    @Test
    void findAll_ValidUse_ReturnListOfTargetSetEntity() {
        List<TargetSetEntity> targetSetList = createTestTargetSetList(true, progExercise);
        when(targetSetRepository.findAll()).thenReturn(targetSetList);

        List<TargetSetEntity> targetSetFound = targetSetServiceImpl.findAll();

        Assertions.assertEquals(targetSetList, targetSetFound);
    }

    @Test
    void findOne_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TargetSetNotFoundException.class, () -> targetSetServiceImpl.findOne(targetSet.getId()));
    }

    @Test
    void findOne_ValidTargetSetId_ReturnTargetSetEntity() {
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));

        TargetSetEntity targetSetFound = targetSetServiceImpl.findOne(targetSet.getId());

        Assertions.assertEquals(targetSet, targetSetFound);
    }

    @Test
    void findMany_ValidTargetSetIds_ReturnSetOfTargetSetEntity() {
        List<TargetSetEntity> targetSetList = createTestTargetSetList(true, progExercise);
        Set<Long> targetSetIds = targetSetList.stream().map(TargetSetEntity::getId).collect(Collectors.toSet());
        when(targetSetRepository.findAllById(Mockito.anyIterable())).thenReturn(targetSetList);

        Set<TargetSetEntity> targetSetFound = targetSetServiceImpl.findMany(targetSetIds);
        Assertions.assertEquals(new HashSet<>(targetSetList), targetSetFound);
    }

    @Test
    void findAllByTargetSetId_ValidTargetSetIds_ReturnSetOfTargetSetEntity() {
        List<TargetSetEntity> targetSetList = createTestTargetSetList(true, progExercise);
        when(targetSetRepository.findAllByProgExerciseId(Mockito.any(Long.class))).thenReturn(targetSetList);

        List<TargetSetEntity> targetSetFound = targetSetServiceImpl.findAllByProgExerciseId(progExercise.getId());
        Assertions.assertEquals(targetSetList, targetSetFound);
    }

    @Test
    void save_AddNewTargetSet_ReturnTargetSetEntity() {
        TargetSetEntity unsavedTargetSet = createTestTargetSet(null, progExercise, null);
        when(targetSetRepository.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);

        TargetSetEntity targetSetSaved = targetSetServiceImpl.save(unsavedTargetSet);

        Assertions.assertEquals(targetSet, targetSetSaved);
    }

    @Test
    void save_UpdateTargetSetWithInvalidId_ReturnTargetSetEntity() {
        TargetSetEntity unsavedTargetSet = createTestTargetSet(1L, progExercise, null);
        when(targetSetRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        assertThrows(TargetSetNotFoundException.class, () -> targetSetServiceImpl.save(unsavedTargetSet));
    }

    @Test
    void save_UpdateTargetSet_ReturnTargetSetEntity() {
        TargetSetEntity unsavedTargetSet = createTestTargetSet(1L, progExercise, null);
        when(targetSetRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        when(targetSetRepository.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);

        TargetSetEntity targetSetSaved = targetSetServiceImpl.save(unsavedTargetSet);

        Assertions.assertEquals(targetSet, targetSetSaved);
    }

    @Test
    void delete_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TargetSetNotFoundException.class, () -> targetSetServiceImpl.delete(targetSet.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        TargetSetEntity targetSetBis = createTestTargetSet(2L, progExercise, null);
        targetSet.setTargetSetUpdate(targetSetBis);
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(targetSetRepository.findByTargetSetUpdateId(Mockito.any(Long.class))).thenReturn(Optional.of(targetSetBis));

        assertAll(() -> targetSetServiceImpl.delete(targetSet.getId()));
    }

    @Test
    void setTheUpdate_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        when(targetSetRepository.findById(targetSet.getId())).thenReturn(Optional.empty());

        assertThrows(TargetSetNotFoundException.class, () -> targetSetServiceImpl.setTheUpdate(targetSet, 1L));
    }

    @Test
    void setTheUpdate_UpdateTargetSet_Void() {
        when(targetSetRepository.findById(targetSet.getId())).thenReturn(Optional.of(targetSet));

        assertAll(() -> targetSetServiceImpl.setTheUpdate(targetSet, 1L));
    }

    @Test
    void updateTargetStates_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TargetSetNotFoundException.class, () -> targetSetServiceImpl.updateTargetStates(targetSet.getId(), TargetSetState.HIDDEN));
    }

    @Test
    void updateTargetStates_InvalidTargetSetId_ReturnTargetSetEntity() {
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(targetSetRepository.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);
        when(targetSetRepository.findByTargetSetUpdateId(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet)).thenReturn(Optional.empty());

        assertAll(() -> targetSetServiceImpl.updateTargetStates(targetSet.getId(), TargetSetState.HIDDEN));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        when(targetSetRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean targetSetFound = targetSetServiceImpl.exists(targetSet.getId());

        Assertions.assertTrue(targetSetFound);
    }
}
