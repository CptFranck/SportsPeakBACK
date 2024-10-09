package com.CptFranck.SportsPeak.controller.IntegrationTest;

import com.CptFranck.SportsPeak.controller.PerformanceLogController;
import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.LabelMatchNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.performanceLog.PerformanceLogNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import com.CptFranck.SportsPeak.mappers.impl.PerformanceLogMapperImpl;
import com.CptFranck.SportsPeak.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.*;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class PerformanceLogControllerIntTest {


    private final PerformanceLogMapperImpl performanceLogMapper;
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

    public PerformanceLogControllerIntTest() {
        this.performanceLogMapper = new PerformanceLogMapperImpl(new ModelMapper());
    }

    @BeforeEach
    void setUp() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(1L, user, exercise));
        user.getProgExercisesCreated().add(progExercise);
        exercise.getProgExercises().add(progExercise);
        exerciseRepository.save(exercise);
        userRepository.save(user);
        targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
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
        PerformanceLogEntity performanceLogEntity =
                performanceLogRepository.save(createTestPerformanceLog(null, targetSet));

        List<PerformanceLogDto> performanceLogsDtos = performanceLogController.getPerformanceLogs();

        assertEqualExerciseList(List.of(performanceLogEntity), performanceLogsDtos);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Unsuccessful() {
        Assertions.assertThrows(PerformanceLogNotFoundException.class,
                () -> performanceLogController.getPerformanceLogById(1L)
        );
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Success() {
        PerformanceLogEntity performanceLogEntity =
                performanceLogRepository.save(createTestPerformanceLog(null, targetSet));

        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, null);
        PerformanceLogEntity performanceLog = createTestPerformanceLog(1L, targetSet);
        performanceLogMapper.mapTo(performanceLog);
        performanceLogMapper.mapTo(performanceLogEntity);

        PerformanceLogDto performanceLogDto =
                performanceLogController.getPerformanceLogById(performanceLogEntity.getId());

        assertExerciseDtoAndEntity(performanceLogEntity, performanceLogDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PerformanceLogController_GetPerformanceLogsByTargetId_Success() {
        PerformanceLogEntity performanceLogEntity =
                performanceLogRepository.save(createTestPerformanceLog(null, targetSet));

        List<PerformanceLogDto> performanceLogsDtos =
                performanceLogController.getPerformanceLogsByTargetSetsId(targetSet.getId());

        assertEqualExerciseList(List.of(performanceLogEntity), performanceLogsDtos);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulTargetSetNotFound() {
        InputNewPerformanceLog inputNewPerformanceLog =
                createTestInputNewPerformanceLog(1L, false);

        Assertions.assertThrows(TargetSetNotFoundException.class,
                () -> performanceLogController.addPerformanceLog(inputNewPerformanceLog)
        );

    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulWrongLabel() {
        InputNewPerformanceLog inputNewPerformanceLog =
                createTestInputNewPerformanceLog(targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> performanceLogController.addPerformanceLog(inputNewPerformanceLog)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PerformanceLogController_AddPerformanceLog_Success() {
        InputNewPerformanceLog inputNewPerformanceLog =
                createTestInputNewPerformanceLog(targetSet.getId(), false);

        PerformanceLogDto performanceLogDto = performanceLogController.addPerformanceLog(inputNewPerformanceLog);

        assertExerciseDtoAndInput(inputNewPerformanceLog, performanceLogDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulDoesNotExist() {
        InputPerformanceLog inputPerformanceLog =
                createTestInputPerformanceLog(1L, targetSet.getId(), false);

        Assertions.assertThrows(PerformanceLogNotFoundException.class,
                () -> performanceLogController.modifyPerformanceLog(inputPerformanceLog)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulWrongLabel() {
        PerformanceLogEntity performanceLogEntity =
                performanceLogRepository.save(createTestPerformanceLog(null, targetSet));
        InputPerformanceLog inputPerformanceLog =
                createTestInputPerformanceLog(performanceLogEntity.getId(), targetSet.getId(), true);

        Assertions.assertThrows(LabelMatchNotFoundException.class,
                () -> performanceLogController.modifyPerformanceLog(inputPerformanceLog)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PerformanceLogController_ModifyPerformanceLog_Success() {
        PerformanceLogEntity performanceLogEntity =
                performanceLogRepository.save(createTestPerformanceLog(null, targetSet));
        InputPerformanceLog inputPerformanceLog =
                createTestInputPerformanceLog(performanceLogEntity.getId(), targetSet.getId(), false);

        PerformanceLogDto performanceLogDto = performanceLogController.modifyPerformanceLog(inputPerformanceLog);

        assertExerciseDtoAndInput(inputPerformanceLog, performanceLogDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PerformanceLogController_DeletePerformanceLog_UnsuccessfulExerciseNotFound() {
        Assertions.assertThrows(PerformanceLogNotFoundException.class,
                () -> performanceLogController.deletePerformanceLog(1L)
        );
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PerformanceLogController_DeletePerformanceLog_Success() {
        PerformanceLogEntity performanceLogEntity =
                performanceLogRepository.save(createTestPerformanceLog(null, targetSet));

        Long id = performanceLogController.deletePerformanceLog(performanceLogEntity.getId());

        Assertions.assertEquals(performanceLogEntity.getId(), id);
    }

    private void assertEqualExerciseList(
            List<PerformanceLogEntity> performanceLogEntities,
            List<PerformanceLogDto> performanceLogDtos
    ) {
        performanceLogDtos.forEach(performanceLogDto -> assertExerciseDtoAndEntity(
                performanceLogEntities.stream().filter(
                        performanceLogEntity -> Objects.equals(performanceLogEntity.getId(), performanceLogDto.getId())
                ).toList().getFirst(),
                performanceLogDto)
        );
    }

    private void assertExerciseDtoAndEntity(PerformanceLogEntity performanceLogEntity, PerformanceLogDto performanceLogDto) {
        Assertions.assertNotNull(performanceLogDto);
        Assertions.assertEquals(performanceLogEntity.getId(), performanceLogDto.getId());
        Assertions.assertEquals(performanceLogEntity.getSetIndex(), performanceLogDto.getSetIndex());
        Assertions.assertEquals(performanceLogEntity.getRepetitionNumber(), performanceLogDto.getRepetitionNumber());
        Assertions.assertEquals(performanceLogEntity.getWeight(), performanceLogDto.getWeight());
        Assertions.assertEquals(performanceLogEntity.getWeightUnit().label, performanceLogDto.getWeightUnit());
        Assertions.assertEquals(performanceLogEntity.getLogDate(), performanceLogDto.getLogDate());
        Assertions.assertEquals(performanceLogEntity.getTargetSet().getId(), performanceLogDto.getTargetSet().getId());
    }

    private void assertExerciseDtoAndInput(InputNewPerformanceLog inputNewPerformanceLog, PerformanceLogDto performanceLogDto) {
        Assertions.assertNotNull(performanceLogDto);
        Assertions.assertEquals(inputNewPerformanceLog.getSetIndex(), performanceLogDto.getSetIndex());
        Assertions.assertEquals(inputNewPerformanceLog.getRepetitionNumber(), performanceLogDto.getRepetitionNumber());
        Assertions.assertEquals(inputNewPerformanceLog.getWeight(), performanceLogDto.getWeight());
        Assertions.assertEquals(inputNewPerformanceLog.getWeightUnit(), performanceLogDto.getWeightUnit());
        Assertions.assertEquals(inputNewPerformanceLog.getLogDate(), performanceLogDto.getLogDate());
        Assertions.assertEquals(inputNewPerformanceLog.getTargetSetId(), performanceLogDto.getTargetSet().getId());
    }
}