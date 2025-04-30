package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.muscle.InputMuscle;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
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

import static com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery.MuscleQuery.*;
import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class MuscleApiIntTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

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
        muscleRepository.deleteAll();
    }

    @Test
    void MuscleApi_GetMuscles_Success() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getMusclesQuery,
                "data.getMuscles");

        List<MuscleDto> muscleDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualMuscleList(List.of(muscle), muscleDtos);
    }

    @Test
    void MuscleApi_GetMuscleById_UnsuccessfulMuscleNotFound() {
        variables.put("id", muscle.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getMuscleByIdQuery, "data.getMuscleById", variables));

        Assertions.assertTrue(exception.getMessage().contains("MuscleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The muscle with id %s has not been found", muscle.getId() + 1)));
    }

    @Test
    void MuscleApi_GetMuscleById_Success() {
        variables.put("id", muscle.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getMuscleByIdQuery,
                "data.getMuscleById", variables);

        MuscleDto muscleDto = objectMapper.convertValue(response, MuscleDto.class);
        assertMuscleDtoAndEntity(muscle, muscleDto);
    }

    @Test
    void MuscleApi_AddMuscle_UnsuccessfulNotAuthenticated() {
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
    void MuscleApi_AddMuscle_Success() {
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
    void MuscleApi_ModifyMuscle_UnsuccessfulNotAuthenticated() {
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
    void MuscleApi_ModifyMuscle_UnsuccessfulMuscleNotFound() {
        variables.put("inputMuscle", objectMapper.convertValue(
                createTestInputMuscle(muscle.getId() + 1), new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyMuscleQuery, "data.modifyMuscle", variables));

        Assertions.assertTrue(exception.getMessage().contains("MuscleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The muscle with id %s has not been found", muscle.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void MuscleApi_ModifyMuscle_Success() {
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
    void MuscleApi_DeleteMuscle_UnsuccessfulNotAuthenticated() {
        variables.put("muscleId", muscle.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteMuscleQuery, "data.deleteMuscle", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN")
//    void MuscleApi_DeleteMuscle_UnsuccessfulMuscleNotFound() {
//        variables.put("muscleId", muscle.getId() + 1);
//
//        QueryException exception = Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteMuscleQuery, "data.deleteMuscle", variables));
//
//        Assertions.assertTrue(exception.getMessage().contains("MuscleNotFoundException"));
//        Assertions.assertTrue(exception.getMessage().contains(String.format("The muscle with id %s has not been found", muscle.getId() + 1)));
//    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void MuscleApi_DeleteMuscle_Success() {
        variables.put("muscleId", muscle.getId());

        Integer id = dgsQueryExecutor.executeAndExtractJsonPath(deleteMuscleQuery, "data.deleteMuscle", variables);

        Assertions.assertEquals(id, muscle.getId().intValue());
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

    private void assertMuscleDtoAndEntity(MuscleEntity muscleEntity, MuscleDto muscleDto) {
        Assertions.assertNotNull(muscleDto);
        Assertions.assertEquals(muscleEntity.getId(), muscleDto.getId());
        Assertions.assertEquals(muscleEntity.getName(), muscleDto.getName());
        Assertions.assertEquals(muscleEntity.getLatinName(), muscleDto.getLatinName());
        Assertions.assertEquals(muscleEntity.getFunction(), muscleDto.getFunction());
        Assertions.assertEquals(muscleEntity.getDescription(), muscleDto.getDescription());
        Assertions.assertEquals(muscleEntity.getExercises().size(), muscleDto.getExercises().size());
    }

    private void assertMuscleDtoAndInput(InputNewMuscle inputNewMuscle, MuscleDto muscleDto) {
        Assertions.assertNotNull(muscleDto);
        Assertions.assertEquals(inputNewMuscle.getName(), muscleDto.getName());
        Assertions.assertEquals(inputNewMuscle.getLatinName(), muscleDto.getLatinName());
        Assertions.assertEquals(inputNewMuscle.getFunction(), muscleDto.getFunction());
        Assertions.assertEquals(inputNewMuscle.getDescription(), muscleDto.getDescription());
        Assertions.assertEquals(inputNewMuscle.getExerciseIds().size(), muscleDto.getExercises().size());
    }
}