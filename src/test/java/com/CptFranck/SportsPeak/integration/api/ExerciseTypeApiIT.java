package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
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

import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.ExerciseTypeQuery.*;
import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.*;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ExerciseTypeApiIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    private ExerciseTypeEntity exerciseType;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    void afterEach() {
        exerciseRepository.deleteAll();
        exerciseTypeRepository.deleteAll();
    }

    @Test
    void getExerciseTypes_ValidUse_ReturnListOfExerciseTypeDto() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getExerciseTypesQuery,
                "data.getExerciseTypes");

        List<ExerciseTypeDto> exerciseTypeDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualExerciseList(List.of(exerciseType), exerciseTypeDtos);
    }

    @Test
    void getExerciseTypeById_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        variables.put("id", exerciseType.getId());
        exerciseTypeRepository.delete(exerciseType);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getExerciseTypeByIdQuery, "data.getExerciseTypeById", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseTypeNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exerciseType with the id %s has not been found", exerciseType.getId())));
    }

    @Test
    void getExerciseTypeById_ValidInput_ReturnExerciseTypeDto() {
        variables.put("id", exerciseType.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getExerciseTypeByIdQuery,
                "data.getExerciseTypeById", variables);

        ExerciseTypeDto exerciseTypeDto = objectMapper.convertValue(response, ExerciseTypeDto.class);
        assertExerciseTypeDtoValid(exerciseType, exerciseTypeDto);
    }

    @Test
    void addExerciseType_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        InputNewExerciseType inputNewExerciseType = createTestInputNewExerciseType();
        variables.put("inputNewExerciseType", objectMapper.convertValue(inputNewExerciseType,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addExerciseTypeQuery, "data.addExerciseType", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addExerciseType_ValidInput_ReturnExerciseTypeDto() {
        InputNewExerciseType inputNewExerciseType = createTestInputNewExerciseType();
        variables.put("inputNewExerciseType", objectMapper.convertValue(
                inputNewExerciseType, new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(addExerciseTypeQuery,
                "data.addExerciseType", variables);

        ExerciseTypeDto exerciseTypeDto = objectMapper.convertValue(response, ExerciseTypeDto.class);
        assertExerciseTypeDtoValidInput(inputNewExerciseType, exerciseTypeDto);
    }

    @Test
    void modifyExerciseType_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("inputExerciseType", objectMapper.convertValue(
                createTestInputExerciseType(exerciseType.getId()), new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseTypeQuery, "data.modifyExerciseType", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void modifyExerciseType_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        variables.put("inputExerciseType", objectMapper.convertValue(
                createTestInputExerciseType(exerciseType.getId()), new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        exerciseTypeRepository.delete(exerciseType);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseTypeQuery, "data.modifyExerciseType", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseTypeNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exerciseType with the id %s has not been found", exerciseType.getId())));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void modifyExerciseType_ValidInput_ReturnExerciseTypeDto() {
        InputNewExerciseType inputNewExerciseType = createTestInputExerciseType(exerciseType.getId());
        variables.put("inputExerciseType", objectMapper.convertValue(
                inputNewExerciseType, new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseTypeQuery,
                "data.modifyExerciseType", variables);

        ExerciseTypeDto exerciseTypeDto = objectMapper.convertValue(response, ExerciseTypeDto.class);
        assertExerciseTypeDtoValidInput(inputNewExerciseType, exerciseTypeDto);
    }

    @Test
    void deleteExerciseType_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("exerciseTypeId", exerciseType.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseTypeQuery, "data.deleteExerciseType", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteExerciseType_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        variables.put("exerciseTypeId", exerciseType.getId());
        exerciseTypeRepository.delete(exerciseType);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseTypeQuery, "data.deleteExerciseType", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseTypeNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exerciseType with the id %s has not been found", exerciseType.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteExerciseType_ExerciseTypeStillUsedByExercise_ThrowExerciseTypeStillUsedInExerciseException() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        exercise.getExerciseTypes().add(exerciseType);
        exerciseRepository.save(exercise);
        variables.put("exerciseTypeId", exerciseType.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseTypeQuery, "data.deleteExerciseType", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseTypeStillUsedInExerciseException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exercise type with id %s always used by %s exercise(s)", exerciseType.getId().toString(), Long.valueOf(exercise.getExerciseTypes().size()).toString())));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteExerciseType_ValidInput_Void() {
        variables.put("exerciseTypeId", exerciseType.getId());

        String id = dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseTypeQuery, "data.deleteExerciseType", variables);

        Assertions.assertEquals(exerciseType.getId(), Long.valueOf(id));
    }

    private void assertEqualExerciseList(List<ExerciseTypeEntity> exerciseTypeEntities, List<ExerciseTypeDto> exerciseTypeDtos) {
        Assertions.assertEquals(exerciseTypeEntities.size(), exerciseTypeDtos.size());
        exerciseTypeDtos.forEach(exerciseTypeDto -> assertExerciseTypeDtoValid(
                exerciseTypeEntities.stream().filter(
                        exerciseTypeEntity -> Objects.equals(exerciseTypeEntity.getId(), exerciseTypeDto.getId())
                ).toList().getFirst(),
                exerciseTypeDto)
        );
    }

    private void assertExerciseTypeDtoValid(ExerciseTypeEntity exerciseTypeEntity, ExerciseTypeDto exerciseTypeDto) {
        Assertions.assertNotNull(exerciseTypeDto);
        Assertions.assertEquals(exerciseTypeEntity.getName(), exerciseTypeDto.getName());
        Assertions.assertEquals(exerciseTypeEntity.getGoal(), exerciseTypeDto.getGoal());
        Assertions.assertEquals(exerciseTypeEntity.getExercises().size(), exerciseTypeDto.getExercises().size());
    }

    private void assertExerciseTypeDtoValidInput(InputNewExerciseType inputNewExercise, ExerciseTypeDto exerciseDto) {
        Assertions.assertNotNull(exerciseDto);
        Assertions.assertEquals(inputNewExercise.getName(), exerciseDto.getName());
        Assertions.assertEquals(inputNewExercise.getGoal(), exerciseDto.getGoal());
        Assertions.assertEquals(inputNewExercise.getExerciseIds().size(), exerciseDto.getExercises().size());
    }
}