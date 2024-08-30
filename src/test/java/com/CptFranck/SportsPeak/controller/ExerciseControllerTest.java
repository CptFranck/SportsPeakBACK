package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import org.intellij.lang.annotations.Language;
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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        ExerciseController.class
})
class ExerciseControllerTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    @MockBean
    private MuscleService muscleService;

    @MockBean
    private ExerciseService exerciseService;

    @MockBean
    private ExerciseTypeService exerciseTypeService;

    private ExerciseEntity exercise;

    private List<ExerciseEntity> exercises;

    private ExerciseDto exerciseDto;

    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        exercise = createTestExercise(1L);
        exercises = List.of(exercise);
        exerciseDto = createTestExerciseDto(1L);
        variables = new LinkedHashMap<>();
    }

    @Test
    void ExerciseController_GetExercises_Success() {
        when(exerciseService.findAll()).thenReturn(exercises);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        @Language("GraphQL")
        String query = """
                 query {
                    getExercises {
                        id
                        exerciseTypes {
                            id
                            name
                            goal
                        }
                        muscles {
                            id
                            name
                            function
                        }
                        progExercises {
                            id
                            name
                            note
                            trustLabel
                            visibility
                        }
                        name
                        description
                        goal
                    }
                }
                """;

        List<LinkedHashMap<String, Object>> exerciseDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getExercises");

        Assertions.assertNotNull(exerciseDtos);
    }

    @Test
    void ExerciseController_GetExerciseById_Unsuccessful() {
        variables.put("id", 1);
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        @Language("GraphQL")
        String query = """
                 query ($id : Int!){
                     getExerciseById(id: $id) {
                         id
                         exerciseTypes {
                             id
                             name
                             goal
                         }
                         muscles {
                             id
                             name
                             function
                         }
                         progExercises {
                             id
                             name
                             note
                             trustLabel
                             visibility
                         }
                         name
                         description
                         goal
                     }
                 }
                """;

        LinkedHashMap<String, Object> exerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getExerciseById", variables);

        Assertions.assertNull(exerciseDto);
    }

    @Test
    void ExerciseController_GetExerciseById_Success() {
        variables.put("id", 1);
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        @Language("GraphQL")
        String query = """
                 query ($id : Int!){
                     getExerciseById(id: $id) {
                         id
                         exerciseTypes {
                             id
                             name
                             goal
                         }
                         muscles {
                             id
                             name
                             function
                         }
                         progExercises {
                             id
                             name
                             note
                             trustLabel
                             visibility
                         }
                         name
                         description
                         goal
                     }
                 }
                """;

        LinkedHashMap<String, Object> exerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getExerciseById", variables);

        Assertions.assertNotNull(exerciseDto);
    }

    @Test
    void ExerciseController_AddExercise_Success() {
        variables.put("inputNewExercise", objectMapper.convertValue(
                createTestInputNewExercise(),
                new TypeReference<LinkedHashMap<String, Object>>() {
                })
        );
        Set<MuscleEntity> muscles = new HashSet<>();
        Set<ExerciseTypeEntity> exerciseType = new HashSet<>();
        when(muscleService.findMany(Mockito.anySet())).thenReturn(muscles);
        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(exerciseType);
        when(exerciseService.save(Mockito.any(ExerciseEntity.class))).thenReturn(exercise);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        @Language("GraphQL")
        String query = """
                 mutation ($inputNewExercise : InputNewExercise!){
                     addExercise(inputNewExercise: $inputNewExercise) {
                         id
                         name
                         goal
                         muscles {
                             id
                             name
                             function
                         }
                         exerciseTypes {
                             id
                             name
                             goal
                         }
                         progExercises {
                             id
                             name
                             note
                             trustLabel
                             visibility
                         }
                     }
                 }
                """;

        LinkedHashMap<String, Object> exerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.addExercise", variables);

        Assertions.assertNotNull(exerciseDto);
    }

    @Test
    void ExerciseController_ModifyExercise_Unsuccessful() {
        variables.put("inputExercise", objectMapper.convertValue(
                createTestInputExercise(),
                new TypeReference<LinkedHashMap<String, Object>>() {
                })
        );
        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(false);

        @Language("GraphQL")
        String query = """
                 mutation ($inputExercise : InputExercise!){
                      modifyExercise(inputExercise: $inputExercise) {
                          id
                          name
                          goal
                          muscles {
                              id
                              name
                              function
                          }
                          exerciseTypes {
                              id
                              name
                              goal
                          }
                          progExercises {
                              id
                              name
                              note
                              trustLabel
                              visibility
                          }
                      }
                  }
                """;

        LinkedHashMap<String, Object> exerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyExercise", variables);

        Assertions.assertNull(exerciseDto);
    }

    @Test
    void ExerciseController_ModifyExercise_Success() {
        variables.put("inputExercise", objectMapper.convertValue(
                createTestInputExercise(),
                new TypeReference<LinkedHashMap<String, Object>>() {
                })
        );
        Set<MuscleEntity> muscles = new HashSet<>();
        Set<ExerciseTypeEntity> exerciseType = new HashSet<>();
        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(muscleService.findMany(Mockito.anySet())).thenReturn(muscles);
        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(exerciseType);
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(exerciseService.save(Mockito.any(ExerciseEntity.class))).thenReturn(exercise);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        @Language("GraphQL")
        String query = """
                 mutation ($inputExercise : InputExercise!){
                      modifyExercise(inputExercise: $inputExercise) {
                          id
                          name
                          goal
                          muscles {
                              id
                              name
                              function
                          }
                          exerciseTypes {
                              id
                              name
                              goal
                          }
                          progExercises {
                              id
                              name
                              note
                              trustLabel
                              visibility
                          }
                      }
                  }
                """;

        LinkedHashMap<String, Object> exerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyExercise", variables);

        Assertions.assertNotNull(exerciseDto);
    }

    @Test
    void ExerciseController_DeleteExercise_Success() {
        variables.put("exerciseId", 1);
        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(true);

        @Language("GraphQL")
        String query = """
                 mutation ($exerciseId : Int!){
                       deleteExercise(exerciseId: $exerciseId)
                   }
                """;

        Integer exerciseDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deleteExercise", variables);

        Assertions.assertNotNull(exerciseDto);
    }
}