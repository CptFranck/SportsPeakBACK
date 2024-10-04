package com.CptFranck.SportsPeak.controller.IntegrationTest;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.controller.MuscleController;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.MuscleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery.MuscleQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        MuscleController.class
})
class MuscleControllerIntTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Mapper<MuscleEntity, MuscleDto> muscleMapper;

    @Autowired
    private MuscleService muscleService;

    @Autowired
    private ExerciseService exerciseService;

    private MuscleEntity muscle;
    private MuscleDto muscleDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        muscle = createTestMuscle(1L);
        muscleDto = createTestMuscleDto(1L);
        variables = new LinkedHashMap<>();
    }

    @Test
    void MuscleController_GetMuscles_Success() {
        when(muscleService.findAll()).thenReturn(List.of(muscle));
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

        List<LinkedHashMap<String, Object>> muscleDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getMusclesQuery, "data.getMuscles");

        Assertions.assertNotNull(muscleDtos);
    }

    @Test
    void MuscleController_GetMuscleById_Unsuccessful() {
        variables.put("id", 1);
        when(muscleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getMuscleByIdQuery, "data.getMuscleById", variables)
        );

    }

    @Test
    void MuscleController_GetMuscleById_Success() {
        variables.put("id", 1);
        when(muscleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

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
        when(exerciseService.findMany(Mockito.anySet())).thenReturn(exercises);
        when(muscleService.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

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
        when(exerciseService.findMany(Mockito.anySet())).thenReturn(exercises);
        when(muscleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));
        when(muscleService.save(Mockito.any(MuscleEntity.class))).thenReturn(muscle);
        when(muscleMapper.mapTo(Mockito.any(MuscleEntity.class))).thenReturn(muscleDto);

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
}