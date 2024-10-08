package com.CptFranck.SportsPeak.controller.IntegrationTest.DGS;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.LinkedHashMap;
import java.util.List;

import static com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery.ExerciseQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ExerciseControllerDGSIntTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private MuscleRepository muscleRepository;

    @Autowired
    private ExerciseTypeService exerciseTypeService;

    private ExerciseEntity exercise;
    private ExerciseDto exerciseDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        exercise = createTestExercise(1L);
        exerciseDto = createTestExerciseDto(1L);
        variables = new LinkedHashMap<>();
    }

    @Test
    void ExerciseController_GetExercises_Success() {
        List<LinkedHashMap<String, Object>> exerciseDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getExercisesQuery, "data.getExercises");

        Assertions.assertNotNull(exerciseDtos);
    }

    @Test
    void ExerciseController_GetExerciseById_Unsuccessful() {
        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getExerciseByIdQuery, "data.getExerciseById", variables)
        );
    }

    @Test
    void ExerciseController_GetExerciseById_Success() {
        LinkedHashMap<String, Object> exerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getExerciseByIdQuery, "data.getExerciseById", variables);

        Assertions.assertNotNull(exerciseDto);
    }

    @Test
    void ExerciseController_AddExercise_Success() {
        variables.put("inputNewExercise", objectMapper.convertValue(
                        createTestInputNewExercise(),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        LinkedHashMap<String, Object> exerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(addExerciseQuery, "data.addExercise", variables);

        Assertions.assertNotNull(exerciseDto);
    }

    @Test
    void ExerciseController_ModifyExercise_UnsuccessfulDoesNotExist() {
        variables.put("inputExercise", objectMapper.convertValue(
                        createTestInputExercise(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseQuery, "data.modifyExercise", variables)
        );
    }

    @Test
    void ExerciseController_ModifyExercise_UnsuccessfulExerciseNotFound() {
        variables.put("inputExercise", objectMapper.convertValue(
                        createTestInputExercise(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseQuery, "data.modifyExercise", variables)
        );
    }

    @Test
    void ExerciseController_ModifyExercise_Success() {
        variables.put("inputExercise", objectMapper.convertValue(
                        createTestInputExercise(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        LinkedHashMap<String, Object> exerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseQuery, "data.modifyExercise", variables);

        Assertions.assertNotNull(exerciseDto);
    }

    @Test
    void ExerciseController_DeleteExercise_Success() {
        variables.put("exerciseId", 1);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseQuery, "data.deleteExercise", variables);

        Assertions.assertNotNull(id);
    }
}