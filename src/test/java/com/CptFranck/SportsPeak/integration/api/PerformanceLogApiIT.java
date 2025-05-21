package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputNewPerformanceLog;
import com.CptFranck.SportsPeak.domain.input.performanceLog.InputPerformanceLog;
import com.CptFranck.SportsPeak.repository.*;
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

import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.PerformanceLogQuery.*;
import static com.CptFranck.SportsPeak.utils.TestDateTimeUtils.assertDatetimeWithTimestamp;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.*;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class PerformanceLogApiIT {

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
    void getPerformanceLogs_ValidUse_ReturnListOfPerformanceLogDto() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogsQuery,
                "data.getPerformanceLogs");

        List<PerformanceLogDto> performanceLogDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualPerformanceLogList(List.of(performanceLog), performanceLogDtos);
    }

    @Test
    void getPerformanceLogById_InvalidPerformanceLogId_ThrowPerformanceLogNotFoundException() {
        variables.put("id", performanceLog.getId());
        performanceLogRepository.delete(performanceLog);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogByIdQuery, "data.getPerformanceLogById", variables));

        Assertions.assertTrue(exception.getMessage().contains("PerformanceLogNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The performanceLog with the id %s has not been found", performanceLog.getId())));
    }

    @Test
    void getPerformanceLogById_ValidPerformanceLogId_ReturnPerformanceLogDto() {
        variables.put("id", performanceLog.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogByIdQuery,
                "data.getPerformanceLogById", variables);

        PerformanceLogDto performanceLogDto = objectMapper.convertValue(response, PerformanceLogDto.class);
        assertPerformanceLogDtoAndEntity(performanceLog, performanceLogDto);
    }

    @Test
    void getPerformanceLogsByTargetSetsId_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("targetSetId", performanceLog.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogsByTargetSetsIdQuery, "data.getPerformanceLogsByTargetSetsId", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getPerformanceLogsByTargetSetsId_ValidUse_ReturnListOfPerformanceLogDto() {
        variables.put("targetSetId", targetSet.getId());

        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogsByTargetSetsIdQuery,
                "data.getPerformanceLogsByTargetSetsId", variables);

        List<PerformanceLogDto> performanceLogDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualPerformanceLogList(List.of(performanceLog), performanceLogDtos);
    }

    @Test
    void addPerformanceLog_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                createTestInputNewPerformanceLog(targetSet.getId(), false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        performanceLogRepository.delete(performanceLog);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addPerformanceLogQuery, "data.addPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addPerformanceLog_InvalidLabel_ThrowLabelMatchNotFoundException() {
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
    void addPerformanceLog_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                createTestInputNewPerformanceLog(targetSet.getId(), false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        performanceLogRepository.delete(performanceLog);
        targetSetRepository.delete(targetSet);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addPerformanceLogQuery, "data.addPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("TargetSetNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The targetSet with the id %s has not been found", targetSet.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addPerformanceLog_ValidInput_ReturnPerformanceLogDto() {
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
    void modifyPerformanceLog_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId(), false);
        variables.put("inputPerformanceLog", objectMapper.convertValue(inputPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {
        }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyPerformanceLog_InvalidLabel_ThrowLabelMatchNotFoundException() {
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
    void modifyPerformanceLog_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId(), false);
        variables.put("inputPerformanceLog", objectMapper.convertValue(inputPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {
        }));
        performanceLogRepository.delete(performanceLog);
        targetSetRepository.delete(targetSet);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("TargetSetNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The targetSet with the id %s has not been found", targetSet.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyPerformanceLog_InvalidPerformanceLogId_ThrowPerformanceLogNotFoundException() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId(), false);
        variables.put("inputPerformanceLog", objectMapper.convertValue(inputPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {
        }));
        performanceLogRepository.delete(performanceLog);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("PerformanceLogNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The performanceLog with the id %s has not been found", performanceLog.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyPerformanceLog_ValidInput_ReturnPerformanceLogDto() {
        InputPerformanceLog inputPerformanceLog = createTestInputPerformanceLog(performanceLog.getId(), targetSet.getId(), false);
        variables.put("inputPerformanceLog", objectMapper.convertValue(inputPerformanceLog, new TypeReference<LinkedHashMap<String, Object>>() {
        }));

        LinkedHashMap<String, Object> response =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables);

        PerformanceLogDto performanceLogDto = objectMapper.convertValue(response, PerformanceLogDto.class);
        assertPerformanceLogDtoAndInput(inputPerformanceLog, performanceLogDto);
    }


    @Test
    void deletePerformanceLog_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("performanceLogId", performanceLog.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deletePerformanceLogQuery, "data.deletePerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deletePerformanceLog_InvalidPerformanceLogIf_ThrowPerformanceLogNotFoundException() {
        variables.put("performanceLogId", performanceLog.getId());
        performanceLogRepository.delete(performanceLog);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deletePerformanceLogQuery, "data.deletePerformanceLog", variables));

        Assertions.assertTrue(exception.getMessage().contains("PerformanceLogNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The performanceLog with the id %s has not been found", performanceLog.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deletePerformanceLog_ValidInput_ReturnPerformanceLogId() {
        variables.put("performanceLogId", performanceLog.getId());

        String id = dgsQueryExecutor.executeAndExtractJsonPath(deletePerformanceLogQuery, "data.deletePerformanceLog", variables);

        Assertions.assertEquals(performanceLog.getId(), Long.valueOf(id));
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