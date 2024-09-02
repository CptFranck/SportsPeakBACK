package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.MuscleService;
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

import java.util.LinkedHashMap;
import java.util.List;

import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscleDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        MuscleController.class
})
class MuscleControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;
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

        @Language("GraphQL")
        String query = """
                 query {
                     getMuscles {
                         id
                         exercises {
                             id
                             name
                             goal
                         }
                         name
                         function
                     }
                 }
                """;

        List<LinkedHashMap<String, Object>> exerciseDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getMuscles");

        Assertions.assertNotNull(exerciseDtos);
    }

//    @Test
//    void MuscleController_GetExerciseById_Unsuccessful() {
//        variables.put("id", 1);
//        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());
//
//        @Language("GraphQL")
//        String query = """
//                 query ($id : Int!){
//                     getExerciseById(id: $id) {
//                         id
//                         exerciseTypes {
//                             id
//                             name
//                             goal
//                         }
//                         muscles {
//                             id
//                             name
//                             function
//                         }
//                         progExercises {
//                             id
//                             name
//                             note
//                             trustLabel
//                             visibility
//                         }
//                         name
//                         description
//                         goal
//                     }
//                 }
//                """;
//
//        LinkedHashMap<String, Object> exerciseDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getExerciseById", variables);
//
//        Assertions.assertNull(exerciseDto);
//    }
//
//    @Test
//    void MuscleController_GetExerciseById_Success() {
//        variables.put("id", 1);
//        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));
//        when(muscleMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(muscleDto);
//
//        @Language("GraphQL")
//        String query = """
//                 query ($id : Int!){
//                     getExerciseById(id: $id) {
//                         id
//                         exerciseTypes {
//                             id
//                             name
//                             goal
//                         }
//                         muscles {
//                             id
//                             name
//                             function
//                         }
//                         progExercises {
//                             id
//                             name
//                             note
//                             trustLabel
//                             visibility
//                         }
//                         name
//                         description
//                         goal
//                     }
//                 }
//                """;
//
//        LinkedHashMap<String, Object> exerciseDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getExerciseById", variables);
//
//        Assertions.assertNotNull(exerciseDto);
//    }
//
//    @Test
//    void MuscleController_AddExercise_Success() {
//        variables.put("inputNewExercise", objectMapper.convertValue(
//                        createTestInputNewExercise(),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        Set<MuscleEntity> muscles = new HashSet<>();
//        Set<ExerciseTypeEntity> exerciseType = new HashSet<>();
//        when(muscleService.findMany(Mockito.anySet())).thenReturn(muscles);
//        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(exerciseType);
//        when(exerciseService.save(Mockito.any(ExerciseEntity.class))).thenReturn(muscle);
//        when(muscleMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(muscleDto);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($inputNewExercise : InputNewExercise!){
//                     addExercise(inputNewExercise: $inputNewExercise) {
//                         id
//                         name
//                         goal
//                         muscles {
//                             id
//                             name
//                             function
//                         }
//                         exerciseTypes {
//                             id
//                             name
//                             goal
//                         }
//                         progExercises {
//                             id
//                             name
//                             note
//                             trustLabel
//                             visibility
//                         }
//                     }
//                 }
//                """;
//
//        LinkedHashMap<String, Object> exerciseDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.addExercise", variables);
//
//        Assertions.assertNotNull(exerciseDto);
//    }
//
//    @Test
//    void MuscleController_ModifyExercise_Unsuccessful() {
//        variables.put("inputExercise", objectMapper.convertValue(
//                        createTestInputExercise(),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(false);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($inputExercise : InputExercise!){
//                      modifyExercise(inputExercise: $inputExercise) {
//                          id
//                          name
//                          goal
//                          muscles {
//                              id
//                              name
//                              function
//                          }
//                          exerciseTypes {
//                              id
//                              name
//                              goal
//                          }
//                          progExercises {
//                              id
//                              name
//                              note
//                              trustLabel
//                              visibility
//                          }
//                      }
//                  }
//                """;
//
//        LinkedHashMap<String, Object> exerciseDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyExercise", variables);
//
//        Assertions.assertNull(exerciseDto);
//    }
//
//    @Test
//    void MuscleController_ModifyExercise_Success() {
//        variables.put("inputExercise", objectMapper.convertValue(
//                        createTestInputExercise(),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        Set<MuscleEntity> muscles = new HashSet<>();
//        Set<ExerciseTypeEntity> exerciseType = new HashSet<>();
//        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(true);
//        when(muscleService.findMany(Mockito.anySet())).thenReturn(muscles);
//        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(exerciseType);
//        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(muscle));
//        when(exerciseService.save(Mockito.any(ExerciseEntity.class))).thenReturn(muscle);
//        when(muscleMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(muscleDto);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($inputExercise : InputExercise!){
//                      modifyExercise(inputExercise: $inputExercise) {
//                          id
//                          name
//                          goal
//                          muscles {
//                              id
//                              name
//                              function
//                          }
//                          exerciseTypes {
//                              id
//                              name
//                              goal
//                          }
//                          progExercises {
//                              id
//                              name
//                              note
//                              trustLabel
//                              visibility
//                          }
//                      }
//                  }
//                """;
//
//        LinkedHashMap<String, Object> exerciseDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyExercise", variables);
//
//        Assertions.assertNotNull(exerciseDto);
//    }
//
//    @Test
//    void MuscleController_DeleteExercise_Unsuccessful() {
//        variables.put("exerciseId", 1);
//        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(false);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($exerciseId : Int!){
//                       deleteExercise(exerciseId: $exerciseId)
//                   }
//                """;
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deleteExercise", variables);
//
//        Assertions.assertNull(id);
//    }
//
//    @Test
//    void MuscleController_DeleteExercise_Success() {
//        variables.put("exerciseId", 1);
//        when(exerciseService.exists(Mockito.any(Long.class))).thenReturn(true);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($exerciseId : Int!){
//                       deleteExercise(exerciseId: $exerciseId)
//                   }
//                """;
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deleteExercise", variables);
//
//        Assertions.assertNotNull(id);
//    }
}