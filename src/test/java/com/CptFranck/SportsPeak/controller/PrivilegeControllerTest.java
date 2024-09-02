package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleService;
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

import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.*;
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

        List<LinkedHashMap<String, Object>> privilegeDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getPrivileges");

        Assertions.assertNotNull(privilegeDtos);
    }

    @Test
    void PrivilegeController_GetPrivilegeById_Unsuccessful() {
        variables.put("id", 1);
        when(privilegeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        @Language("GraphQL")
        String query = """
                 query ($id : Int!) {
                      getPrivilegeById (id: $id) {
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

        LinkedHashMap<String, Object> privilegeDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getPrivilegeById", variables);

        Assertions.assertNull(privilegeDto);
    }

    @Test
    void PrivilegeController_GetPrivilegeById_Success() {
        variables.put("id", 1);
        when(privilegeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(privilegeEntity));
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        @Language("GraphQL")
        String query = """
                 query ($id : Int!) {
                      getPrivilegeById (id: $id) {
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

        LinkedHashMap<String, Object> privilegeDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getPrivilegeById", variables);

        Assertions.assertNotNull(privilegeDto);
    }

    @Test
    void PrivilegeController_AddPrivilege_Success() {
        variables.put("inputNewPrivilege", objectMapper.convertValue(
                        createTestInputNewPrivilege(),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        Set<RoleEntity> roles = new HashSet<>();
        when(roleService.findMany(Mockito.anySet())).thenReturn(roles);
        when(privilegeService.save(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeEntity);
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        @Language("GraphQL")
        String query = """
                 mutation ($inputNewPrivilege : InputNewPrivilege!){
                      addPrivilege(inputNewPrivilege: $inputNewPrivilege) {
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

        LinkedHashMap<String, Object> privilegeDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.addPrivilege", variables);

        Assertions.assertNotNull(privilegeDto);
    }

    @Test
    void PrivilegeController_ModifyPrivilege_Unsuccessful() {
        variables.put("inputPrivilege", objectMapper.convertValue(
                        createTestInputPrivilege(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(false);

        @Language("GraphQL")
        String query = """
                 mutation ($inputPrivilege : InputPrivilege!){
                     modifyPrivilege(inputPrivilege: $inputPrivilege) {
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

        LinkedHashMap<String, Object> privilegeDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyPrivilege", variables);

        Assertions.assertNull(privilegeDto);
    }

    @Test
    void PrivilegeController_ModifyPrivilege_Success() {
        variables.put("inputPrivilege", objectMapper.convertValue(
                        createTestInputPrivilege(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        Set<RoleEntity> roles = new HashSet<>();
        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(roleService.findMany(Mockito.anySet())).thenReturn(roles);
        when(privilegeService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(privilegeEntity));
        when(privilegeService.save(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeEntity);
        when(privilegeMapper.mapTo(Mockito.any(PrivilegeEntity.class))).thenReturn(privilegeDto);

        @Language("GraphQL")
        String query = """
                 mutation ($inputPrivilege : InputPrivilege!){
                     modifyPrivilege(inputPrivilege: $inputPrivilege) {
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

        LinkedHashMap<String, Object> privilegeDto =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyPrivilege", variables);

        Assertions.assertNotNull(privilegeDto);
    }

    @Test
    void PrivilegeController_DeletePrivilege_Unsuccessful() {
        variables.put("privilegeId", 1);
        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(false);

        @Language("GraphQL")
        String query = """
                 mutation ($privilegeId : Int!){
                     deletePrivilege(privilegeId: $privilegeId)
                 }
                """;

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deletePrivilege", variables);

        Assertions.assertNull(id);
    }

//    @Test
//    void PrivilegeController_DeletePrivilege_Success() {
//        variables.put("privilegeId", 1);
//        when(privilegeService.exists(Mockito.any(Long.class))).thenReturn(true);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($privilegeId : Int!){
//                     deletePrivilege(privilegeId: $privilegeId)
//                 }
//                """;
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deletePrivilege", variables);
//
//        Assertions.assertNotNull(id);
//    }
}