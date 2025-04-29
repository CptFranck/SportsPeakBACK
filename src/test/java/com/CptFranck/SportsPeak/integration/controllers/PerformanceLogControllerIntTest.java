package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.PerformanceLogController;
import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
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
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.*;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class PerformanceLogControllerIntTest {


    @Autowired
    private PerformanceLogController performanceLogController;

    @Autowired
    private PerformanceLogRepository performanceLogRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    private TargetSetEntity targetSet;
    private PerformanceLogEntity performanceLog;

    @BeforeEach
    void setUp() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        user.getProgExercisesCreated().add(progExercise);
        exercise.getProgExercises().add(progExercise);
        targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        performanceLog = performanceLogRepository.save(createTestPerformanceLog(null, targetSet));
    }

    @AfterEach
    public void afterEach() {
        this.performanceLogRepository.deleteAll();
        this.targetSetRepository.deleteAll();
        this.progExerciseRepository.deleteAll();
        this.userRepository.deleteAll();
        this.exerciseRepository.deleteAll();
    }

    @Test
    void PerformanceLogController_GetPerformanceLogs_Success() {
        List<PerformanceLogDto> performanceLogsDtos = performanceLogController.getPerformanceLogs();

        assertEqualPerformanceLogList(List.of(performanceLog), performanceLogsDtos);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_UnsuccessfulPerformanceLogNotFound() {
        Assertions.assertThrows(PerformanceLogNotFoundException.class,
                () -> performanceLogController.getPerformanceLogById(performanceLog.getId() + 1));
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Success() {
        PerformanceLogDto performanceLogDto = performanceLogController.getPerformanceLogById(performanceLog.getId());

        assertPerformanceLogDtoAndEntity(performanceLog, performanceLogDto);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogsByTargetId_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> performanceLogController.getPerformanceLogsByTargetSetsId(targetSet.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_GetPerformanceLogsByTargetId_Success() {
        List<PerformanceLogDto> performanceLogsDtos = performanceLogController.getPerformanceLogsByTargetSetsId(targetSet.getId());

        assertEqualPerformanceLogList(List.of(performanceLog), performanceLogsDtos);
    }

    @Test
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulNotAuthenticated() {
        InputNewPerformanceLog inputNewPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId(), false);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> performanceLogController.addPerformanceLog(inputNewPerformanceLog));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulTargetSetNotFound() {
        InputNewPerformanceLog inputNewPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId() + 1, false);

        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> performanceLogController.addPerformanceLog(inputNewPerformanceLog));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulWrongLabel() {
        InputNewPerformanceLog inputNewPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> performanceLogController.addPerformanceLog(inputNewPerformanceLog));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_AddPerformanceLog_Success() {
        InputNewPerformanceLog inputNewPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId(), false);

        PerformanceLogDto performanceLogDto = performanceLogController.addPerformanceLog(inputNewPerformanceLog);

        assertPerformanceLogDtoAndInput(inputNewPerformanceLog, performanceLogDto);
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulNotAuthenticated() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId(), false);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> performanceLogController.modifyPerformanceLog(inputPerformanceLog));
    }

//    @Test
//    @WithMockUser(username = "user", roles = "USER")
//    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulTargetSetNotFound() {
//        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId() + 1, false);
//
//        Assertions.assertThrows(TargetSetNotFoundException.class,
//                () -> performanceLogController.modifyPerformanceLog(inputPerformanceLog));
//    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulPerformanceLogNotFound() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId() + 1, targetSet.getId(), false);

        Assertions.assertThrows(PerformanceLogNotFoundException.class,
                () -> performanceLogController.modifyPerformanceLog(inputPerformanceLog));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulWrongLabel() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> performanceLogController.modifyPerformanceLog(inputPerformanceLog));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_ModifyPerformanceLog_Success() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId(), false);

        PerformanceLogDto performanceLogDto = performanceLogController.modifyPerformanceLog(inputPerformanceLog);

        assertPerformanceLogDtoAndInput(inputPerformanceLog, performanceLogDto);
    }

    @Test
    void PerformanceLogController_DeletePerformanceLog_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> performanceLogController.deletePerformanceLog(performanceLog.getId()));
    }

//    @Test
//    @WithMockUser(username = "user", roles = "USER")
//    void PerformanceLogController_DeletePerformanceLog_UnsuccessfulExerciseNotFound() {
//        Assertions.assertThrows(PerformanceLogNotFoundException.class,
//                () -> performanceLogController.deletePerformanceLog(performanceLog.getId() + 1));
//    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_DeletePerformanceLog_Success() {
        Long id = performanceLogController.deletePerformanceLog(performanceLog.getId());

        Assertions.assertEquals(performanceLog.getId(), id);
    }

    private void assertEqualPerformanceLogList(
            List<PerformanceLogEntity> performanceLogEntities,
            List<PerformanceLogDto> performanceLogDtos
    ) {
        Assertions.assertEquals(performanceLogEntities.size(), performanceLogDtos.size());
        performanceLogDtos.forEach(performanceLogDto -> assertPerformanceLogDtoAndEntity(
                performanceLogEntities.stream().filter(
                        performanceLogEntity -> Objects.equals(performanceLogEntity.getId(), performanceLogDto.getId())
                ).toList().getFirst(),
                performanceLogDto)
        );
    }

    private void assertPerformanceLogDtoAndEntity(PerformanceLogEntity performanceLogEntity, PerformanceLogDto performanceLogDto) {
        Assertions.assertNotNull(performanceLogDto);
        Assertions.assertEquals(performanceLogEntity.getId(), performanceLogDto.getId());
        Assertions.assertEquals(performanceLogEntity.getSetIndex(), performanceLogDto.getSetIndex());
        Assertions.assertEquals(performanceLogEntity.getRepetitionNumber(), performanceLogDto.getRepetitionNumber());
        Assertions.assertEquals(performanceLogEntity.getWeight(), performanceLogDto.getWeight());
        Assertions.assertEquals(performanceLogEntity.getWeightUnit().label, performanceLogDto.getWeightUnit());
        assertDatetimeWithTimestamp(performanceLogEntity.getLogDate(), performanceLogDto.getLogDate());
    }

    private void assertPerformanceLogDtoAndInput(InputNewPerformanceLog inputNewPerformanceLog, PerformanceLogDto performanceLogDto) {
        Assertions.assertNotNull(performanceLogDto);
        Assertions.assertEquals(inputNewPerformanceLog.getSetIndex(), performanceLogDto.getSetIndex());
        Assertions.assertEquals(inputNewPerformanceLog.getRepetitionNumber(), performanceLogDto.getRepetitionNumber());
        Assertions.assertEquals(inputNewPerformanceLog.getWeight(), performanceLogDto.getWeight());
        Assertions.assertEquals(inputNewPerformanceLog.getWeightUnit(), performanceLogDto.getWeightUnit());
        assertDatetimeWithTimestamp(inputNewPerformanceLog.getLogDate(), performanceLogDto.getLogDate());
    }
}