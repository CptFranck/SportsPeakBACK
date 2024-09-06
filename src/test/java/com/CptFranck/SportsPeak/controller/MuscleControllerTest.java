package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static com.CptFranck.SportsPeak.controller.graphqlQuery.MuscleQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        MuscleController.class
})
class MuscleControllerTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private Mapper<MuscleEntity, MuscleDto> muscleMapper;

    @MockBean
    private MuscleService muscleService;

    @MockBean
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

        LinkedHashMap<String, Object> muscleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getMuscleByIdQuery, "data.getMuscleById", variables);

        Assertions.assertNull(muscleDto);
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
        when(muscleService.exists(Mockito.any(Long.class))).thenReturn(false);

        LinkedHashMap<String, Object> muscleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyMuscleQuery, "data.modifyMuscle", variables);

        Assertions.assertNull(muscleDto);
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
        when(muscleService.exists(Mockito.any(Long.class))).thenReturn(true);
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
        when(muscleService.exists(Mockito.any(Long.class))).thenReturn(true);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteMuscleQuery, "data.deleteMuscle", variables);

        Assertions.assertNotNull(id);
    }
}