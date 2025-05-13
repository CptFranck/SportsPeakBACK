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
import com.CptFranck.SportsPeak.repositories.*;
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
class TargetSetControllerIT {

    @Autowired
    private TargetSetController targetSetController;

    @Autowired
    private PerformanceLogRepository performanceLogRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private UserRepository userRepository;

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
        performanceLogRepository.deleteAll();
        targetSetRepository.deleteAll();
        progExerciseRepository.deleteAll();
        exerciseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getTargetSets_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.getTargetSets());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getTargetSets_ValidUse_ReturnListOfTargetSetDto() {
        List<TargetSetDto> targetSetDtos = targetSetController.getTargetSets();

        assertEqualTargeSetList(List.of(targetSet), targetSetDtos);
    }

    @Test
    void getTargetSetById_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.getTargetSetById(targetSet.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getTargetSetById_InvalidTargetSetId_ThrowsTargetSetNotFoundException() {
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.getTargetSetById(targetSet.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getTargetSetById_ValidInput_ReturnTargetSetDto() {
        TargetSetDto targetSetDto = targetSetController.getTargetSetById(targetSet.getId());

        assertTargetSetDtoAndEntity(targetSet, targetSetDto);
    }

    @Test
    void getTargetSetsByProgExerciseId_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> targetSetController.getTargetSetsByProgExerciseId(progExercise.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getTargetSetsByProgExerciseId_ValidInput_ReturnListOfTargetSetDto() {
        List<TargetSetDto> targetSetDtos = targetSetController.getTargetSetsByProgExerciseId(progExercise.getId());

        assertEqualTargeSetList(List.of(targetSet), targetSetDtos);
    }

    @Test
    void addTargetSet_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null, false);
        targetSetRepository.delete(targetSet);
        progExerciseRepository.delete(progExercise);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.addTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addTargetSet_InvalidProgExerciseId_ThrowsProgExerciseNotFoundException() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null, false);
        targetSetRepository.delete(targetSet);
        progExerciseRepository.delete(progExercise);

        Assertions.assertThrows(ProgExerciseNotFoundException.class, () -> targetSetController.addTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addTargetSet_InvalidInputLabel_ThrowsLabelMatchNotFoundException() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null, true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> targetSetController.addTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addTargetSet_ValidInput_ReturnTargetSetDto() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null, false);

        TargetSetDto targetSetDto = targetSetController.addTargetSet(inputNewTargetSet);

        assertTargetSetDtoAndInputNew(inputNewTargetSet, targetSetDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addTargetSet_ValidInputWithUpdate_ThrowsProgExerciseNotFoundException() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), targetSet.getId(), false);
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.addTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addTargetSet_ValidInputWithUpdate_ReturnTargetSetDto() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), targetSet.getId(), false);

        TargetSetDto targetSetDto = targetSetController.addTargetSet(inputNewTargetSet);

        targetSet = targetSetRepository.findById(targetSet.getId()).orElseThrow();
        assertTargetSetDtoAndInputNew(inputNewTargetSet, targetSetDto);
        Assertions.assertEquals(targetSet.getTargetSetUpdate().getId(), targetSetDto.getId());
    }

    @Test
    void modifyTargetSet_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        InputTargetSet inputNewTargetSet = createTestInputTargetSet(targetSet.getId(), false);
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.modifyTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSet_InvalidTargetSetId_ThrowsTargetSetNotFoundException() {
        InputTargetSet inputNewTargetSet = createTestInputTargetSet(targetSet.getId(), false);
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.modifyTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSet_InvalidInputLabel_ThrowsLabelMatchNotFoundException() {
        InputTargetSet inputNewTargetSet = createTestInputTargetSet(targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> targetSetController.modifyTargetSet(inputNewTargetSet));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSet_ValidInput_ReturnTargetSetDto() {
        InputTargetSet inputTargetSet = createTestInputTargetSet(targetSet.getId(), false);

        TargetSetDto targetSetDto = targetSetController.modifyTargetSet(inputTargetSet);

        assertTargetSetDtoAndInput(inputTargetSet, targetSetDto);
    }

    @Test
    void modifyTargetSetState_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), false);
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.modifyTargetSetState(inputTargetSetState));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSetState_InvalidTargetSetId_ThrowsTargetSetNotFoundException() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), false);
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.modifyTargetSetState(inputTargetSetState));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSetState_InvalidInputLabel_ThrowsLabelMatchNotFoundException() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class, () -> targetSetController.modifyTargetSetState(inputTargetSetState));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSetState_validInput_ReturnListOfTargetSetDto() {
        targetSetRepository.save(createTestTargetSet(null, progExercise, targetSet));
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), false);

        List<TargetSetDto> targetSetDtos = targetSetController.modifyTargetSetState(inputTargetSetState);

        targetSetDtos.forEach(targetSetDto -> Assertions.assertEquals(targetSetDto.getState(), inputTargetSetState.getState()));
    }

    @Test
    void deleteTargetSet_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> targetSetController.deleteTargetSet(targetSet.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteTargetSet_InvalidTargetSetId_ThrowsTargetSetNotFoundException() {
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> targetSetController.deleteTargetSet(targetSet.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteTargetSet_ValidInput_ReturnExerciseId() {
        TargetSetEntity targetSet2 = targetSetRepository.save(createTestTargetSet(null, progExercise, targetSet));
        TargetSetEntity targetSet3 = targetSetRepository.save(createTestTargetSet(null, progExercise, targetSet2));
        PerformanceLogEntity performanceLog = performanceLogRepository.save(createTestPerformanceLog(null, targetSet2));
        targetSet2.getPerformanceLogs().add(performanceLog);
        targetSet2 = targetSetRepository.save(targetSet2);

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