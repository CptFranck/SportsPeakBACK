package com.CptFranck.SportsPeak.controller.IntegrationTest;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.controller.TargetSetController;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
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

import static com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery.TargetSetQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.*;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        TargetSetController.class
})
class TargetSetControllerIntTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Mapper<TargetSetEntity, TargetSetDto> TargetSetMapper;

    @MockBean
    private TargetSetService targetSetService;

    @MockBean
    private ProgExerciseService progExerciseService;

    private TargetSetEntity targetSet;
    private TargetSetDto targetSetDto;
    private ProgExerciseEntity progExercise;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        UserEntity user = createTestUser(1L);
        UserDto userDto = createTestUserDto(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ExerciseDto exerciseDto = createTestExerciseDto(1L);
        progExercise = createTestProgExercise(1L, user, exercise);
        ProgExerciseDto progExerciseDto = createTestProgExerciseDto(1L, userDto, exerciseDto);
        targetSet = createTestTargetSet(1L, progExercise, null);
        targetSetDto = createTestTargetSetDto(1L, progExerciseDto, null);
        variables = new LinkedHashMap<>();
    }

    @Test
    void TargetSetController_GetTargetSets_Success() {
        when(targetSetService.findAll()).thenReturn(List.of(targetSet));
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        List<LinkedHashMap<String, Object>> TargetSetDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetsQuery, "data.getTargetSets");

        Assertions.assertNotNull(TargetSetDtos);
    }

    @Test
    void TargetSetController_GetTargetSetById_UnsuccessfulDoesNotExist() {
        variables.put("id", 1);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetByIdQuery, "data.getTargetSetById", variables)
        );
    }

    @Test
    void TargetSetController_GetTargetSetById_Success() {
        variables.put("id", 1);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        LinkedHashMap<String, Object> TargetSetDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetByIdQuery, "data.getTargetSetById", variables);

        Assertions.assertNotNull(TargetSetDto);
    }

    @Test
    void TargetSetController_GetTargetSetsByTargetId_Success() {
        variables.put("progExerciseId", 1);
        when(targetSetService.findAllByProgExerciseId(Mockito.any(Long.class))).thenReturn(List.of(targetSet));
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        List<LinkedHashMap<String, Object>> TargetSetDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetsByProgExerciseIdQuery, "data.getTargetSetsByProgExerciseId", variables);

        Assertions.assertNotNull(TargetSetDtos);
    }

    @Test
    void TargetSetController_AddTargetSet_UnsuccessfulProgExerciseNotFound() {
        variables.put("inputNewTargetSet", objectMapper.convertValue(
                        createTestInputNewTargetSet(1L, null),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addTargetSetQuery, "data.addTargetSet", variables));
    }

    @Test
    void TargetSetController_AddTargetSet_Success() {
        variables.put("inputNewTargetSet", objectMapper.convertValue(
                        createTestInputNewTargetSet(1L, null),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        LinkedHashMap<String, Object> TargetSetDto =
                dgsQueryExecutor.executeAndExtractJsonPath(addTargetSetQuery, "data.addTargetSet", variables);

        Assertions.assertNotNull(TargetSetDto);
    }

    @Test
    void TargetSetController_AddTargetSetWithUpdate_UnsuccessfulTargetSetNotFound() {
        variables.put("inputNewTargetSet", objectMapper.convertValue(
                createTestInputNewTargetSet(1L, 1L),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addTargetSetQuery, "data.addTargetSet", variables));
    }

    @Test
    void TargetSetController_AddTargetSetWithUpdate_Success() {
        variables.put("inputNewTargetSet", objectMapper.convertValue(
                        createTestInputNewTargetSet(1L, 1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        LinkedHashMap<String, Object> TargetSetDto =
                dgsQueryExecutor.executeAndExtractJsonPath(addTargetSetQuery, "data.addTargetSet", variables);

        Assertions.assertNotNull(TargetSetDto);
    }

    @Test
    void TargetSetController_ModifyTargetSet_UnsuccessfulTargetSetNotFound() {
        variables.put("inputTargetSet", objectMapper.convertValue(
                        createTestInputTargetSet(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetQuery, "data.modifyTargetSet", variables));
    }

    @Test
    void TargetSetController_ModifyTargetSet_Success() {
        variables.put("inputTargetSet", objectMapper.convertValue(
                        createTestInputTargetSet(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        LinkedHashMap<String, Object> TargetSetDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetQuery, "data.modifyTargetSet", variables);

        Assertions.assertNotNull(TargetSetDto);
    }

    @Test
    void TargetSetController_ModifyTargetSetState_UnsuccessfulNotFound() {
        variables.put("inputTargetSetState", objectMapper.convertValue(
                createTestInputInputTargetSetState(1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetStateQuery, "data.modifyTargetSetState", variables));
    }

    @Test
    void TargetSetController_ModifyTargetSetState_UnsuccessfulWrongLabel() {
        variables.put("inputTargetSetState", objectMapper.convertValue(
                        createTestInputInputTargetSetState(1L, true),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetStateQuery, "data.modifyTargetSetState", variables));
    }

    @Test
    void TargetSetController_ModifyTargetSetState_Success() {
        variables.put("inputTargetSetState", objectMapper.convertValue(
                createTestInputInputTargetSetState(1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(targetSet);
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        List<LinkedHashMap<String, Object>> TargetSetDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetStateQuery, "data.modifyTargetSetState", variables);

        Assertions.assertNotNull(TargetSetDto);
    }

    @Test
    void TargetSetController_DeleteTargetSet_Success() {
        variables.put("targetSetId", 1);
        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(true);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteTargetSetQuery, "data.deleteTargetSet", variables);

        Assertions.assertNotNull(id);
    }
}