package com.CptFranck.SportsPeak.controller.IntegrationTest.DGS;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
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

    private LinkedHashMap<String, Object> variables;

    private ExerciseEntity exercise;

    @BeforeEach
    void init() {
        exercise = exerciseRepository.save(createTestExercise(null));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    public void afterEach() {
        exerciseRepository.deleteAll();
    }

    @Test
    void ExerciseController_GetExercises_Success() {
        List<LinkedHashMap<String, Object>> exerciseDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getExercisesQuery, "data.getExercises");

        Assertions.assertNotNull(exerciseDtos);
    }

    @Test
    void ExerciseController_GetExerciseById_Unsuccessful() {
        variables.put("id", exercise.getId() + 1);

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getExerciseByIdQuery, "data.getExerciseById", variables)
        );
    }

    @Test
    void ExerciseController_GetExerciseById_Success() {
        variables.put("id", exercise.getId());

        LinkedHashMap<String, Object> exerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getExerciseByIdQuery, "data.getExerciseById", variables);

        Assertions.assertNotNull(exerciseDto);
    }

    @Test
    void ExerciseController_AddExercise_UnsuccessfulNotAuthorized() {
        variables.put("inputNewExercise", objectMapper.convertValue(
                        createTestInputNewExercise(),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addExerciseQuery, "data.addExercise", variables)
        );
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