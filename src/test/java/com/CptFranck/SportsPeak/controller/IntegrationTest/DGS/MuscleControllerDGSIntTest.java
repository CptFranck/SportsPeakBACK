package com.CptFranck.SportsPeak.controller.IntegrationTest.DGS;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.controller.MuscleController;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.muscle.InputNewMuscle;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery.MuscleQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        MuscleController.class
})
class MuscleControllerDGSIntTest {

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
    void MuscleController_GetMuscles_Success() {
        List<LinkedHashMap<String, Object>> response =
                dgsQueryExecutor.executeAndExtractJsonPath(getMusclesQuery, "data.getMuscles");

        List<MuscleDto> muscleDtos = objectMapper.convertValue(response, new TypeReference<List<MuscleDto>>() {
        });
        muscleDtos.forEach(muscleDto -> assertMuscleDtoValid(muscle, muscleDto));
    }

    @Test
    void MuscleController_GetMuscleById_UnsuccessfulMuscleNotFound() {
        variables.put("id", muscle.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getMuscleByIdQuery, "data.getMuscleById", variables)
        );

        Assertions.assertTrue(exception.getMessage().contains("ExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exercise with the id %s has not been found", muscle.getId() + 1)));
    }

    @Test
    void MuscleController_GetMuscleById_Success() {
        variables.put("id", 1);

        LinkedHashMap<String, Object> muscleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getMuscleByIdQuery, "data.getMuscleById", variables);

        Assertions.assertNotNull(muscleDto);
    }

    @Test
    void MuscleController_AddMuscle_Success() {
        variables.put("inputNewMuscle", objectMapper.convertValue(
                        createTestInputNewMuscle(),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        Set<ExerciseEntity> exercises = new HashSet<>();

        LinkedHashMap<String, Object> muscleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(addMuscleQuery, "data.addMuscle", variables);

        Assertions.assertNotNull(muscleDto);
    }

    @Test
    void MuscleController_ModifyMuscle_UnsuccessfulDoesNotExist() {
        variables.put("inputMuscle", objectMapper.convertValue(
                        createTestInputMuscle(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyMuscleQuery, "data.modifyMuscle", variables)
        );
    }

    @Test
    void MuscleController_ModifyMuscle_Success() {
        variables.put("inputMuscle", objectMapper.convertValue(
                        createTestInputMuscle(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        Set<ExerciseEntity> exercises = new HashSet<>();

        LinkedHashMap<String, Object> muscleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyMuscleQuery, "data.modifyMuscle", variables);

        Assertions.assertNotNull(muscleDto);
    }

    @Test
    void MuscleController_DeleteMuscle_Success() {
        variables.put("muscleId", 1);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteMuscleQuery, "data.deleteMuscle", variables);

        Assertions.assertNotNull(id);
    }

    private void assertMuscleDtoValid(MuscleEntity muscleEntity, MuscleDto muscleDto) {
        Assertions.assertNotNull(muscleDto);
        Assertions.assertEquals(muscleEntity.getName(), muscleDto.getName());
        Assertions.assertEquals(muscleEntity.getFunction(), muscleDto.getFunction());
        Assertions.assertEquals(muscleEntity.getDescription(), muscleDto.getDescription());
        Assertions.assertEquals(muscleEntity.getExercises().size(), muscleDto.getExercises().size());
    }

    private void assertMuscleDtoValidInput(InputNewMuscle inputNewMuscle, MuscleDto muscleDto) {
        Assertions.assertNotNull(muscleDto);
        Assertions.assertEquals(inputNewMuscle.getName(), muscleDto.getName());
        Assertions.assertEquals(inputNewMuscle.getFunction(), muscleDto.getFunction());
        Assertions.assertEquals(inputNewMuscle.getDescription(), muscleDto.getDescription());
        Assertions.assertEquals(inputNewMuscle.getExerciseIds().size(), muscleDto.getExercises().size());
    }
}