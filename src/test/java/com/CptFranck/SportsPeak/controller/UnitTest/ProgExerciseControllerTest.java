package com.CptFranck.SportsPeak.controller.UnitTest;

import com.CptFranck.SportsPeak.controller.ProgExerciseController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseStillUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.*;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgExerciseControllerTest {

    @InjectMocks
    private ProgExerciseController progExerciseController;

    @Mock
    private Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper;

    @Mock
    private UserService userService;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private ProgExerciseService progExerciseService;

    private UserEntity user;
    private ExerciseEntity exercise;
    private ProgExerciseEntity progExercise;
    private ProgExerciseDto progExerciseDto;

    @BeforeEach
    void init() {
        user = createTestUser(1L);
        exercise = createTestExercise(1L);
        UserDto userDto = createTestUserDto(1L);
        ExerciseDto exerciseDto = createTestExerciseDto(1L);
        progExercise = createTestProgExercise(1L, user, exercise);
        progExerciseDto = createTestProgExerciseDto(1L, userDto, exerciseDto);
    }

    @Test
    void ProgExerciseController_GetProgExercises_Success() {
        when(progExerciseService.findAll()).thenReturn(List.of(progExercise));
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        List<ProgExerciseDto> progExerciseDtos = progExerciseController.getProgExercises();

        Assertions.assertNotNull(progExerciseDtos);
    }

    @Test
    void ProgExerciseController_GetProgExerciseById_Unsuccessful() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        ProgExerciseDto progExerciseDto = progExerciseController.getProgExerciseById(1L);

        Assertions.assertNull(progExerciseDto);
    }

    @Test
    void ProgExerciseController_GetProgExerciseById_Success() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        ProgExerciseDto progExerciseDto = progExerciseController.getProgExerciseById(1L);

        Assertions.assertNotNull(progExerciseDto);
    }

    @Test
    void ProgExerciseController_AddProgExercise_UnsuccessfulUserNotFound() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> progExerciseController.addProgExercise(
                        createTestInputNewProgExercise(1L, 1L)
                )
        );
    }

    @Test
    void ProgExerciseController_AddProgExercise_UnsuccessfulExerciseNotFound() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> progExerciseController.addProgExercise(
                        createTestInputNewProgExercise(1L, 1L)
                )
        );
    }

    @Test
    void ProgExerciseController_AddProgExercise_Success() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(progExerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        ProgExerciseDto progExerciseDto = progExerciseController.addProgExercise(
                createTestInputNewProgExercise(1L, 1L)
        );

        Assertions.assertNotNull(progExerciseDto);
    }

    @Test
    void ProgExerciseController_ModifyProgExercise_UnsuccessfulProgExerciseNotFound() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ProgExerciseNotFoundException.class,
                () -> progExerciseController.modifyProgExercise(
                        createTestInputProgExercise(1L, 1L, false)
                )
        );
    }

    @Test
    void ProgExerciseController_ModifyProgExercise_Unsuccessful() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> progExerciseController.modifyProgExercise(
                        createTestInputProgExercise(1L, 1L, false)
                )
        );
    }

    @Test
    void ProgExerciseController_ModifyProgExercise_UnsuccessfulWrongLabel() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> progExerciseController.modifyProgExercise(
                        createTestInputProgExercise(1L, 1L, true)
                )
        );
    }

    @Test
    void ProgExerciseController_ModifyProgExercise_Success() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(progExerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        ProgExerciseDto progExerciseDto = progExerciseController.modifyProgExercise(
                createTestInputProgExercise(1L, 1L, false)
        );

        Assertions.assertNotNull(progExerciseDto);
    }

    @Test
    void ProgExerciseController_ModifyProgExerciseTrustLabel_UnsuccessfulProgExerciseNotFound() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> progExerciseController.modifyProgExerciseTrustLabel(
                        createTestInputProgExerciseTrustLabel(1L, false)
                )
        );
    }

    @Test
    void ProgExerciseController_ModifyProgExerciseTrustLabel_UnsuccessfulWrongLabel() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> progExerciseController.modifyProgExerciseTrustLabel(
                        createTestInputProgExerciseTrustLabel(1L, true)
                )
        );
    }

    @Test
    void ProgExerciseController_ModifyProgExerciseTrustLabel_Success() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(progExerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        ProgExerciseDto progExerciseDto = progExerciseController.modifyProgExerciseTrustLabel(
                createTestInputProgExerciseTrustLabel(1L, false)
        );
        Assertions.assertNotNull(progExerciseDto);
    }

    @Test
    void ProgExerciseController_DeleteProgExercise_UnsuccessfulNotFound() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(ProgExerciseNotFoundException.class,
                () -> progExerciseController.deleteProgExercise(1L));
    }

    @Test
    void ProgExerciseController_DeleteProgExercise_UnsuccessfulProgExerciseStillUsed() {
        UserEntity userBis = createTestUser(2L);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(userService.findUserBySubscribedProgExercises(Mockito.any(ProgExerciseEntity.class))).thenReturn(Set.of(user, userBis));

        Assertions.assertThrows(ProgExerciseStillUsedException.class,
                () -> progExerciseController.deleteProgExercise(1L));
    }

    @Test
    void ProgExerciseController_DeleteProgExercise_Success() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(userService.findUserBySubscribedProgExercises(Mockito.any(ProgExerciseEntity.class))).thenReturn(Set.of(user));

        Long id = progExerciseController.deleteProgExercise(1L);

        Assertions.assertNotNull(id);
    }

    @Test
    void ProgExerciseController_DeleteProgExercise_SuccessWithNoSubscriber() {
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(userService.findUserBySubscribedProgExercises(Mockito.any(ProgExerciseEntity.class))).thenReturn(Set.of());

        Long id = progExerciseController.deleteProgExercise(1L);
        Assertions.assertNotNull(id);
    }
}