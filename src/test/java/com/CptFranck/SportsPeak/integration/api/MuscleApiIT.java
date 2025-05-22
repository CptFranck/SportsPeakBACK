package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.MuscleRepository;
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

import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.MuscleQuery.*;
import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.*;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class MuscleApiIT {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private MuscleRepository muscleRepository;

    private MuscleEntity muscle;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        muscle = muscleRepository.save(createTestMuscle(null));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    public void afterEach() {
        exerciseRepository.deleteAll();
        muscleRepository.deleteAll();
    }

    @Test
    void getMuscles_ValidUse_ReturnListOfMuscleDto() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getMusclesQuery,
                "data.getMuscles");

        List<MuscleDto> muscleDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualMuscleList(List.of(muscle), muscleDtos);
    }

    @Test
    void getMuscleById_InvalidMuscleId_ThrowMuscleNotFoundException() {
        variables.put("id", muscle.getId());
        muscleRepository.delete(muscle);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getMuscleByIdQuery, "data.getMuscleById", variables));

        Assertions.assertTrue(exception.getMessage().contains("MuscleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The muscle with id %s has not been found", muscle.getId())));
    }

    @Test
    void getMuscleById_ValidMuscleId_ReturnMuscleDto() {
        variables.put("id", muscle.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getMuscleByIdQuery,
                "data.getMuscleById", variables);

        MuscleDto muscleDto = objectMapper.convertValue(response, MuscleDto.class);
        assertMuscleDtoAndEntity(muscle, muscleDto);
    }

    @Test
    void addMuscle_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("inputNewMuscle", objectMapper.convertValue(
                createTestInputNewMuscle(), new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addMuscleQuery, "data.addMuscle", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addMuscle_ValidInput_ReturnMuscleDto() {
        InputNewMuscle inputNewMuscle = createTestInputNewMuscle();
        variables.put("inputNewMuscle", objectMapper.convertValue(
                inputNewMuscle, new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(addMuscleQuery,
                "data.addMuscle", variables);

        MuscleDto muscleDto = objectMapper.convertValue(response, MuscleDto.class);
        assertMuscleDtoAndInput(inputNewMuscle, muscleDto);
    }

    @Test
    void modifyMuscle_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("inputMuscle", objectMapper.convertValue(
                createTestInputMuscle(muscle.getId()), new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyMuscleQuery, "data.modifyMuscle", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void modifyMuscle_InvalidMuscleId_ThrowMuscleNotFoundException() {
        variables.put("inputMuscle", objectMapper.convertValue(
                createTestInputMuscle(muscle.getId()), new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        muscleRepository.delete(muscle);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyMuscleQuery, "data.modifyMuscle", variables));

        Assertions.assertTrue(exception.getMessage().contains("MuscleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The muscle with id %s has not been found", muscle.getId())));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void modifyMuscle_ValidInput_ReturnMuscleDto() {
        InputMuscle inputMuscle = createTestInputMuscle(muscle.getId());
        variables.put("inputMuscle",
                objectMapper.convertValue(inputMuscle, new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyMuscleQuery,
                "data.modifyMuscle", variables);

        MuscleDto muscleDto = objectMapper.convertValue(response, MuscleDto.class);
        assertMuscleDtoAndInput(inputMuscle, muscleDto);
    }

    @Test
    void deleteMuscle_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("muscleId", muscle.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteMuscleQuery, "data.deleteMuscle", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteMuscle_InvalidMuscleId_ThrowMuscleNotFoundException() {
        variables.put("muscleId", muscle.getId());
        muscleRepository.delete(muscle);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteMuscleQuery, "data.deleteMuscle", variables));

        Assertions.assertTrue(exception.getMessage().contains("MuscleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The muscle with id %s has not been found", muscle.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteMuscle_MuscleStillUsedByExercise_ThrowMuscleStillUsedInExerciseException() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        exercise.getMuscles().add(muscle);
        exerciseRepository.save(exercise);
        variables.put("muscleId", muscle.getId());


        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteMuscleQuery, "data.deleteMuscle", variables));

        Assertions.assertTrue(exception.getMessage().contains("MuscleStillUsedInExerciseException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The muscle with id %s always used by %s exercise(s)", muscle.getId().toString(), Long.valueOf(exercise.getMuscles().size()).toString())));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteMuscle_ValidInput_ReturnMuscleId() {
        variables.put("muscleId", muscle.getId());

        String id = dgsQueryExecutor.executeAndExtractJsonPath(deleteMuscleQuery, "data.deleteMuscle", variables);

        Assertions.assertEquals(muscle.getId(), Long.valueOf(id));
    }

    private void assertEqualMuscleList(
            List<MuscleEntity> muscleEntities,
            List<MuscleDto> muscleDtos
    ) {
        Assertions.assertEquals(muscleEntities.size(), muscleDtos.size());
        muscleDtos.forEach(muscleDto -> assertMuscleDtoAndEntity(
                muscleEntities.stream().filter(
                        muscleEntity -> Objects.equals(muscleEntity.getId(), muscleDto.getId())
                ).toList().getFirst(),
                muscleDto)
        );
    }
}