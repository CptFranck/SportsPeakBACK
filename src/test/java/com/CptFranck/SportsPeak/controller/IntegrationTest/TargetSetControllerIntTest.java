package com.CptFranck.SportsPeak.controller.IntegrationTest;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.domain.utils.TestDateTimeUtils.assertDatetimeWithTimestamp;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.createTestPerformanceLog;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.*;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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


    @BeforeEach
    void setUp() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
    }

    @AfterEach
    public void afterEach() {
        this.targetSetRepository.deleteAll();
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void TargetSetController_GetTargetSets_Success() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));

        List<TargetSetDto> targetSetDtos = targetSetController.getTargetSets();

        assertEqualExerciseList(List.of(targetSet), targetSetDtos);
    }

    @Test
    void TargetSetController_GetTargetSetById_UnsuccessfulDoesNotExist() {
        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> targetSetController.getTargetSetById(1L)
        );
    }

    @Test
    void TargetSetController_GetTargetSetById_Success() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));

        TargetSetDto targetSetDto = targetSetController.getTargetSetById(targetSet.getId());

        assertExerciseDtoAndEntity(targetSet, targetSetDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_GetTargetSetsByTargetId_Success() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));

        List<TargetSetDto> targetSetDtos = targetSetController.getTargetSetsByProgExerciseId(progExercise.getId());

        assertEqualExerciseList(List.of(targetSet), targetSetDtos);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_AddTargetSet_UnsuccessfulProgExerciseNotFound() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null);
        progExerciseRepository.delete(progExercise);

        Assertions.assertThrows(ProgExerciseNotFoundException.class,
                () -> targetSetController.addTargetSet(inputNewTargetSet)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_AddTargetSet_Success() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null);

        TargetSetDto targetSetDto = targetSetController.addTargetSet(inputNewTargetSet);

        assertExerciseDtoAndInputNew(inputNewTargetSet, targetSetDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_AddTargetSetWithUpdate_UnsuccessfulTargetSetNotFound() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), targetSet.getId());
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> targetSetController.addTargetSet(inputNewTargetSet)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_AddTargetSetWithUpdate_Success() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        targetSet = targetSetRepository.findById(targetSet.getId()).orElseThrow();
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), targetSet.getId());

        TargetSetDto targetSetDto = targetSetController.addTargetSet(inputNewTargetSet);

        targetSet = targetSetRepository.findById(targetSet.getId()).orElseThrow();
        assertExerciseDtoAndInputNew(inputNewTargetSet, targetSetDto);
        Assertions.assertEquals(targetSet.getTargetSetUpdate().getId(), targetSetDto.getId());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_ModifyTargetSet_UnsuccessfulTargetSetNotFound() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        InputTargetSet inputNewTargetSet = createTestInputTargetSet(targetSet.getId());
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> targetSetController.modifyTargetSet(inputNewTargetSet)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_ModifyTargetSet_Success() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        InputTargetSet inputTargetSet = createTestInputTargetSet(targetSet.getId());

        TargetSetDto targetSetDto = targetSetController.modifyTargetSet(inputTargetSet);

        assertExerciseDtoAndInput(inputTargetSet, targetSetDto);

    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_ModifyTargetSetState_UnsuccessfulTargetSetNotFound() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), false);
        targetSetRepository.delete(targetSet);

        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> targetSetController.modifyTargetSetState(inputTargetSetState)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_ModifyTargetSetState_UnsuccessfulWrongLabel() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> targetSetController.modifyTargetSetState(inputTargetSetState)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_ModifyTargetSetState_Success() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), false);

        List<TargetSetDto> targetSetDtos = targetSetController.modifyTargetSetState(inputTargetSetState);

        targetSetDtos.forEach(targetSetDto ->
                Assertions.assertEquals(targetSetDto.getState(), inputTargetSetState.getState())
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void TargetSetController_DeleteExercise_UnsuccessfulExerciseNotFound() {
        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> targetSetController.deleteTargetSet(1L)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void TargetSetController_DeleteTargetSet_Success() {
        TargetSetEntity targetSet1 = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        TargetSetEntity targetSet2 = targetSetRepository.save(createTestTargetSet(null, progExercise, targetSet1));
        TargetSetEntity targetSet3 = targetSetRepository.save(createTestTargetSet(null, progExercise, targetSet2));
        PerformanceLogEntity performanceLog = createTestPerformanceLog(null, targetSet2);
        targetSet2.getPerformanceLogs().add(performanceLog);
        targetSetRepository.save(targetSet2);

        Long id = targetSetController.deleteTargetSet(targetSet2.getId());

        targetSet1 = targetSetRepository.findById(targetSet1.getId()).orElseThrow();
        targetSet3 = targetSetRepository.findById(targetSet3.getId()).orElseThrow();
        Assertions.assertEquals(targetSet2.getId(), id);
        Assertions.assertEquals(targetSet3.getTargetSetUpdate().getId(), targetSet1.getId());
    }

    private void assertEqualExerciseList(
            List<TargetSetEntity> targetSetEntities,
            List<TargetSetDto> targetSetDtos
    ) {
        targetSetDtos.forEach(targetSetDto -> assertExerciseDtoAndEntity(
                targetSetEntities.stream().filter(
                        targetSetEntity -> Objects.equals(targetSetEntity.getId(), targetSetDto.getId())
                ).toList().getFirst(),
                targetSetDto)
        );
    }

    private void assertExerciseDtoAndEntity(TargetSetEntity targetSetEntity, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit().label, targetSetDto.getWeightUnit());
        Assertions.assertEquals(targetSetEntity.getPhysicalExertionUnitTime(), targetSetDto.getPhysicalExertionUnitTime().InputDurationToDuration());
        Assertions.assertEquals(targetSetEntity.getRestTime(), targetSetDto.getRestTime().InputDurationToDuration());
        Assertions.assertEquals(targetSetEntity.getState().label, targetSetDto.getState());
        Assertions.assertEquals(targetSetEntity.getProgExercise().getId(), targetSetDto.getProgExercise().getId());
    }

    private void assertExerciseDtoAndInputNew(InputNewTargetSet inputNewTargetSet, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(inputNewTargetSet.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(inputNewTargetSet.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(inputNewTargetSet.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(inputNewTargetSet.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(inputNewTargetSet.getWeightUnit(), targetSetDto.getWeightUnit());
        Assertions.assertEquals(inputNewTargetSet.getPhysicalExertionUnitTime().getHours(), targetSetDto.getPhysicalExertionUnitTime().getHours());
        Assertions.assertEquals(inputNewTargetSet.getRestTime().getHours(), targetSetDto.getRestTime().getHours());
        Assertions.assertEquals(inputNewTargetSet.getProgExerciseId(), targetSetDto.getProgExercise().getId());
        assertDatetimeWithTimestamp(inputNewTargetSet.getCreationDate(), targetSetDto.getCreationDate());
    }

    private void assertExerciseDtoAndInput(InputTargetSet targetSetEntity, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit(), targetSetDto.getWeightUnit());
        Assertions.assertEquals(targetSetEntity.getPhysicalExertionUnitTime().getHours(), targetSetDto.getPhysicalExertionUnitTime().getHours());
        Assertions.assertEquals(targetSetEntity.getRestTime().getHours(), targetSetDto.getRestTime().getHours());
    }
}