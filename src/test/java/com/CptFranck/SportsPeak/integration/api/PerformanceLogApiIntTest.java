package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import com.CptFranck.SportsPeak.repositories.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery.PerformanceLogQuery.*;
import static com.CptFranck.SportsPeak.utils.TestDateTimeUtils.assertDatetimeWithTimestamp;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.*;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class PerformanceLogApiIntTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private PerformanceLogRepository performanceLogRepository;

    private TargetSetEntity targetSet;
    private PerformanceLogEntity performanceLog;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        performanceLog = performanceLogRepository.save(createTestPerformanceLog(null, targetSet));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    void afterEach() {
        performanceLogRepository.deleteAll();
        targetSetRepository.deleteAll();
        progExerciseRepository.deleteAll();
        userRepository.deleteAll();
        exerciseRepository.deleteAll();
    }

    @Test
    void PerformanceLogController_GetPerformanceLogs_Success() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogsQuery,
                "data.getPerformanceLogs");

        List<PerformanceLogDto> performanceLogDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualPerformanceLogList(List.of(performanceLog), performanceLogDtos);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Unsuccessful() {
        variables.put("id", performanceLog.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogByIdQuery, "data.getPerformanceLogById", variables));

        Assertions.assertTrue(exception.getMessage().contains("PerformanceLogNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The performanceLog with the id %s has not been found", performanceLog.getId() + 1)));
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Success() {
        variables.put("id", performanceLog.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogByIdQuery,
                "data.getPerformanceLogById", variables);

        PerformanceLogDto performanceLogDto = objectMapper.convertValue(response, PerformanceLogDto.class);
        assertPerformanceLogDtoAndEntity(performanceLog, performanceLogDto);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogsByTargetId_UnsuccessfulNotAuthenticated() {
        variables.put("targetSetId", performanceLog.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogsByTargetSetsIdQuery, "data.getPerformanceLogsByTargetSetsId", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_GetPerformanceLogsByTargetId_Success() {
        variables.put("targetSetId", performanceLog.getId());

        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogsByTargetSetsIdQuery,
                "data.getPerformanceLogsByTargetSetsId", variables);

        List<PerformanceLogDto> performanceLogDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualPerformanceLogList(List.of(performanceLog), performanceLogDtos);
    }

    @Test
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulNotAuthenticated() {
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                createTestInputNewPerformanceLog(targetSet.getId() + 1, false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addPerformanceLogQuery, "data.addPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulTargetSetNotFound() {
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                createTestInputNewPerformanceLog(targetSet.getId() + 1, false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addPerformanceLogQuery, "data.addPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("TargetSetNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The targetSet with the id %s has not been found", targetSet.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulWrongLabel() {
        InputNewPerformanceLog inputNewPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId(), true);
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                inputNewPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addPerformanceLogQuery, "data.addPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("LabelMatchNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("No %s (label) match with %s", "WeightUnit", inputNewPerformanceLog.getWeightUnit())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_AddPerformanceLog_Success() {
        InputNewPerformanceLog inputNewPerformanceLog = createTestInputNewPerformanceLog(targetSet.getId(), false);
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                inputNewPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(addPerformanceLogQuery,
                "data.addPerformanceLog", variables);

        PerformanceLogDto performanceLogDto = objectMapper.convertValue(response, PerformanceLogDto.class);
        assertPerformanceLogDtoAndInput(inputNewPerformanceLog, performanceLogDto);
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulNotAuthenticated() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId(), false);
        variables.put("inputPerformanceLog", objectMapper.convertValue(inputPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {
        }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

//    @Test
//    @WithMockUser(username = "user", roles = "USER")
//    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulTargetSetNotFound() {
//        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId() + 1, false);
//        variables.put("inputPerformanceLog", objectMapper.convertValue(inputPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {}));
//
//        QueryException exception = Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables));
//
//        Assertions.assertTrue(exception.getMessage().contains("TargetSetNotFoundException"));
//        Assertions.assertTrue(exception.getMessage().contains(String.format("The targetSet with the id %s has not been found", targetSet.getId() + 1)));
//    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulPerformanceLogNotFound() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId() + 1, targetSet.getId(), false);
        variables.put("inputPerformanceLog", objectMapper.convertValue(inputPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {
        }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("PerformanceLogNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The performanceLog with the id %s has not been found", performanceLog.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulWrongLabel() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId(), true);
        variables.put("inputPerformanceLog", objectMapper.convertValue(inputPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {
        }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("LabelMatchNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("No %s (label) match with %s", "WeightUnit", inputPerformanceLog.getWeightUnit())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_ModifyPerformanceLog_Success() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId(), false);
        variables.put("inputPerformanceLog", objectMapper.convertValue(inputPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {
        }));

        LinkedHashMap<String, Object> response =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables);

        PerformanceLogDto performanceLogDto = objectMapper.convertValue(response, PerformanceLogDto.class);
        assertPerformanceLogDtoAndInput(inputPerformanceLog, performanceLogDto);
    }


    @Test
    void PerformanceLogController_DeletePerformanceLog_Success() {
        variables.put("performanceLogId", performanceLog.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deletePerformanceLogQuery, "data.deletePerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }


    @Test
    @WithMockUser(username = "user", roles = "USER")
    void PerformanceLogController_DeletePerformanceLog_UnsuccessfulNotAuthenticated() {
        variables.put("performanceLogId", performanceLog.getId());

        Integer id = dgsQueryExecutor.executeAndExtractJsonPath(deletePerformanceLogQuery, "data.deletePerformanceLog", variables);

        Assertions.assertEquals(performanceLog.getId().intValue(), id);
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