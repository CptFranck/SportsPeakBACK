package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;
import com.CptFranck.SportsPeak.repositories.PrivilegeRepository;
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

import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.PrivilegeQuery.*;
import static com.CptFranck.SportsPeak.utils.TestPrivilegeUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class PrivilegeApiIT {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    private PrivilegeEntity privilege;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        privilege = privilegeRepository.save(createTestPrivilege(null, 0));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    void afterEach() {
        privilegeRepository.deleteAll();
    }

    @Test
    void getPrivileges_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getPrivilegeQuery, "data.getPrivileges"));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getPrivileges_ValidUse_ReturnListOfPrivilegeDto() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getPrivilegeQuery,
                "data.getPrivileges");

        List<PrivilegeDto> privilegeDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualPrivilegeList(List.of(privilege), privilegeDtos);
    }

    @Test
    void getPrivilegeById_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        variables.put("id", privilege.getId());
        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getPrivilegeByIdQuery, "data.getPrivilegeById", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getPrivilegeById_InvalidPrivilegeId_ThrowPrivilegeNotFoundException() {
        variables.put("id", privilege.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getPrivilegeByIdQuery, "data.getPrivilegeById", variables));

        Assertions.assertTrue(exception.getMessage().contains("PrivilegeNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The privilege with the id %s has not been found", privilege.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getPrivilegeById_ValidPrivilegeId_ReturnPrivilegeDto() {
        variables.put("id", privilege.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getPrivilegeByIdQuery,
                "data.getPrivilegeById", variables);

        PrivilegeDto privilegeDto = objectMapper.convertValue(response, PrivilegeDto.class);
        assertPrivilegeDtoAndEntity(privilege, privilegeDto);
    }

    @Test
    void addPrivilege_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        variables.put("inputNewPrivilege", objectMapper.convertValue(
                createTestInputNewPrivilege(), new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addPrivilegeQuery, "data.addPrivilege", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void addPrivilege_AddNewPrivilegeWithNameAlreadyTaken_ThrowPrivilegeExistsException() {
        InputNewPrivilege inputNewPrivilege = createTestInputNewPrivilege();
        variables.put("inputNewPrivilege", objectMapper.convertValue(
                inputNewPrivilege, new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addPrivilegeQuery, "data.addPrivilege", variables));

        Assertions.assertTrue(exception.getMessage().contains("PrivilegeExistsException"));
        Assertions.assertTrue(exception.getMessage().contains("A privilege with this name already exists"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void PrivilegeApi_AddPrivilege_Success() {
        InputNewPrivilege inputNewPrivilege = createTestInputNewPrivilege();
        inputNewPrivilege.setName("name");
        variables.put("inputNewPrivilege", objectMapper.convertValue(
                inputNewPrivilege, new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(addPrivilegeQuery,
                "data.addPrivilege", variables);

        PrivilegeDto privilegeDto = objectMapper.convertValue(response, PrivilegeDto.class);
        assertPrivilegeDtoAndInput(inputNewPrivilege, privilegeDto);
    }

    @Test
    void modifyPrivilege_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        variables.put("inputPrivilege", objectMapper.convertValue(
                createTestInputPrivilege(privilege.getId()), new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPrivilegeQuery, "data.getPrivilegeById", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyPrivilege_UpdatePrivilegeWithNameAlreadyTaken_ThrowPrivilegeExistsException() {
        variables.put("inputPrivilege", objectMapper.convertValue(
                createTestInputPrivilege(privilege.getId() + 1), new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPrivilegeQuery, "data.getPrivilegeById", variables));

        Assertions.assertTrue(exception.getMessage().contains("PrivilegeNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The privilege with the id %s has not been found", privilege.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyPrivilege_InvalidPrivilegeId_ThrowPrivilegeNotFoundException() {
        variables.put("inputPrivilege", objectMapper.convertValue(
                createTestInputPrivilege(privilege.getId() + 1), new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        privilege.setName("name");
        privilegeRepository.save(privilege);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyPrivilegeQuery, "data.getPrivilegeById", variables));

        Assertions.assertTrue(exception.getMessage().contains("PrivilegeNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The privilege with the id %s has not been found", privilege.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyPrivilege_UpdatePrivilege_ReturnPrivilegeDto() {
        InputPrivilege inputPrivilege = createTestInputPrivilege(privilege.getId());
        variables.put("inputPrivilege", objectMapper.convertValue(
                inputPrivilege, new TypeReference<LinkedHashMap<String, Object>>() {
                }));


        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyPrivilegeQuery,
                "data.modifyPrivilege", variables);

        PrivilegeDto privilegeDto = objectMapper.convertValue(response, PrivilegeDto.class);
        assertPrivilegeDtoAndInput(inputPrivilege, privilegeDto);
    }

    @Test
    void deletePrivilege_NotAuthenticated_ThrowsQueryAuthenticationCredentialsNotFoundException() {
        variables.put("privilegeId", privilege.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deletePrivilegeQuery, "data.deletePrivilege", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));

    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deletePrivilege_InvalidPrivilegeId_ThrowPrivilegeNotFoundException() {
        variables.put("privilegeId", privilege.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deletePrivilegeQuery, "data.deletePrivilege", variables));

        Assertions.assertTrue(exception.getMessage().contains("PrivilegeNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The privilege with the id %s has not been found", privilege.getId() + 1)));

    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deletePrivilege_ValidInput_Void() {
        variables.put("privilegeId", privilege.getId());

        String id = dgsQueryExecutor.executeAndExtractJsonPath(deletePrivilegeQuery, "data.deletePrivilege", variables);

        Assertions.assertEquals(privilege.getId(), Long.valueOf(id));
    }

    private void assertEqualPrivilegeList(
            List<PrivilegeEntity> privilegeEntities,
            List<PrivilegeDto> privilegeDtos
    ) {
        Assertions.assertEquals(privilegeEntities.size(), privilegeDtos.size());
        privilegeDtos.forEach(exerciseDto -> assertPrivilegeDtoAndEntity(
                privilegeEntities.stream().filter(
                        privilegeEntity -> Objects.equals(privilegeEntity.getId(), exerciseDto.getId())
                ).toList().getFirst(),
                exerciseDto)
        );
    }

    private void assertPrivilegeDtoAndEntity(PrivilegeEntity privilegeEntity, PrivilegeDto privilegeDto) {
        Assertions.assertNotNull(privilegeDto);
        Assertions.assertEquals(privilegeEntity.getId(), privilegeDto.getId());
        Assertions.assertEquals(privilegeEntity.getName(), privilegeDto.getName());
    }

    private void assertPrivilegeDtoAndInput(InputNewPrivilege inputNewPrivilege, PrivilegeDto privilegeDto) {
        Assertions.assertNotNull(privilegeDto);
        Assertions.assertEquals(inputNewPrivilege.getName(), privilegeDto.getName());
    }
}