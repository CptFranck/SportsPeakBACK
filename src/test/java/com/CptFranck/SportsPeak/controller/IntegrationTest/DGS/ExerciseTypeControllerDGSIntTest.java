package com.CptFranck.SportsPeak.controller.IntegrationTest.DGS;

import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.input.exerciseType.InputNewExerciseType;
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

import static com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery.ExerciseTypeQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ExerciseTypeControllerDGSIntTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    private ExerciseTypeEntity exerciseType;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        exerciseType = exerciseTypeRepository.save(createTestExerciseType(1L));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    void afterEach() {
        exerciseTypeRepository.deleteAll();
    }

    @Test
    void ExerciseTypeController_GetExerciseTypes_Success() {
        List<LinkedHashMap<String, Object>> response =
                dgsQueryExecutor.executeAndExtractJsonPath(getExerciseTypesQuery, "data.getExerciseTypes");

        List<ExerciseTypeDto> exerciseTypeDtos = objectMapper.convertValue(response, new TypeReference<List<ExerciseTypeDto>>() {
        });
        exerciseTypeDtos.forEach(exerciseTypeDto -> assertExerciseTypeDtoValid(exerciseType, exerciseTypeDto));
    }

    @Test
    void ExerciseTypeController_GetExerciseTypeById_Unsuccessful() {
        variables.put("id", exerciseType.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getExerciseTypeByIdQuery, "data.getExerciseTypeById", variables)
        );

        Assertions.assertTrue(exception.getMessage().contains("ExerciseTypeNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exerciseType with the id %s has not been found", exerciseType.getId() + 1)));
    }

    @Test
    void ExerciseTypeController_GetExerciseTypeById_Success() {
        variables.put("id", exerciseType.getId());

        LinkedHashMap<String, Object> response =
                dgsQueryExecutor.executeAndExtractJsonPath(getExerciseTypeByIdQuery, "data.getExerciseTypeById", variables);

        ExerciseTypeDto exerciseTypeDto = objectMapper.convertValue(response, ExerciseTypeDto.class);
        assertExerciseTypeDtoValid(exerciseType, exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_AddExerciseType_UnsuccessfulNotAuthenticated() {
        InputNewExerciseType inputNewExerciseType = createTestInputNewExerciseType();
        variables.put("inputNewExerciseType", objectMapper.convertValue(
                        inputNewExerciseType,
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addExerciseTypeQuery, "data.addExerciseType", variables)
        );

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void ExerciseTypeController_AddExerciseType_Success() {
        InputNewExerciseType inputNewExerciseType = createTestInputNewExerciseType();
        variables.put("inputNewExerciseType", objectMapper.convertValue(
                inputNewExerciseType,
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        LinkedHashMap<String, Object> response =
                dgsQueryExecutor.executeAndExtractJsonPath(addExerciseTypeQuery, "data.addExerciseType", variables);

        ExerciseTypeDto exerciseTypeDto = objectMapper.convertValue(response, ExerciseTypeDto.class);
        assertExerciseTypeDtoValidInput(inputNewExerciseType, exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_ModifyExerciseType_UnsuccessfulNotAuthenticated() {
        variables.put("inputExerciseType", objectMapper.convertValue(
                createTestInputExerciseType(exerciseType.getId()),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseTypeQuery, "data.modifyExerciseType", variables)
        );

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void ExerciseTypeController_ModifyExerciseType_UnsuccessfulExerciseNotFound() {
        variables.put("inputExerciseType", objectMapper.convertValue(
                        createTestInputExerciseType(exerciseType.getId() + 1),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseTypeQuery, "data.modifyExerciseType", variables)
        );

        Assertions.assertTrue(exception.getMessage().contains("ExerciseTypeNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exerciseType with the id %s has not been found", exerciseType.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void ExerciseTypeController_ModifyExerciseType_Success() {
        InputNewExerciseType inputNewExerciseType = createTestInputExerciseType(exerciseType.getId());
        variables.put("inputExerciseType", objectMapper.convertValue(
                inputNewExerciseType,
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        LinkedHashMap<String, Object> response =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyExerciseTypeQuery, "data.modifyExerciseType", variables);

        ExerciseTypeDto exerciseTypeDto = objectMapper.convertValue(response, ExerciseTypeDto.class);
        assertExerciseTypeDtoValidInput(inputNewExerciseType, exerciseTypeDto);
    }

    @Test
    void ExerciseTypeController_DeleteExercise_UnsuccessfulNotAuthenticated() {
        variables.put("exerciseTypeId", exerciseType.getId());


        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseTypeQuery, "data.deleteExerciseType", variables)
        );

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void ExerciseTypeController_DeleteExercise_Success() {
        variables.put("exerciseTypeId", 1);

        Integer id = dgsQueryExecutor.executeAndExtractJsonPath(deleteExerciseTypeQuery, "data.deleteExerciseType", variables);

        Assertions.assertEquals(id, exerciseType.getId().intValue());
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