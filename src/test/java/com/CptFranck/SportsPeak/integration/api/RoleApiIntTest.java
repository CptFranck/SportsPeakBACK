package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.domain.input.role.InputRole;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.RoleQuery.*;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.*;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class RoleApiIntTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity role;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        role = roleRepository.save(createTestRole(null, 0));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    void afterEach() {
        roleRepository.deleteAll();
    }

    @Test
    void RoleApi_GetRoles_UnsuccessfulNotAuthenticated() {
        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getRolesQuery, "data.getRoles"));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleApi_GetRoles_Success() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getRolesQuery,
                "data.getRoles");

        List<RoleDto> roleDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualRoleList(List.of(role), roleDtos);
    }

    @Test
    void RoleApi_GetRoleById_UnsuccessfulNotAuthenticated() {
        variables.put("id", role.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getRoleByIdQuery, "data.getRoleById", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleApi_GetRoleById_UnsuccessfulRoleNotFound() {
        variables.put("id", role.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getRoleByIdQuery, "data.getRoleById", variables));

        Assertions.assertTrue(exception.getMessage().contains("RoleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The role the id or the name %s has not been found", role.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleApi_GetRoleById_Success() {
        variables.put("id", role.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getRoleByIdQuery,
                "data.getRoleById", variables);

        RoleDto roleDto = objectMapper.convertValue(response, RoleDto.class);
        assertRoleDtoAndEntity(role, roleDto);
    }

    @Test
    void RoleApi_AddRole_UnsuccessfulNotAuthenticated() {
        InputNewRole inputNewRole = createTestInputNewRole();
        variables.put("inputNewRole", objectMapper.convertValue(
                inputNewRole,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addRoleQuery, "data.addRole", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleApi_AddRole_Success() {
        InputNewRole inputNewRole = createTestInputNewRole();
        variables.put("inputNewRole", objectMapper.convertValue(
                inputNewRole,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(addRoleQuery,
                "data.addRole", variables);

        RoleDto roleDto = objectMapper.convertValue(response, RoleDto.class);
        assertRoleDtoAndInput(inputNewRole, roleDto);
    }

    @Test
    void RoleApi_ModifyRole_UnsuccessfulNotAuthenticated() {
        variables.put("inputRole", objectMapper.convertValue(
                createTestInputRole(role.getId() + 1),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyRoleQuery, "data.modifyRole", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleApi_ModifyRole_UnsuccessfulRoleNotFound() {
        variables.put("inputRole", objectMapper.convertValue(
                createTestInputRole(role.getId() + 1),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyRoleQuery, "data.modifyRole", variables));

        Assertions.assertTrue(exception.getMessage().contains("RoleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The role the id or the name %s has not been found", role.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleApi_ModifyRole_Success() {
        InputRole inputRole = createTestInputRole(role.getId());
        variables.put("inputRole", objectMapper.convertValue(
                inputRole,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyRoleQuery,
                "data.modifyRole", variables);

        RoleDto roleDto = objectMapper.convertValue(response, RoleDto.class);
        assertRoleDtoAndInput(inputRole, roleDto);
    }

    @Test
    void RoleApi_DeleteRole_UnsuccessfulNotAuthenticated() {
        variables.put("roleId", role.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteRoleQuery, "data.deleteRole", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void RoleApi_DeleteRole_Success() {
        variables.put("roleId", role.getId());

        String id = dgsQueryExecutor.executeAndExtractJsonPath(deleteRoleQuery, "data.deleteRole", variables);

        Assertions.assertEquals(role.getId(), Long.valueOf(id));
    }

    private void assertEqualRoleList(
            List<RoleEntity> roleEntities,
            List<RoleDto> roleDtos
    ) {
        Assertions.assertEquals(roleEntities.size(), roleDtos.size());
        roleEntities.forEach(roleEntity -> assertRoleDtoAndEntity(
                roleEntity,
                roleDtos.stream().filter(
                        roleDto -> Objects.equals(roleDto.getId(), roleEntity.getId())
                ).toList().getFirst())
        );
    }

    private void assertRoleDtoAndEntity(RoleEntity roleEntity, RoleDto roleDto) {
        Assertions.assertNotNull(roleDto);
        Assertions.assertEquals(roleEntity.getName(), roleDto.getName());
    }

    private void assertRoleDtoAndInput(InputNewRole inputNewRole, RoleDto roleDto) {
        Assertions.assertNotNull(roleDto);
        Assertions.assertEquals(inputNewRole.getName(), roleDto.getName());
    }
}