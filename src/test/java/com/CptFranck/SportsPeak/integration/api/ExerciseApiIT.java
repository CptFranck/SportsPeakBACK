package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.input.exercise.InputExercise;
import com.CptFranck.SportsPeak.domain.input.exercise.InputNewExercise;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
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
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ExerciseApiIT {

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
    void getExercises_ValidUse_ReturnListOfExerciseDto() {
        List<LinkedHashMap<String, Object>> response =
                dgsQueryExecutor.executeAndExtractJsonPath(getExercisesQuery, "data.getExercises");

        List<ExerciseDto> exerciseDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualExerciseList(List.of(exercise), exerciseDtos);
    }

    @Test
    void getExerciseById_InvalidExerciseId_ThrowQueryExceptionExerciseNotFound() {
        variables.put("id", exercise.getId());
        exerciseRepository.delete(exercise);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getExerciseByIdQuery, "data.getExerciseById", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exercise with the id %s has not been found", exercise.getId())));
    }

    @Test
    void getExerciseById_ValidInput_ReturnExerciseDto() {
        variables.put("id", exercise.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getExerciseByIdQuery,
                "data.getExerciseById", variables);

        ExerciseDto exerciseDto = objectMapper.convertValue(response, ExerciseDto.class);
        assertExerciseDtoAndEntity(exercise, exerciseDto);
    }

    @Test
    void addExercise_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
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
    void addExercise_ValidInput_ReturnExerciseDto() {
        InputNewExercise inputNewExercise = createTestInputNewExercise();
        variables.put("inputNewExercise", objectMapper.convertValue(
                inputNewExercise, new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(addExerciseQuery,
                "data.addExercise", variables);

        ExerciseDto exerciseDto = objectMapper.convertValue(response, ExerciseDto.class);
        assertExerciseDtoAndInput(inputNewExercise, exerciseDto);
    }

    @Test
    void modifyExercise_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
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
    void modifyExercise_InvalidExerciseId_ThrowQueryExceptionExerciseNotFound() {
        variables.put("inputExercise", objectMapper.convertValue(
                createTestInputExercise(exercise.getId()), new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        exerciseRepository.delete(exercise);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseQuery, "data.modifyExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exercise with the id %s has not been found", exercise.getId())));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void modifyExercise_ValidInput_ReturnExerciseDto() {
        InputExercise inputExercise = createTestInputExercise(exercise.getId());
        variables.put("inputExercise", objectMapper.convertValue(inputExercise, new TypeReference<LinkedHashMap<String, Object>>() {
        }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseQuery,
                "data.modifyExercise", variables);

        ExerciseDto exerciseDto = objectMapper.convertValue(response, ExerciseDto.class);
        assertExerciseDtoAndInput(inputExercise, exerciseDto);
    }

    @Test
    void deleteExercise_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("exerciseId", exercise.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseQuery, "data.deleteExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteExercise_InvalidExerciseId_ThrowQueryExceptionExerciseNotFound() {
        variables.put("exerciseId", exercise.getId());
        exerciseRepository.delete(exercise);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseQuery, "data.deleteExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exercise with the id %s has not been found", exercise.getId())));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteExercise_ValidInput_ReturnExerciseId() {
        variables.put("exerciseId", exercise.getId());

        String id = dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseQuery, "data.deleteExercise", variables);

        Assertions.assertEquals(exercise.getId(), Long.valueOf(id));
    }

    private void assertEqualExerciseList(
            List<ExerciseEntity> exerciseEntities,
            List<ExerciseDto> exerciseDtos
    ) {
        Assertions.assertEquals(exerciseEntities.size(), exerciseDtos.size());
        exerciseDtos.forEach(exerciseDto -> assertExerciseDtoAndEntity(
                exerciseEntities.stream().filter(
                        exerciseEntity -> Objects.equals(exerciseEntity.getId(), exerciseDto.getId())
                ).toList().getFirst(),
                exerciseDto)
        );
    }
}