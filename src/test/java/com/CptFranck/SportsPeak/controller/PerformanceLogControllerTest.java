package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.*;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.*;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSetDto;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        PerformanceLogController.class
})
class PerformanceLogControllerTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockBean
    private Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper;

    @MockBean
    private PerformanceLogService performanceLogService;

    @MockBean
    private TargetSetService targetSetService;

    private PerformanceLogEntity performanceLog;
    private PerformanceLogDto performanceLogDto;
    private TargetSetEntity targetSet;
    private TargetSetDto targetSetDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        UserEntity user = createTestUser(1L);
        UserDto userDto = createTestUserDto();
        ExerciseEntity exercise = createTestExercise(1L);
        ExerciseDto exerciseDto = createTestExerciseDto(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        ProgExerciseDto progExerciseDto = createTestProgExerciseDto(userDto, exerciseDto);
        targetSet = createTestTargetSet(1L, progExercise, null);
        targetSetDto = createTestTargetSetDto(progExerciseDto, null);
        performanceLog = createTestPerformanceLog(1L, targetSet);
        performanceLogDto = createTestPerformanceLogDto(1L, targetSetDto);
        variables = new LinkedHashMap<>();
    }

    @Test
    void PerformanceLogController_GetPerformanceLogs_Success() {
        when(performanceLogService.findAll()).thenReturn(List.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        @Language("GraphQL")
        String query = """
                 query {
                      getPerformanceLogs {
                          id
                          setIndex
                          repetitionNumber
                          weight
                          weightUnit
                          logDate
                          targetSet {
                              id
                          }
                      }
                  }
                """;

        List<LinkedHashMap<String, Object>> PerformanceLogDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getPerformanceLogs");

        Assertions.assertNotNull(PerformanceLogDtos);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Unsuccessful() {
        variables.put("id", 1);
        when(performanceLogService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        @Language("GraphQL")
        String query = """
                 query ($id : Int!){
                       getPerformanceLogById(id: $id) {
                           id
                           setIndex
                           repetitionNumber
                           weight
                           weightUnit
                           logDate
                           targetSet {
                               id
                           }
                       }
                   }
                """;

        LinkedHashMap<String, Object> PerformanceLogDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getPerformanceLogById", variables);

        Assertions.assertNull(PerformanceLogDto);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Success() {
        variables.put("id", 1);
        when(performanceLogService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        @Language("GraphQL")
        String query = """
                 query ($id : Int!){
                     getPerformanceLogById(id: $id) {
                         id
                         setIndex
                         repetitionNumber
                         weight
                         weightUnit
                         logDate
                         targetSet {
                             id
                         }
                     }
                 }
                """;

        LinkedHashMap<String, Object> PerformanceLogDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getPerformanceLogById", variables);

        Assertions.assertNotNull(PerformanceLogDto);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogsByTargetId_Success() {
        variables.put("targetSetId", 1);
        when(performanceLogService.findAllByTargetSetId(Mockito.any(Long.class))).thenReturn(List.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        @Language("GraphQL")
        String query = """
                 query ($targetSetId : Int!){
                       getPerformanceLogsByTargetSetsId(targetSetId: $targetSetId) {
                           id
                           setIndex
                           repetitionNumber
                           weight
                           weightUnit
                           logDate
                           targetSet {
                               id
                           }
                       }
                   }
                """;

        List<LinkedHashMap<String, Object>> PerformanceLogDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getPerformanceLogsByTargetSetsId", variables);

        Assertions.assertNotNull(PerformanceLogDtos);
    }

    @Test
    void PerformanceLogController_AddPerformanceLog_Unsuccessful() {
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                        createTestInputNewPerformanceLog(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        @Language("GraphQL")
        String query = """
                 mutation ($inputNewPerformanceLog: InputNewPerformanceLog!){
                       addPerformanceLog(inputNewPerformanceLog: $inputNewPerformanceLog) {
                           id
                           setIndex
                           repetitionNumber
                           weight
                           weightUnit
                           logDate
                           targetSet {
                               id
                           }
                       }
                   }
                """;

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(query, "data.addPerformanceLog", variables));
    }

    @Test
    void PerformanceLogController_AddPerformanceLog_Success() {
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                        createTestInputNewPerformanceLog(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(performanceLogService.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        @Language("GraphQL")
        String query = """
                 mutation ($inputNewPerformanceLog: InputNewPerformanceLog!){
                       addPerformanceLog(inputNewPerformanceLog: $inputNewPerformanceLog) {
                           id
                           setIndex
                           repetitionNumber
                           weight
                           weightUnit
                           logDate
                           targetSet {
                               id
                           }
                       }
                   }
                """;

        LinkedHashMap<String, Object> PerformanceLogDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.addPerformanceLog", variables);

        Assertions.assertNotNull(PerformanceLogDto);
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_Unsuccessful() {
        variables.put("inputPerformanceLog", objectMapper.convertValue(
                        createTestInputPerformanceLog(1L, 1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(performanceLogService.exists(Mockito.any(Long.class))).thenReturn(false);

        @Language("GraphQL")
        String query = """
                 mutation ($inputPerformanceLog: InputPerformanceLog!){
                     modifyPerformanceLog(inputPerformanceLog: $inputPerformanceLog) {
                         id
                         setIndex
                         repetitionNumber
                         weight
                         weightUnit
                         logDate
                         targetSet {
                             id
                         }
                     }
                 }
                """;

        LinkedHashMap<String, Object> PerformanceLogDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyPerformanceLog", variables);

        Assertions.assertNull(PerformanceLogDto);
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_Success() {
        variables.put("inputPerformanceLog", objectMapper.convertValue(
                        createTestInputPerformanceLog(1L, 1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(performanceLogService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(performanceLogService.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        @Language("GraphQL")
        String query = """
                 mutation ($inputPerformanceLog: InputPerformanceLog!){
                     modifyPerformanceLog(inputPerformanceLog: $inputPerformanceLog) {
                         id
                         setIndex
                         repetitionNumber
                         weight
                         weightUnit
                         logDate
                         targetSet {
                             id
                         }
                     }
                 }
                """;

        LinkedHashMap<String, Object> PerformanceLogDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyPerformanceLog", variables);

        Assertions.assertNotNull(PerformanceLogDto);
    }

    @Test
    void PerformanceLogController_DeletePerformanceLog_Unsuccessful() {
        variables.put("performanceLogId", 1);
        when(performanceLogService.exists(Mockito.any(Long.class))).thenReturn(false);

        @Language("GraphQL")
        String query = """
                 mutation ($performanceLogId : Int!){
                     deletePerformanceLog(performanceLogId: $performanceLogId)
                 }
                """;

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deletePerformanceLog", variables);

        Assertions.assertNull(id);
    }

    @Test
    void PerformanceLogController_DeletePerformanceLog_Success() {
        variables.put("performanceLogId", 1);
        when(performanceLogService.exists(Mockito.any(Long.class))).thenReturn(true);

        @Language("GraphQL")
        String query = """
                 mutation ($performanceLogId : Int!){
                     deletePerformanceLog(performanceLogId: $performanceLogId)
                 }
                """;

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deletePerformanceLog", variables);

        Assertions.assertNotNull(id);
    }
}