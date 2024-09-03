package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.UserService;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.controller.graphqlQuery.ProgExerciseQuery.getProgExerciseByIdQuery;
import static com.CptFranck.SportsPeak.controller.graphqlQuery.ProgExerciseQuery.getProgExercisesQuery;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        ProgExerciseController.class
})
class ProgExerciseControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;
    @MockBean
    private Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ExerciseService exerciseService;

    @MockBean
    private ProgExerciseService progExerciseService;

    private ProgExerciseEntity progExercise;
    private ProgExerciseDto progExerciseDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        UserDto userDto = createTestUserDto(1L);
        ExerciseDto exerciseDto = createTestExerciseDto(1L);
        progExercise = createTestProgExercise(1L, user, exercise);
        progExerciseDto = createTestProgExerciseDto(1L, userDto, exerciseDto);
        variables = new LinkedHashMap<>();
    }

    @Test
    void ProgExerciseController_GetProgExercises_Success() {
        when(progExerciseService.findAll()).thenReturn(List.of(progExercise));
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        List<LinkedHashMap<String, Object>> progExerciseDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getProgExercisesQuery, "data.getProgExercises");

        Assertions.assertNotNull(progExerciseDtos);
    }

    @Test
    void ProgExerciseController_GetProgExerciseById_Unsuccessful() {
        variables.put("id", 1);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        LinkedHashMap<String, Object> progExerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getProgExerciseByIdQuery, "data.getProgExerciseById", variables);

        Assertions.assertNull(progExerciseDto);
    }

    @Test
    void ProgExerciseController_GetProgExerciseById_Success() {
        variables.put("id", 1);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        LinkedHashMap<String, Object> progExerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getProgExerciseByIdQuery, "data.getProgExerciseById", variables);

        Assertions.assertNotNull(progExerciseDto);
    }
//
//    @Test
//    void ProgExerciseController_AddProgExercise_Success() {
//        variables.put("inputNewProgExercise", objectMapper.convertValue(
//                createTestInputNewProgExercise(),
//                new TypeReference<LinkedHashMap<String, Object>>() {
//                }
//                )
//        );
//        Set<MuscleEntity> muscles = new HashSet<>();
//        Set<ProgExerciseTypeEntity> exerciseType = new HashSet<>();
//        when(progExerciseService.findMany(Mockito.anySet())).thenReturn(muscles);
//        when(userService.findMany(Mockito.anySet())).thenReturn(exerciseType);
//        when(exerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
//        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);
//
//        LinkedHashMap<String, Object> progExerciseDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(addProgExerciseQuery, "data.addProgExercise", variables);
//
//        Assertions.assertNotNull(progExerciseDto);
//    }
//
//    @Test
//    void ProgExerciseController_ModifyProgExercise_UnsuccessfulDoesNotExist() {
//        variables.put("inputProgExercise", objectMapper.convertValue(
//                        createTestInputProgExercise(),
//                new TypeReference<LinkedHashMap<String, Object>>() {
//                }
//                )
//        );
//        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(false);
//
//        LinkedHashMap<String, Object> progExerciseDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery, "data.modifyProgExercise", variables);
//
//        Assertions.assertNull(progExerciseDto);
//    }
//
//    @Test
//    void ProgExerciseController_ModifyProgExercise_UnsuccessfulProgExerciseNotFound() {
//        variables.put("inputProgExercise", objectMapper.convertValue(
//                        createTestInputProgExercise(),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        Set<MuscleEntity> muscles = new HashSet<>();
//        Set<ProgExerciseTypeEntity> exerciseType = new HashSet<>();
//        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(true);
//        when(progExerciseService.findMany(Mockito.anySet())).thenReturn(muscles);
//        when(userService.findMany(Mockito.anySet())).thenReturn(exerciseType);
//        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery, "data.modifyProgExercise", variables));
//    }
//
//    @Test
//    void ProgExerciseController_ModifyProgExercise_Success() {
//        variables.put("inputProgExercise", objectMapper.convertValue(
//                createTestInputProgExercise(),
//                new TypeReference<LinkedHashMap<String, Object>>() {
//                }
//                )
//        );
//        Set<MuscleEntity> muscles = new HashSet<>();
//        Set<ProgExerciseTypeEntity> exerciseType = new HashSet<>();
//        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(true);
//        when(progExerciseService.findMany(Mockito.anySet())).thenReturn(muscles);
//        when(userService.findMany(Mockito.anySet())).thenReturn(exerciseType);
//        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
//        when(exerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
//        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);
//
//        LinkedHashMap<String, Object> progExerciseDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery, "data.modifyProgExercise", variables);
//
//        Assertions.assertNotNull(progExerciseDto);
//    }
//
//    @Test
//    void ProgExerciseController_DeleteProgExercise_Unsuccessful() {
//        variables.put("exerciseId", 1);
//        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(false);
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(deleteProgExerciseQuery, "data.deleteProgExercise", variables);
//
//        Assertions.assertNull(id);
//    }
//
//    @Test
//    void ProgExerciseController_DeleteProgExercise_Success() {
//        variables.put("exerciseId", 1);
//        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(true);
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(deleteProgExerciseQuery, "data.deleteProgExercise", variables);
//
//        Assertions.assertNotNull(id);
//    }
}