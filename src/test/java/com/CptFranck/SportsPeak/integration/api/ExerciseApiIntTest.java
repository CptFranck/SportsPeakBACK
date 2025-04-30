package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.ExerciseQuery.*;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ExerciseApiIntTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private ExerciseEntity exercise;
    private LinkedHashMap<String, Object> variables;

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
    void ExerciseApi_GetExercises_Success() {
        List<LinkedHashMap<String, Object>> response =
                dgsQueryExecutor.executeAndExtractJsonPath(getExercisesQuery, "data.getExercises");

        List<ExerciseDto> exerciseDtos = objectMapper.convertValue(response, new TypeReference<List<ExerciseDto>>() {
        });
        assertEqualExerciseList(List.of(exercise), exerciseDtos);
    }

    @Test
    void ExerciseApi_GetExerciseById_UnsuccessfulExerciseNotFound() {
        variables.put("id", exercise.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getExerciseByIdQuery, "data.getExerciseById", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exercise with the id %s has not been found", exercise.getId() + 1)));
    }

    @Test
    void ExerciseApi_GetExerciseById_Success() {
        variables.put("id", exercise.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getExerciseByIdQuery,
                "data.getExerciseById", variables);

        ExerciseDto exerciseDto = objectMapper.convertValue(response, ExerciseDto.class);
        assertExerciseDtoValid(exercise, exerciseDto);
    }

    @Test
    void ExerciseApi_AddExercise_UnsuccessfulNotAuthenticated() {
        variables.put("inputNewExercise", objectMapper.convertValue(
                createTestInputNewExercise(), new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addExerciseQuery, "data.addExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void ExerciseApi_AddExercise_Success() {
        InputNewExercise inputNewExercise = createTestInputNewExercise();
        variables.put("inputNewExercise", objectMapper.convertValue(
                inputNewExercise, new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(addExerciseQuery,
                "data.addExercise", variables);

        ExerciseDto exerciseDto = objectMapper.convertValue(response, ExerciseDto.class);
        assertExerciseDtoValidInput(inputNewExercise, exerciseDto);
    }

    @Test
    void ExerciseApi_ModifyExercise_UnsuccessfulNotAuthenticated() {
        variables.put("inputExercise", objectMapper.convertValue(
                createTestInputExercise(exercise.getId()), new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseQuery, "data.modifyExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void ExerciseApi_ModifyExercise_UnsuccessfulExerciseNotFound() {
        variables.put("inputExercise", objectMapper.convertValue(
                createTestInputExercise(exercise.getId() + 1), new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseQuery, "data.modifyExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exercise with the id %s has not been found", exercise.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void ExerciseApi_ModifyExercise_Success() {
        InputExercise inputExercise = createTestInputExercise(exercise.getId());
        variables.put("inputExercise", objectMapper.convertValue(inputExercise, new TypeReference<LinkedHashMap<String, Object>>() {
        }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseQuery,
                "data.modifyExercise", variables);

        ExerciseDto exerciseDto = objectMapper.convertValue(response, ExerciseDto.class);
        assertExerciseDtoValidInput(inputExercise, exerciseDto);
    }

    @Test
    void ExerciseApi_DeleteExercise_UnsuccessfulNotAuthenticated() {
        variables.put("exerciseId", exercise.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseQuery, "data.deleteExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void ExerciseApi_DeleteExercise_Success() {
        variables.put("exerciseId", exercise.getId());

        Integer id = dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseQuery, "data.deleteExercise", variables);

        Assertions.assertEquals(id, exercise.getId().intValue());
    }

    private void assertEqualExerciseList(
            List<ExerciseEntity> exerciseEntities,
            List<ExerciseDto> exerciseDtos
    ) {
        Assertions.assertEquals(exerciseEntities.size(), exerciseDtos.size());
        exerciseDtos.forEach(exerciseDto -> assertExerciseDtoValid(
                exerciseEntities.stream().filter(
                        exerciseEntity -> Objects.equals(exerciseEntity.getId(), exerciseDto.getId())
                ).toList().getFirst(),
                exerciseDto)
        );
    }

    private void assertExerciseDtoValid(ExerciseEntity exerciseEntity, ExerciseDto exerciseDto) {
        Assertions.assertNotNull(exerciseDto);
        Assertions.assertEquals(exerciseEntity.getName(), exerciseDto.getName());
        Assertions.assertEquals(exerciseEntity.getGoal(), exerciseDto.getGoal());
        Assertions.assertEquals(exerciseEntity.getDescription(), exerciseDto.getDescription());
        Assertions.assertEquals(exerciseEntity.getMuscles().size(), exerciseDto.getMuscles().size());
        Assertions.assertEquals(exerciseEntity.getExerciseTypes().size(), exerciseDto.getProgExercises().size());
        Assertions.assertEquals(exerciseEntity.getProgExercises().size(), exerciseDto.getProgExercises().size());
    }

    private void assertExerciseDtoValidInput(InputNewExercise inputNewExercise, ExerciseDto exerciseDto) {
        Assertions.assertNotNull(exerciseDto);
        Assertions.assertEquals(inputNewExercise.getName(), exerciseDto.getName());
        Assertions.assertEquals(inputNewExercise.getGoal(), exerciseDto.getGoal());
        Assertions.assertEquals(inputNewExercise.getDescription(), exerciseDto.getDescription());
        Assertions.assertEquals(inputNewExercise.getMuscleIds().size(), exerciseDto.getMuscles().size());
        Assertions.assertEquals(inputNewExercise.getExerciseTypeIds().size(), exerciseDto.getProgExercises().size());
        Assertions.assertEquals(inputNewExercise.getExerciseTypeIds().size(), exerciseDto.getProgExercises().size());
    }
}