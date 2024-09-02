package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
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
import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRoleDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        RoleController.class
})
class RoleControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;
    @MockBean
    private Mapper<RoleEntity, RoleDto> roleMapper;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserService userService;

    @MockBean
    private PrivilegeService privilegeService;


    @MockBean
    private ExerciseTypeService exerciseTypeService;

    private RoleEntity roleEntity;
    private RoleDto roleDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        roleEntity = createTestRole(1L, 0);
        roleDto = createTestRoleDto(1L);
        variables = new LinkedHashMap<>();
    }

    @Test
    void RoleController_GetRoles_Success() {
        when(roleService.findAll()).thenReturn(List.of(roleEntity));
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        @Language("GraphQL")
        String query = """
                 query {
                     getRoles {
                         id
                         name
                         users {
                             id
                             email
                             firstName
                             lastName
                             username
                         }
                         privileges {
                             id
                             name
                         }
                     }
                 }
                """;

        List<LinkedHashMap<String, Object>> roleDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getRoles");

        Assertions.assertNotNull(roleDtos);
    }

    @Test
    void RoleController_GetRoleById_Unsuccessful() {
        variables.put("id", 1);
        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        @Language("GraphQL")
        String query = """
                 query ($id : Int!) {
                     getRoleById (id: $id) {
                         id
                         name
                         users {
                             id
                             email
                             firstName
                             lastName
                             username
                         }
                         privileges {
                             id
                             name
                         }
                     }
                 }
                """;

        LinkedHashMap<String, Object> roleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getRoleById", variables);

        Assertions.assertNull(roleDto);
    }

    @Test
    void RoleController_GetRoleById_Success() {
        variables.put("id", 1);
        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(roleEntity));
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        @Language("GraphQL")
        String query = """
                 query ($id : Int!) {
                       getRoleById (id: $id) {
                           id
                           name
                           users {
                               id
                               email
                               firstName
                               lastName
                               username
                           }
                           privileges {
                               id
                               name
                           }
                       }
                   }
                """;

        LinkedHashMap<String, Object> roleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getRoleById", variables);

        Assertions.assertNotNull(roleDto);
    }

//    @Test
//    void RoleController_AddRole_Success() {
//        variables.put("inputNewRole", objectMapper.convertValue(
//                        createTestInputNewRole(),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        Set<RoleEntity> roles = new HashSet<>();
//        when(roleService.findMany(Mockito.anySet())).thenReturn(roles);
//        when(privilegeService.save(Mockito.any(RoleEntity.class))).thenReturn(roleEntity);
//        when(privilegeMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($inputNewRole : InputNewRole!){
//                      addRole(inputNewRole: $inputNewRole) {
//                          id
//                          name
//                          roles {
//                              id
//                              name
//                              privileges {
//                                  id
//                                  name
//                              }
//                          }
//                      }
//                  }
//                """;
//
//        LinkedHashMap<String, Object> roleDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.addRole", variables);
//
//        Assertions.assertNotNull(roleDto);
//    }
//
//    @Test
//    void RoleController_ModifyRole_Unsuccessful() {
//        variables.put("inputRole", objectMapper.convertValue(
//                        createTestInputRole(1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(false);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($inputRole : InputRole!){
//                     modifyRole(inputRole: $inputRole) {
//                         id
//                         name
//                         roles {
//                             id
//                             name
//                             privileges {
//                                 id
//                                 name
//                             }
//                         }
//                     }
//                 }
//                """;
//
//        LinkedHashMap<String, Object> roleDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyRole", variables);
//
//        Assertions.assertNull(roleDto);
//    }
//
//    @Test
//    void RoleController_ModifyRole_Success() {
//        variables.put("inputRole", objectMapper.convertValue(
//                        createTestInputRole(1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        Set<RoleEntity> roles = new HashSet<>();
//        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(true);
//        when(roleService.findMany(Mockito.anySet())).thenReturn(roles);
//        when(privilegeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(roleEntity));
//        when(privilegeService.save(Mockito.any(RoleEntity.class))).thenReturn(roleEntity);
//        when(privilegeMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($inputRole : InputRole!){
//                     modifyRole(inputRole: $inputRole) {
//                         id
//                         name
//                         roles {
//                             id
//                             name
//                             privileges {
//                                 id
//                                 name
//                             }
//                         }
//                     }
//                 }
//                """;
//
//        LinkedHashMap<String, Object> roleDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyRole", variables);
//
//        Assertions.assertNotNull(roleDto);
//    }
//
//    @Test
//    void RoleController_DeleteRole_Unsuccessful() {
//        variables.put("privilegeId", 1);
//        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(false);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($privilegeId : Int!){
//                     deleteRole(privilegeId: $privilegeId)
//                 }
//                """;
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deleteRole", variables);
//
//        Assertions.assertNull(id);
//    }
//
//    @Test
//    void RoleController_DeleteRole_Success() {
//        variables.put("privilegeId", 1);
//        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(true);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($privilegeId : Int!){
//                     deleteRole(privilegeId: $privilegeId)
//                 }
//                """;
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deleteRole", variables);
//
//        Assertions.assertNotNull(id);
//    }
}