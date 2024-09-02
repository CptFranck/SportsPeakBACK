package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseTypeDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        ExerciseTypeController.class
})
class ExerciseTestControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
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

        @Language("GraphQL")
        String query = """
                 query {
                     getExerciseTypes {
                         id
                         exercises {
                             id
                             name
                             goal
                         }
                         name
                         goal
                     }
                 }
                """;

        List<LinkedHashMap<String, Object>> exerciseTypeDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getExerciseTypes");

        Assertions.assertNotNull(exerciseTypeDtos);
    }


}