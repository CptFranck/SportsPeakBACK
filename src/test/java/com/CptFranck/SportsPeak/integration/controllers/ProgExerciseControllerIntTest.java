package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.ProgExerciseController;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseStillUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputNewProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExerciseTrustLabel;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.*;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserBis;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ProgExerciseControllerIntTest {

    @Autowired
    private ProgExerciseController progExerciseController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    private UserEntity user;

    private ExerciseEntity exercise;

    @BeforeEach
    void setUp() {
        user = userRepository.save(createTestUser(null));
        exercise = exerciseRepository.save(createTestExercise(null));
    }

    @AfterEach
    public void afterEach() {
        userRepository.findAll().forEach(user -> {
            user.setSubscribedProgExercises(new HashSet<>());
            userRepository.save(user);
        });
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void ProgExerciseController_GetProgExercises_Success() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));

        List<ProgExerciseDto> progExerciseDtos = progExerciseController.getProgExercises();

        assertEqualExerciseList(List.of(progExercise), progExerciseDtos);
    }

    @Test
    void ProgExerciseController_GetProgExerciseById_UnsuccessfulProgExerciseNotFound() {
        Assertions.assertThrows(ProgExerciseNotFoundException.class,
                () -> progExerciseController.getProgExerciseById(1L)
        );
    }

    @Test
    void ProgExerciseController_GetProgExerciseById_Success() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));

        ProgExerciseDto progExerciseDto = progExerciseController.getProgExerciseById(progExercise.getId());

        assertProgExerciseDtoAndEntity(progExercise, progExerciseDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseController_AddProgExercise_UnsuccessfulUserNotFound() {
        InputNewProgExercise inputNewExercise = createTestInputNewProgExercise(user.getId(), exercise.getId());
        userRepository.delete(user);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> progExerciseController.addProgExercise(inputNewExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseController_AddProgExercise_UnsuccessfulExerciseNotFound() {
        InputNewProgExercise inputNewExercise = createTestInputNewProgExercise(user.getId(), exercise.getId());
        exerciseRepository.delete(exercise);

        Assertions.assertThrows(ExerciseNotFoundException.class,
                () -> progExerciseController.addProgExercise(inputNewExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseController_AddProgExercise_Success() {
        InputNewProgExercise inputNewExercise = createTestInputNewProgExercise(user.getId(), exercise.getId());

        ProgExerciseDto progExerciseDto = progExerciseController.addProgExercise(inputNewExercise);

        assertProgExerciseDtoAndInputNew(inputNewExercise, progExerciseDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseController_ModifyProgExercise_UnsuccessfulProgExerciseNotFound() {
        InputProgExercise inputNewExercise = createTestInputProgExercise(1L, user.getId(), false);

        Assertions.assertThrows(ProgExerciseNotFoundException.class,
                () -> progExerciseController.modifyProgExercise(inputNewExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseController_ModifyProgExercise_UnsuccessfulWrongLabel() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        InputProgExercise inputNewExercise =
                createTestInputProgExercise(progExercise.getId(), exercise.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> progExerciseController.modifyProgExercise(inputNewExercise));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseController_ModifyProgExercise_Success() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        InputProgExercise inputNewExercise =
                createTestInputProgExercise(progExercise.getId(), exercise.getId(), false);

        ProgExerciseDto progExerciseDto = progExerciseController.modifyProgExercise(inputNewExercise);

        assertProgExerciseDtoAndInput(inputNewExercise, progExerciseDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ProgExerciseController_ModifyProgExerciseTrustLabel_UnsuccessfulProgExerciseNotFound() {
        progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        InputProgExerciseTrustLabel inputProgExerciseTrustLabel =
                createTestInputProgExerciseTrustLabel(0L, false);

        Assertions.assertThrows(ProgExerciseNotFoundException.class,
                () -> progExerciseController.modifyProgExerciseTrustLabel(inputProgExerciseTrustLabel));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ProgExerciseController_ModifyProgExerciseTrustLabel_UnsuccessfulWrongLabel() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        InputProgExerciseTrustLabel inputProgExerciseTrustLabel =
                createTestInputProgExerciseTrustLabel(progExercise.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> progExerciseController.modifyProgExerciseTrustLabel(inputProgExerciseTrustLabel));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ProgExerciseController_ModifyProgExerciseTrustLabel_Success() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        InputProgExerciseTrustLabel inputProgExerciseTrustLabel =
                createTestInputProgExerciseTrustLabel(progExercise.getId(), false);

        ProgExerciseDto progExerciseDto = progExerciseController.modifyProgExerciseTrustLabel(inputProgExerciseTrustLabel);

        Assertions.assertEquals(inputProgExerciseTrustLabel.getId(), progExerciseDto.getId());
        Assertions.assertEquals(inputProgExerciseTrustLabel.getTrustLabel(), progExerciseDto.getTrustLabel());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseController_DeleteProgExercise_UnsuccessfulProgExerciseNotFound() {
        Assertions.assertThrows(ProgExerciseNotFoundException.class,
                () -> progExerciseController.deleteProgExercise(0L));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseController_DeleteProgExercise_UnsuccessfulProgExerciseStillUsed() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        UserEntity userBis = userRepository.save(createTestUserBis(null));
        userBis.getSubscribedProgExercises().add(progExercise);
        userRepository.save(userBis);

        Assertions.assertThrows(ProgExerciseStillUsedException.class,
                () -> progExerciseController.deleteProgExercise(progExercise.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseController_DeleteProgExercise_Success() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));

        Long id = progExerciseController.deleteProgExercise(progExercise.getId());

        Assertions.assertEquals(progExercise.getId(), id);
    }

    private void assertEqualExerciseList(
            List<ProgExerciseEntity> progExerciseEntities,
            List<ProgExerciseDto> progExerciseDtos
    ) {
        progExerciseDtos.forEach(progExerciseDto -> assertProgExerciseDtoAndEntity(
                progExerciseEntities.stream().filter(
                        progExercise -> Objects.equals(progExercise.getId(), progExerciseDto.getId())
                ).toList().getFirst(),
                progExerciseDto)
        );
    }

    private void assertProgExerciseDtoAndEntity(ProgExerciseEntity progExercise, ProgExerciseDto progExerciseDto) {
        Assertions.assertNotNull(progExerciseDto);
        Assertions.assertEquals(progExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(progExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(progExercise.getVisibility().label, progExerciseDto.getVisibility());
        Assertions.assertEquals(progExercise.getTrustLabel().label, progExerciseDto.getTrustLabel());
        Assertions.assertEquals(progExercise.getCreator().getId(), progExerciseDto.getCreator().getId());
        Assertions.assertEquals(progExercise.getExercise().getId(), progExerciseDto.getExercise().getId());
        Assertions.assertEquals(progExercise.getTargetSets().size(), progExerciseDto.getTargetSets().size());
    }

    private void assertProgExerciseDtoAndInputNew(InputNewProgExercise inputNewProgExercise, ProgExerciseDto progExerciseDto) {
        Assertions.assertNotNull(progExerciseDto);
        Assertions.assertEquals(inputNewProgExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(inputNewProgExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(inputNewProgExercise.getVisibility(), progExerciseDto.getVisibility());
        Assertions.assertEquals(inputNewProgExercise.getCreatorId(), progExerciseDto.getCreator().getId());
        Assertions.assertEquals(inputNewProgExercise.getExerciseId(), progExerciseDto.getExercise().getId());
    }

    private void assertProgExerciseDtoAndInput(InputProgExercise inputProgExercise, ProgExerciseDto progExerciseDto) {
        Assertions.assertNotNull(progExerciseDto);
        Assertions.assertEquals(inputProgExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(inputProgExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(inputProgExercise.getVisibility(), progExerciseDto.getVisibility());
        Assertions.assertEquals(inputProgExercise.getExerciseId(), progExerciseDto.getExercise().getId());
    }
}