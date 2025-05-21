package com.CptFranck.SportsPeak.unit.services.managers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseStillUsedException;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.CptFranck.SportsPeak.service.UserService;
import com.CptFranck.SportsPeak.service.managerImpl.ProgExerciseManagerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProgExerciseManagerImplTest {

    @InjectMocks
    private ProgExerciseManagerImpl progExerciseManager;

    @Mock
    private ProgExerciseService progExerciseService;

    @Mock
    private TargetSetService targetSetService;

    @Mock
    private UserService userService;

    private UserEntity user;
    private ProgExerciseEntity progExercise;

    @BeforeEach
    public void setUp() {
        user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        progExercise = createTestProgExercise(1L, user, exercise);
    }

    @Test
    void saveTargetSet_AddTargetSetWithoutUpdate_ReturnTargetSetEntity() {
        TargetSetEntity targetSet = createTestTargetSet(null, progExercise, null);
        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);

        TargetSetEntity targetSetSaved = progExerciseManager.saveTargetSet(targetSet, null);

        Assertions.assertEquals(targetSet, targetSetSaved);
    }

    @Test
    void saveTargetSet_AddTargetSetWithUpdate_ReturnTargetSetEntity() {
        TargetSetEntity targetSet = createTestTargetSet(null, progExercise, null);
        TargetSetEntity targetSetBis = createTestTargetSet(1L, progExercise, null);
        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(targetSetBis);

        TargetSetEntity targetSetSaved = progExerciseManager.saveTargetSet(targetSet, targetSetBis.getId());

        Assertions.assertEquals(targetSet, targetSetSaved);
        Assertions.assertEquals(targetSetBis.getTargetSetUpdate(), targetSetSaved);
    }

    @Test
    void deleteProgExercise_UserStillLinkedToProgExercise_Trow() {
        UserEntity userBis = createTestUser(1L);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(progExercise);
        when(userService.findUserBySubscribedProgExercises(Mockito.any(ProgExerciseEntity.class))).thenReturn(Set.of(userBis, user));

        assertThrows(ProgExerciseStillUsedException.class, () -> progExerciseManager.deleteProgExercise(progExercise.getId()));
    }

    @Test
    void deleteProgExercise_ValidUseWithoutUser_Void() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(progExercise);
        when(userService.findUserBySubscribedProgExercises(Mockito.any(ProgExerciseEntity.class))).thenReturn(Collections.emptySet());

        assertAll(() -> progExerciseManager.deleteProgExercise(progExercise.getId()));
    }

    @Test
    void deleteProgExercise_ValidUseWithUserAuthor_UpdatePrivilege_Void() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(progExercise);
        when(userService.findUserBySubscribedProgExercises(Mockito.any(ProgExerciseEntity.class))).thenReturn(Set.of(user));

        assertAll(() -> progExerciseManager.deleteProgExercise(progExercise.getId()));
    }
}
