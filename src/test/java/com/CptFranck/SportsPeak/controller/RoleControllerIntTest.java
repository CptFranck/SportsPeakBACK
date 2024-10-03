package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
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

import static com.CptFranck.SportsPeak.controller.graphqlQuery.RoleQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        RoleController.class
})
class RoleControllerIntTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

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

        List<LinkedHashMap<String, Object>> roleDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getRolesQuery, "data.getRoles");

        Assertions.assertNotNull(roleDtos);
    }

    @Test
    void RoleController_GetRoleById_Unsuccessful() {
        variables.put("id", 1);
        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        LinkedHashMap<String, Object> roleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getRoleByIdQuery, "data.getRoleById", variables);

        Assertions.assertNull(roleDto);
    }

    @Test
    void RoleController_GetRoleById_Success() {
        variables.put("id", 1);
        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(roleEntity));
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        LinkedHashMap<String, Object> roleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getRoleByIdQuery, "data.getRoleById", variables);

        Assertions.assertNotNull(roleDto);
    }

    @Test
    void RoleController_AddRole_Success() {
        variables.put("inputNewRole", objectMapper.convertValue(
                        createTestInputNewRole(),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        Set<UserEntity> users = new HashSet<>();
        Set<PrivilegeEntity> privileges = new HashSet<>();
        when(userService.findMany(Mockito.anySet())).thenReturn(users);
        when(privilegeService.findMany(Mockito.anySet())).thenReturn(privileges);
        when(roleService.save(Mockito.any(RoleEntity.class))).thenReturn(roleEntity);
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        LinkedHashMap<String, Object> roleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(addRoleQuery, "data.addRole", variables);

        Assertions.assertNotNull(roleDto);
    }

    @Test
    void RoleController_ModifyRole_UnsuccessfulDoesNotExist() {
        variables.put("inputRole", objectMapper.convertValue(
                        createTestInputRole(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(roleService.exists(Mockito.any(Long.class))).thenReturn(false);

        LinkedHashMap<String, Object> roleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyRoleQuery, "data.modifyRole", variables);

        Assertions.assertNull(roleDto);
    }

    @Test
    void RoleController_ModifyRole_Success() {
        variables.put("inputRole", objectMapper.convertValue(
                        createTestInputRole(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        Set<UserEntity> users = new HashSet<>();
        Set<PrivilegeEntity> privileges = new HashSet<>();
        when(roleService.exists(Mockito.any(Long.class))).thenReturn(true);
        when(userService.findMany(Mockito.anySet())).thenReturn(users);
        when(privilegeService.findMany(Mockito.anySet())).thenReturn(privileges);
        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(roleEntity));
        when(roleService.save(Mockito.any(RoleEntity.class))).thenReturn(roleEntity);
        when(roleMapper.mapTo(Mockito.any(RoleEntity.class))).thenReturn(roleDto);

        LinkedHashMap<String, Object> roleDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyRoleQuery, "data.modifyRole", variables);

        Assertions.assertNotNull(roleDto);
    }

    @Test
    void RoleController_DeleteRole_UnsuccessfulDoesNotExist() {
        variables.put("roleId", 1);
        when(roleService.exists(Mockito.any(Long.class))).thenReturn(false);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteRoleQuery, "data.deleteRole", variables);

        Assertions.assertNull(id);
    }

    @Test
    void RoleController_DeleteRole_Success() {
        variables.put("roleId", 1);
        when(roleService.exists(Mockito.any(Long.class))).thenReturn(true);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteRoleQuery, "data.deleteRole", variables);

        Assertions.assertNotNull(id);
    }
}