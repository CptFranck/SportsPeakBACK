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
    TargetSetRepository targetSetRepository;

    @InjectMocks
    TargetSetServiceImpl targetSetServiceImpl;

    private ProgExerciseEntity getProgExerciseEntity() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        return createTestProgExercise(1L, user, exercise);
    }

    @Test
    void TargetSetService_Save_Success() {
        ProgExerciseEntity progExercise = getProgExerciseEntity();
        TargetSetEntity targetSet = createTestTargetSet(null, progExercise, null);
        TargetSetEntity targetSetSavedInRepository = createTestTargetSet(1L, progExercise, null);
        when(targetSetRepository.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetSavedInRepository);

        TargetSetEntity targetSetSaved = targetSetServiceImpl.save(targetSet);

        Assertions.assertEquals(targetSetSavedInRepository, targetSetSaved);
    }

    @Test
    void TargetSetService_FindAll_Success() {
        ProgExerciseEntity progExercise = getProgExerciseEntity();
        List<TargetSetEntity> targetSetList = createTestTargetSetList(true, progExercise);
        when(targetSetRepository.findAll()).thenReturn(targetSetList);

        List<TargetSetEntity> TargetSetFound = targetSetServiceImpl.findAll();

        Assertions.assertEquals(targetSetList, TargetSetFound);
    }

    @Test
    void TargetSetService_FindOne_Success() {
        ProgExerciseEntity progExercise = getProgExerciseEntity();
        TargetSetEntity targetSetEntity = createTestTargetSet(1L, progExercise, null);
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(targetSetEntity));

        Optional<TargetSetEntity> TargetSetFound = targetSetServiceImpl.findOne(targetSetEntity.getId());

        Assertions.assertTrue(TargetSetFound.isPresent());
        Assertions.assertEquals(targetSetEntity, TargetSetFound.get());
    }

    @Test
    void TargetSetService_FindMany_Success() {
        ProgExerciseEntity progExercise = getProgExerciseEntity();
        List<TargetSetEntity> targetSetList = createTestTargetSetList(true, progExercise);
        Set<Long> targetSetIds = targetSetList.stream().map(TargetSetEntity::getId).collect(Collectors.toSet());
        when(targetSetRepository.findAllById(Mockito.anyIterable())).thenReturn(targetSetList);

        Set<TargetSetEntity> TargetSetFound = targetSetServiceImpl.findMany(targetSetIds);
        Assertions.assertEquals(new HashSet<>(targetSetList), TargetSetFound);
    }

    @Test
    void TargetSetService_FindAllByProgExerciseId_Success() {
        ProgExerciseEntity progExercise = getProgExerciseEntity();
        List<TargetSetEntity> targetSetList = createTestTargetSetList(true, progExercise);
        when(targetSetRepository.findAllByProgExerciseId(Mockito.any(Long.class))).thenReturn(targetSetList);

        List<TargetSetEntity> TargetSetFound = targetSetServiceImpl.findAllByProgExerciseId(progExercise.getId());
        Assertions.assertEquals(targetSetList, TargetSetFound);
    }

    @Test
    void TargetSetService_UpdatePreviousUpdateState_Success() {
        ProgExerciseEntity progExercise = getProgExerciseEntity();
        TargetSetEntity targetSetUpdate = createTestTargetSet(2L, progExercise, null);
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, targetSetUpdate);
        when(targetSetRepository.findByTargetSetUpdateId(targetSet.getId())).thenReturn(Optional.of(targetSetUpdate));

        assertAll(() -> targetSetServiceImpl.updatePreviousUpdateState(targetSet.getId(), TargetSetState.HIDDEN));
    }

    @Test
    void TargetSetService_UpdatePreviousUpdateState_TargetSetNotFoundSuccess() {
        ProgExerciseEntity progExercise = getProgExerciseEntity();
        TargetSetEntity targetSet = createTestTargetSet(2L, progExercise, null);
        when(targetSetRepository.findByTargetSetUpdateId(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertAll(() -> targetSetServiceImpl.updatePreviousUpdateState(targetSet.getId(), TargetSetState.HIDDEN));
    }

    @Test
    void TargetSetService_Exists_Success() {
        ProgExerciseEntity progExercise = getProgExerciseEntity();
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, null);
        when(targetSetRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean TargetSetFound = targetSetServiceImpl.exists(targetSet.getId());

        Assertions.assertTrue(TargetSetFound);
    }

    @Test
    void TargetSetService_Delete_Success() {
        ProgExerciseEntity progExercise = getProgExerciseEntity();
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, null);
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));

        assertAll(() -> targetSetServiceImpl.delete(targetSet.getId()));
    }

    @Test
    void TargetSetService_Delete_Unsuccessful() {
        ProgExerciseEntity progExercise = getProgExerciseEntity();
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, null);
        when(targetSetRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TargetSetNotFoundException.class, () -> targetSetServiceImpl.delete(targetSet.getId()));
    }
}
