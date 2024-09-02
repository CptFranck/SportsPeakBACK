package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleService;
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

import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createTestPrivilege;
import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createTestPrivilegeDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        PrivilegeController.class
})
class PrivilegeControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;
    @MockBean
    private Mapper<PrivilegeEntity, PrivilegeDto> privilegeMapper;

    @MockBean
    private PrivilegeService privilegeService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private ExerciseTypeService exerciseTypeService;

    private PrivilegeEntity privilegeEntity;
    private PrivilegeDto privilegeDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        privilegeEntity = createTestPrivilege(1L);
        privilegeDto = createTestPrivilegeDto(1L);
        variables = new LinkedHashMap<>();
    }

    @Test
    void PrivilegeController_GetPrivileges_Success() {
        when(privilegeService.findAll()).thenReturn(List.of(privilegeEntity));
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        @Language("GraphQL")
        String query = """
                 query {
                     getPrivileges {
                         id
                         name
                         roles {
                             id
                             name
                             privileges {
                                 id
                                 name
                             }
                         }
                     }
                 }
                """;

        List<LinkedHashMap<String, Object>> exerciseDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getPrivileges");

        Assertions.assertNotNull(exerciseDtos);
    }

//    @Test
//    void PrivilegeController_GetExerciseById_Unsuccessful() {
//        variables.put("id", 1);
//        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());
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
//    void PrivilegeController_GetExerciseById_Success() {
//        variables.put("id", 1);
//        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(privilegeEntity));
//        when(privilegeMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(privilegeDto);
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

//    @Test
//    void PrivilegeController_AddExercise_Success() {
//        variables.put("inputNewExercise", objectMapper.convertValue(
//                        createTestInputNewExercise(),
//                new TypeReference<LinkedHashMap<String, Object>>() {
//                }
//                )
//        );
//        Set<MuscleEntity> muscles = new HashSet<>();
//        Set<ExerciseTypeEntity> exerciseType = new HashSet<>();
//        when(privilegeService.findMany(Mockito.anySet())).thenReturn(muscles);
//        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(exerciseType);
//        when(roleService.save(Mockito.any(ExerciseEntity.class))).thenReturn(privilegeEntity);
//        when(privilegeMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(privilegeDto);
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
//    void PrivilegeController_ModifyExercise_Unsuccessful() {
//        variables.put("inputExercise", objectMapper.convertValue(
//                        createTestInputExercise(),
//                new TypeReference<LinkedHashMap<String, Object>>() {
//                }
//                )
//        );
//        when(roleService.exists(Mockito.any(Long.class))).thenReturn(false);
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
//    void PrivilegeController_ModifyExercise_Success() {
//        variables.put("inputExercise", objectMapper.convertValue(
//                        createTestInputExercise(),
//                new TypeReference<LinkedHashMap<String, Object>>() {
//                }
//                )
//        );
//        Set<MuscleEntity> muscles = new HashSet<>();
//        Set<ExerciseTypeEntity> exerciseType = new HashSet<>();
//        when(roleService.exists(Mockito.any(Long.class))).thenReturn(true);
//        when(privilegeService.findMany(Mockito.anySet())).thenReturn(muscles);
//        when(exerciseTypeService.findMany(Mockito.anySet())).thenReturn(exerciseType);
//        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(privilegeEntity));
//        when(roleService.save(Mockito.any(ExerciseEntity.class))).thenReturn(privilegeEntity);
//        when(privilegeMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(privilegeDto);
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
//    void PrivilegeController_DeleteExercise_Unsuccessful() {
//        variables.put("exerciseId", 1);
//        when(roleService.exists(Mockito.any(Long.class))).thenReturn(false);
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
//    void PrivilegeController_DeleteExercise_Success() {
//        variables.put("exerciseId", 1);
//        when(roleService.exists(Mockito.any(Long.class))).thenReturn(true);
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