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
class RoleApiIT {

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
    void getRoles_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getRolesQuery, "data.getRoles"));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getRoles_ValidUse_ReturnListOfRoleDto() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getRolesQuery,
                "data.getRoles");

        List<RoleDto> roleDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualRoleList(List.of(role), roleDtos);
    }

    @Test
    void getRoleById_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        variables.put("id", role.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getRoleByIdQuery, "data.getRoleById", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getRoleById_InvalidRoleId_ThrowRoleNotFoundException() {
        roleRepository.delete(role);
        variables.put("id", role.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getRoleByIdQuery, "data.getRoleById", variables));

        Assertions.assertTrue(exception.getMessage().contains("RoleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The role the id or the name %s has not been found", role.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getRoleById_ValidUse_ReturnRoleDto() {
        variables.put("id", role.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getRoleByIdQuery,
                "data.getRoleById", variables);

        RoleDto roleDto = objectMapper.convertValue(response, RoleDto.class);
        assertRoleDtoAndEntity(role, roleDto);
    }

    @Test
    void addRole_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
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
    void addRole_AddNewRoleWithNameAlreadyTaken_ThrowRoleExistsException() {
        InputNewRole inputNewRole = createTestInputNewRole();
        inputNewRole.setName(role.getName());
        variables.put("inputNewRole", objectMapper.convertValue(
                inputNewRole,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addRoleQuery, "data.addRole", variables));

        Assertions.assertTrue(exception.getMessage().contains("RoleExistsException"));
        Assertions.assertTrue(exception.getMessage().contains("A role with this name already exists"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void addRole_ValidInput_ReturnRoleDto() {
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
    void modifyRole_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        variables.put("inputRole", objectMapper.convertValue(
                createTestInputRole(role.getId()),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyRoleQuery, "data.modifyRole", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyRole_InvalidRoleId_ThrowsRoleNotFoundException() {
        roleRepository.delete(role);
        variables.put("inputRole", objectMapper.convertValue(
                createTestInputRole(role.getId()),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyRoleQuery, "data.modifyRole", variables));

        Assertions.assertTrue(exception.getMessage().contains("RoleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The role the id or the name %s has not been found", role.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyRole_ValidInput_ReturnRoleDto() {
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
    void deleteRole_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        variables.put("roleId", role.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteRoleQuery, "data.deleteRole", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteRole_InvalidRoleId_ThrowsRoleNotFoundException() {
        roleRepository.delete(role);
        variables.put("roleId", role.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteRoleQuery, "data.deleteRole", variables));

        Assertions.assertTrue(exception.getMessage().contains("RoleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The role the id or the name %s has not been found", role.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteRole_ValidInput_ReturnRoleId() {
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