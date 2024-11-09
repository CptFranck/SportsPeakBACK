package com.CptFranck.SportsPeak.controller.IntegrationTest.DGS;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.controller.PerformanceLogController;
import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.PerformanceLogService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.exceptions.QueryException;
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

import static com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery.PerformanceLogQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.*;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        PerformanceLogController.class
})
class PerformanceLogControllerDGSIntTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Mapper<PerformanceLogEntity, PerformanceLogDto> performanceLogMapper;

    @MockBean
    private PerformanceLogService performanceLogService;

    @MockBean
    private TargetSetService targetSetService;

    private PerformanceLogEntity performanceLog;
    private PerformanceLogDto performanceLogDto;
    private TargetSetEntity targetSet;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        targetSet = createTestTargetSet(1L, progExercise, null);
        performanceLog = createTestPerformanceLog(1L, targetSet);
        performanceLogDto = createTestPerformanceLogDto(1L);
        variables = new LinkedHashMap<>();
    }

    @Test
    void PerformanceLogController_GetPerformanceLogs_Success() {
        when(performanceLogService.findAll()).thenReturn(List.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        List<LinkedHashMap<String, Object>> PerformanceLogDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogsQuery, "data.getPerformanceLogs");

        Assertions.assertNotNull(PerformanceLogDtos);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Unsuccessful() {
        variables.put("id", 1);
        when(performanceLogService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogByIdQuery, "data.getPerformanceLogById", variables)
        );
    }

    @Test
    void PerformanceLogController_GetPerformanceLogById_Success() {
        variables.put("id", 1);
        when(performanceLogService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        LinkedHashMap<String, Object> returnedPerformanceLogDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogByIdQuery, "data.getPerformanceLogById", variables);

        Assertions.assertNotNull(returnedPerformanceLogDto);
    }

    @Test
    void PerformanceLogController_GetPerformanceLogsByTargetId_Success() {
        variables.put("targetSetId", 1);
        when(performanceLogService.findAllByTargetSetId(Mockito.any(Long.class))).thenReturn(List.of(performanceLog));
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        List<LinkedHashMap<String, Object>> PerformanceLogDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getPerformanceLogsByTargetSetsIdQuery, "data.getPerformanceLogsByTargetSetsId", variables);

        Assertions.assertNotNull(PerformanceLogDtos);
    }

    @Test
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulTargetSetNotFound() {
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                        createTestInputNewPerformanceLog(1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addPerformanceLogQuery, "data.addPerformanceLog", variables));
    }

    @Test
    void PerformanceLogController_AddPerformanceLog_UnsuccessfulWrongLabel() {
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                        createTestInputNewPerformanceLog(1L, true),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addPerformanceLogQuery, "data.addPerformanceLog", variables));
    }

    @Test
    void PerformanceLogController_AddPerformanceLog_Success() {
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                        createTestInputNewPerformanceLog(1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(performanceLogService.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        LinkedHashMap<String, Object> returnedPerformanceLogDto =
                dgsQueryExecutor.executeAndExtractJsonPath(addPerformanceLogQuery, "data.addPerformanceLog", variables);

        Assertions.assertNotNull(returnedPerformanceLogDto);
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulDoesNotExist() {
        variables.put("inputPerformanceLog", objectMapper.convertValue(
                        createTestInputPerformanceLog(1L, 1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(performanceLogService.exists(Mockito.any(Long.class))).thenReturn(false);

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables)
        );
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_UnsuccessfulWrongLabel() {
        variables.put("inputNewPerformanceLog", objectMapper.convertValue(
                        createTestInputPerformanceLog(1L, 1L, true),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(performanceLogService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables));
    }

    @Test
    void PerformanceLogController_ModifyPerformanceLog_Success() {
        variables.put("inputPerformanceLog", objectMapper.convertValue(
                        createTestInputPerformanceLog(1L, 1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(performanceLogService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(performanceLogService.save(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLog);
        when(performanceLogMapper.mapTo(Mockito.any(PerformanceLogEntity.class))).thenReturn(performanceLogDto);

        LinkedHashMap<String, Object> returnedPerformanceLogDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyPerformanceLogQuery, "data.modifyPerformanceLog", variables);

        Assertions.assertNotNull(returnedPerformanceLogDto);
    }

    @Test
    void PerformanceLogController_DeletePerformanceLog_Success() {
        variables.put("performanceLogId", 1);
        when(performanceLogService.exists(Mockito.any(Long.class))).thenReturn(true);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deletePerformanceLogQuery, "data.deletePerformanceLog", variables);

        Assertions.assertNotNull(id);
    }
}