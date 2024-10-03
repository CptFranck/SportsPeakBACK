package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static com.CptFranck.SportsPeak.controller.graphqlQuery.ExerciseTypeQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        ExerciseTypeController.class
})
class ExerciseTypeControllerIntTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    private Mapper<ExerciseTypeEntity, ExerciseTypeDto> exerciseTypeMapper;

    @MockBean
    private ExerciseService exerciseService;

    @MockBean
    private ExerciseTypeService exerciseTypeService;

    private ExerciseTypeEntity exerciseType;
    private ExerciseTypeDto exerciseTypeDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        exerciseType = createTestExerciseType(1L);
        exerciseTypeDto = createTestExerciseTypeDto(1L);
        variables = new LinkedHashMap<>();
    }

    @Test
    void ExerciseTypeController_GetExerciseTypes_Success() {
        when(exerciseTypeService.findAll()).thenReturn(List.of(exerciseType));
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        List<LinkedHashMap<String, Object>> exerciseTypeDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getExerciseTypesQuery, "data.getExerciseTypes");

        Assertions.assertNotNull(exerciseTypeDtos);
    }

    @Test
    void ExerciseTypeController_GetExerciseTypeById_Unsuccessful() {
        variables.put("id", 1);
        when(exerciseTypeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        LinkedHashMap<String, Object> exerciseTypeDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getExerciseTypeByIdQuery, "data.getExerciseTypeById", variables);

        Assertions.assertNull(exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_GetExerciseTypeById_Success() {
        variables.put("id", 1);
        when(exerciseTypeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        LinkedHashMap<String, Object> exerciseTypeDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getExerciseTypeByIdQuery, "data.getExerciseTypeById", variables);

        Assertions.assertNotNull(exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_AddExerciseType_Success() {
        variables.put("inputNewExerciseType", objectMapper.convertValue(
                        createTestInputNewExerciseType(),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        Set<ExerciseEntity> exercises = new HashSet<>();
        when(exerciseService.findMany(Mockito.anySet())).thenReturn(exercises);
        when(exerciseTypeService.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(this.exerciseType);
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        LinkedHashMap<String, Object> exerciseTypeDto =
                dgsQueryExecutor.executeAndExtractJsonPath(addExerciseTypeQuery, "data.addExerciseType", variables);

        Assertions.assertNotNull(exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_ModifyExerciseType_Unsuccessful() {
        variables.put("inputExerciseType", objectMapper.convertValue(
                createTestInputExerciseType(),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }
                )
        );
        when(exerciseTypeService.exists(Mockito.any(Long.class))).thenReturn(false);

        LinkedHashMap<String, Object> exerciseTypeDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseTypeQuery, "data.modifyExerciseType", variables);

        Assertions.assertNull(exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_ModifyExerciseType_Success() {
        variables.put("inputExerciseType", objectMapper.convertValue(
                createTestInputExerciseType(),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }
                )
        );
        Set<ExerciseEntity> exercises = new HashSet<>();
        when(exerciseTypeService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(exerciseService.findMany(Mockito.anySet())).thenReturn(exercises);
        when(exerciseTypeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exerciseType));
        when(exerciseTypeService.save(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseType);
        when(exerciseTypeMapper.mapTo(Mockito.any(ExerciseTypeEntity.class))).thenReturn(exerciseTypeDto);

        LinkedHashMap<String, Object> exerciseTypeDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseTypeQuery, "data.modifyExerciseType", variables);

        Assertions.assertNotNull(exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_DeleteExerciseType_UnsuccessfulDoesNotExist() {
        variables.put("exerciseTypeId", 1);
        when(exerciseTypeService.exists(Mockito.any(Long.class))).thenReturn(false);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseTypeQuery, "data.deleteExerciseType", variables);

        Assertions.assertNull(id);
    }

    @Test
    void ExerciseTypeController_DeleteExercise_Success() {
        variables.put("exerciseTypeId", 1);
        when(exerciseTypeService.exists(Mockito.any(Long.class))).thenReturn(true);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseTypeQuery, "data.deleteExerciseType", variables);

        Assertions.assertNotNull(id);
    }
}