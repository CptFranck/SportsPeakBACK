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
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.CptFranck.SportsPeak.controller.graphqlQuery.ProgExerciseQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.*;
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

    private UserEntity user;
    private ExerciseEntity exercise;
    private ProgExerciseEntity progExercise;
    private ProgExerciseDto progExerciseDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        user = createTestUser(1L);
        exercise = createTestExercise(1L);
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

    @Test
    void ProgExerciseController_AddProgExercise_UnsuccessfulUserNotFound() {
        variables.put("inputNewProgExercise", objectMapper.convertValue(
                        createTestInputNewProgExercise(1L, 1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addProgExerciseQuery, "data.addProgExercise", variables));
    }

    @Test
    void ProgExerciseController_AddProgExercise_UnsuccessfulExerciseNotFound() {
        variables.put("inputNewProgExercise", objectMapper.convertValue(
                        createTestInputNewProgExercise(1L, 1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addProgExerciseQuery, "data.addProgExercise", variables));
    }

    @Test
    void ProgExerciseController_AddProgExercise_Success() {
        variables.put("inputNewProgExercise", objectMapper.convertValue(
                        createTestInputNewProgExercise(1L, 1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(progExerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        LinkedHashMap<String, Object> progExerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(addProgExerciseQuery, "data.addProgExercise", variables);

        Assertions.assertNotNull(progExerciseDto);
    }

    @Test
    void ProgExerciseController_ModifyProgExercise_UnsuccessfulProgExerciseNotFound() {
        variables.put("inputProgExercise", objectMapper.convertValue(
                createTestInputProgExercise(1L, 1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery, "data.modifyProgExercise", variables));
    }

    @Test
    void ProgExerciseController_ModifyProgExercise_Unsuccessful() {
        variables.put("inputProgExercise", objectMapper.convertValue(
                createTestInputProgExercise(1L, 1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery, "data.modifyProgExercise", variables));
    }

    @Test
    void ProgExerciseController_ModifyProgExercise_UnsuccessfulWrongLabel() {
        variables.put("inputProgExercise", objectMapper.convertValue(
                        createTestInputProgExercise(1L, 1L, true),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery, "data.modifyProgExercise", variables));
    }

    @Test
    void ProgExerciseController_ModifyProgExercise_Success() {
        variables.put("inputProgExercise", objectMapper.convertValue(
                createTestInputProgExercise(1L, 1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(progExerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        LinkedHashMap<String, Object> progExerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery, "data.modifyProgExercise", variables);

        Assertions.assertNotNull(progExerciseDto);
    }

    @Test
    void ProgExerciseController_ModifyProgExerciseTrustLabel_UnsuccessfulProgExerciseNotFound() {
        variables.put("inputProgExerciseTrustLabel", objectMapper.convertValue(
                createTestInputProgExerciseTrustLabel(1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseTrustLabelQuery, "data.modifyProgExerciseTrustLabel", variables));
    }

    @Test
    void ProgExerciseController_ModifyProgExerciseTrustLabel_UnsuccessfulWrongLabel() {
        variables.put("inputProgExerciseTrustLabel", objectMapper.convertValue(
                        createTestInputProgExerciseTrustLabel(1L, true),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseTrustLabelQuery, "data.modifyProgExerciseTrustLabel", variables));
    }

    @Test
    void ProgExerciseController_ModifyProgExerciseTrustLabel_Success() {
        variables.put("inputProgExerciseTrustLabel", objectMapper.convertValue(
                createTestInputProgExerciseTrustLabel(1L, false),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(progExerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        LinkedHashMap<String, Object> progExerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseTrustLabelQuery, "data.modifyProgExerciseTrustLabel", variables);

        Assertions.assertNotNull(progExerciseDto);
    }

    @Test
    void ProgExerciseController_DeleteProgExercise_UnsuccessfulNotFound() {
        variables.put("progExerciseId", 1);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteProgExerciseQuery, "data.deleteProgExercise", variables));
    }

    @Test
    void ProgExerciseController_DeleteProgExercise_UnsuccessfulProgExerciseStillUsed() {
        UserEntity userBis = createTestUser(2L);
        variables.put("progExerciseId", 1);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(userService.findUserBySubscribedProgExercises(Mockito.any(ProgExerciseEntity.class))).thenReturn(Set.of(user, userBis));

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteProgExerciseQuery, "data.deleteProgExercise", variables));
    }

    @Test
    void ProgExerciseController_DeleteProgExercise_Success() {
        variables.put("progExerciseId", 1);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(userService.findUserBySubscribedProgExercises(Mockito.any(ProgExerciseEntity.class))).thenReturn(Set.of(user));

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteProgExerciseQuery, "data.deleteProgExercise", variables);

        Assertions.assertNotNull(id);
    }

    @Test
    void ProgExerciseController_DeleteProgExercise_SuccessWithNoSubscriber() {
        variables.put("progExerciseId", 1);
        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
        when(userService.findUserBySubscribedProgExercises(Mockito.any(ProgExerciseEntity.class))).thenReturn(Set.of());

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteProgExerciseQuery, "data.deleteProgExercise", variables);

        Assertions.assertNotNull(id);
    }
}