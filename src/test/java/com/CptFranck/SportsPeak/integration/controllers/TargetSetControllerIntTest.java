package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.TargetSetController;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSetState;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repositories.TargetSetRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.utils.TestDateTimeUtils.assertDatetimeWithTimestamp;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestInputDuration.assertInputDuration;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.*;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class TargetSetControllerIntTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private TargetSetController targetSetController;

    private ProgExerciseEntity progExercise;
    private TargetSetEntity targetSet;


    @BeforeEach
    void setUp() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
    }

    @AfterEach
    public void afterEach() {
        this.targetSetRepository.deleteAll();
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void TargetSetController_GetTargetSets_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.getTargetSets());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_GetTargetSets_Success() {
        List<TargetSetDto> targetSetDtos = targetSetController.getTargetSets();

        assertEqualTargeSetList(List.of(targetSet), targetSetDtos);
    }

    @Test
    void TargetSetController_GetTargetSetById_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.getTargetSetById(targetSet.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_GetTargetSetById_UnsuccessfulTargetSetNotFound() {
        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.getTargetSetById(targetSet.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_GetTargetSetById_Success() {
        TargetSetDto targetSetDto = targetSetController.getTargetSetById(targetSet.getId());

        assertTargetSetDtoAndEntity(targetSet, targetSetDto);
    }

    @Test
    void TargetSetController_GetTargetSetsByTargetId_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> targetSetController.getTargetSetsByProgExerciseId(progExercise.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_GetTargetSetsByTargetId_Success() {
        List<TargetSetDto> targetSetDtos = targetSetController.getTargetSetsByProgExerciseId(progExercise.getId());

        assertEqualTargeSetList(List.of(targetSet), targetSetDtos);
    }

    @Test
    void TargetSetController_AddTargetSet_UnsuccessfulNotAuthenticated() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null);
        targetSetRepository.delete(targetSet);
        progExerciseRepository.delete(progExercise);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.addTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_AddTargetSet_UnsuccessfulProgExerciseNotFound() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null);
        targetSetRepository.delete(targetSet);
        progExerciseRepository.delete(progExercise);

        Assertions.assertThrows(ProgExerciseNotFoundException.class, () -> targetSetController.addTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_AddTargetSet_Success() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null);

        TargetSetDto targetSetDto = targetSetController.addTargetSet(inputNewTargetSet);

        assertTargetSetDtoAndInputNew(inputNewTargetSet, targetSetDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_AddTargetSetWithUpdate_UnsuccessfulTargetSetNotFound() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), targetSet.getId());
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.addTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_AddTargetSetWithUpdate_Success() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), targetSet.getId());

        TargetSetDto targetSetDto = targetSetController.addTargetSet(inputNewTargetSet);

        targetSet = targetSetRepository.findById(targetSet.getId()).orElseThrow();
        assertTargetSetDtoAndInputNew(inputNewTargetSet, targetSetDto);
        Assertions.assertEquals(targetSet.getTargetSetUpdate().getId(), targetSetDto.getId());
    }

    @Test
    void TargetSetController_ModifyTargetSet_UnsuccessfulNotAuthenticated() {
        InputTargetSet inputNewTargetSet = createTestInputTargetSet(targetSet.getId());
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.modifyTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_ModifyTargetSet_UnsuccessfulTargetSetNotFound() {
        InputTargetSet inputNewTargetSet = createTestInputTargetSet(targetSet.getId());
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.modifyTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_ModifyTargetSet_Success() {
        InputTargetSet inputTargetSet = createTestInputTargetSet(targetSet.getId());

        TargetSetDto targetSetDto = targetSetController.modifyTargetSet(inputTargetSet);

        assertTargetSetDtoAndInput(inputTargetSet, targetSetDto);

    }

    @Test
    void TargetSetController_ModifyTargetSetState_UnsuccessfulNotAuthenticated() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), false);
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.modifyTargetSetState(inputTargetSetState));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_ModifyTargetSetState_UnsuccessfulTargetSetNotFound() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), false);
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.modifyTargetSetState(inputTargetSetState));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_ModifyTargetSetState_UnsuccessfulWrongLabel() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> targetSetController.modifyTargetSetState(inputTargetSetState));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_ModifyTargetSetState_Success() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), false);

        List<TargetSetDto> targetSetDtos = targetSetController.modifyTargetSetState(inputTargetSetState);

        targetSetDtos.forEach(targetSetDto -> Assertions.assertEquals(targetSetDto.getState(), inputTargetSetState.getState()));
    }

    @Test
    void TargetSetController_DeleteExercise_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.deleteTargetSet(targetSet.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void TargetSetController_DeleteExercise_UnsuccessfulExerciseNotFound() {
        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.deleteTargetSet(targetSet.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_DeleteTargetSet_Success() {
        TargetSetEntity targetSet2 = targetSetRepository.save(createTestTargetSet(null, progExercise, targetSet));
        TargetSetEntity targetSet3 = targetSetRepository.save(createTestTargetSet(null, progExercise, targetSet2));
        PerformanceLogEntity performanceLog = createTestPerformanceLog(null, targetSet2);
        targetSet2.getPerformanceLogs().add(performanceLog);
        targetSetRepository.save(targetSet2);

        Long id = targetSetController.deleteTargetSet(targetSet2.getId());

        targetSet = targetSetRepository.findById(targetSet.getId()).orElseThrow();
        targetSet3 = targetSetRepository.findById(targetSet3.getId()).orElseThrow();
        Assertions.assertEquals(targetSet2.getId(), id);
        Assertions.assertEquals(targetSet3.getTargetSetUpdate().getId(), targetSet.getId());
    }

    private void assertEqualTargeSetList(
            List<TargetSetEntity> targetSetEntities,
            List<TargetSetDto> targetSetDtos
    ) {
        Assertions.assertEquals(targetSetEntities.size(), targetSetDtos.size());
        targetSetDtos.forEach(targetSetDto -> assertTargetSetDtoAndEntity(
                targetSetEntities.stream().filter(
                        targetSetEntity -> Objects.equals(targetSetEntity.getId(), targetSetDto.getId())
                ).toList().getFirst(),
                targetSetDto)
        );
    }

    private void assertTargetSetDtoAndEntity(TargetSetEntity targetSetEntity, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(targetSetEntity.getId(), targetSetDto.getId());
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit().label, targetSetDto.getWeightUnit());
        Assertions.assertEquals(targetSetEntity.getPhysicalExertionUnitTime(), targetSetDto.getPhysicalExertionUnitTime().InputDurationToDuration());
        Assertions.assertEquals(targetSetEntity.getRestTime(), targetSetDto.getRestTime().InputDurationToDuration());
        assertDatetimeWithTimestamp(targetSetEntity.getCreationDate(), targetSetDto.getCreationDate());
        Assertions.assertEquals(targetSetEntity.getState().label, targetSetDto.getState());
        if (Objects.nonNull(targetSetEntity.getTargetSetUpdate()) || Objects.nonNull(targetSetDto.getTargetSetUpdate()))
            Assertions.assertEquals(targetSetEntity.getTargetSetUpdate().getId(), targetSetDto.getTargetSetUpdate().getId());
        Assertions.assertEquals(targetSetEntity.getPerformanceLogs().size(), targetSetDto.getPerformanceLogs().size());
    }

    private void assertTargetSetDtoAndInputNew(InputNewTargetSet inputNewTargetSet, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(inputNewTargetSet.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(inputNewTargetSet.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(inputNewTargetSet.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(inputNewTargetSet.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(inputNewTargetSet.getWeightUnit(), targetSetDto.getWeightUnit());
        assertInputDuration(inputNewTargetSet.getPhysicalExertionUnitTime(), targetSetDto.getPhysicalExertionUnitTime());
        assertInputDuration(inputNewTargetSet.getRestTime(), targetSetDto.getRestTime());
        assertDatetimeWithTimestamp(inputNewTargetSet.getCreationDate(), targetSetDto.getCreationDate());
    }

    private void assertTargetSetDtoAndInput(InputTargetSet targetSetEntity, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(targetSetEntity.getId(), targetSetDto.getId());
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit(), targetSetDto.getWeightUnit());
        assertInputDuration(targetSetEntity.getPhysicalExertionUnitTime(), targetSetDto.getPhysicalExertionUnitTime());
        assertInputDuration(targetSetEntity.getRestTime(), targetSetDto.getRestTime());
    }
}