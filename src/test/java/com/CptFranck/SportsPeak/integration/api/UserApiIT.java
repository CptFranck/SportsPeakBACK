package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.*;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.ProgExerciseQuery.getUserProgExercisesQuery;
import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.UserQuery.*;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class UserApiIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity user;
    private UserEntity userBis;
    private String rawPassword;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        user = createTestUser(null);
        rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        user = userRepository.save(user);
        userBis = userRepository.save(createTestUserBis(null));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    public void afterEach() {
        userRepository.findAll().forEach(user -> {
            user.setSubscribedProgExercises(new HashSet<>());
            userRepository.save(user);
        });
        progExerciseRepository.deleteAll();
        exerciseRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getUsers_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getUsersQuery, "data.getUsers"));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getUsers_ValidUse_ReturnListOfUserDto() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getUsersQuery,
                "data.getUsers");

        List<UserDto> userDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualUserList(List.of(user, userBis), userDtos);
    }

    @Test
    void getUserById_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        variables.put("id", user.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getUserByIdQuery, "data.getUserById", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getUserById_InvalidTargetSetId_ThrowUserNotFoundException() {
        userRepository.delete(user);
        variables.put("id", user.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getUserByIdQuery, "data.getUserById", variables));

        Assertions.assertTrue(exception.getMessage().contains("UserNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("User with id %s has been not found", user.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getUserById_ValidInput_ReturnUserDto() {
        variables.put("id", user.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getUserByIdQuery,
                "data.getUserById", variables);

        UserDto userDto = objectMapper.convertValue(response, UserDto.class);
        assertUserDtoAndEntity(user, userDto);
    }

    @Test
    void getUserProgExercises_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        variables.put("userId", user.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getUserProgExercisesQuery, "data.getUserProgExercises", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getUserProgExercises_InvalidUserId_ThrowUserNotFoundException() {
        userRepository.delete(user);
        variables.put("userId", user.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getUserProgExercisesQuery, "data.getUserProgExercises", variables));

        Assertions.assertTrue(exception.getMessage().contains("UserNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("User with id %s has been not found", user.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getUserProgExercises_ValidUse_ReturnListOfUserDto() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        user.getSubscribedProgExercises().add(progExercise);
        userRepository.save(user);

        variables.put("userId", user.getId());

        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getUserProgExercisesQuery,
                "data.getUserProgExercises", variables);

        List<ProgExerciseDto> progExerciseDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        Assertions.assertEquals(progExercise.getId(), progExerciseDtos.getFirst().getId());
        Assertions.assertEquals(progExercise.getName(), progExerciseDtos.getFirst().getName());
        Assertions.assertEquals(progExercise.getNote(), progExerciseDtos.getFirst().getNote());
        Assertions.assertEquals(progExercise.getVisibility().label, progExerciseDtos.getFirst().getVisibility());
        Assertions.assertEquals(progExercise.getTrustLabel().label, progExerciseDtos.getFirst().getTrustLabel());
        Assertions.assertEquals(progExercise.getCreator().getId(), progExerciseDtos.getFirst().getCreator().getId());
        Assertions.assertEquals(progExercise.getExercise().getId(), progExerciseDtos.getFirst().getExercise().getId());
        Assertions.assertEquals(progExercise.getTargetSets().size(), progExerciseDtos.getFirst().getTargetSets().size());
    }

    @Test
    void modifyUserIdentity_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        variables.put("inputUserIdentity", objectMapper.convertValue(
                createTestInputUserIdentity(user.getId()),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserIdentityQuery, "data.modifyUserIdentity", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyUserIdentity_InvalidUserId_ThrowUserNotFoundException() {
        InputUserIdentity inputUserIdentity = createTestInputUserIdentity(user.getId());
        variables.put("inputUserIdentity", objectMapper.convertValue(
                inputUserIdentity,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        userRepository.delete(user);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserIdentityQuery, "data.modifyUserIdentity", variables));

        Assertions.assertTrue(exception.getMessage().contains("UserNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("User with id %s has been not found", user.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyUserIdentity_ValidUse_ReturnUserDto() {
        InputUserIdentity inputUserIdentity = createTestInputUserIdentity(user.getId());
        variables.put("inputUserIdentity", objectMapper.convertValue(
                inputUserIdentity,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyUserIdentityQuery,
                "data.modifyUserIdentity", variables);

        UserDto userDto = objectMapper.convertValue(response, UserDto.class);
        Assertions.assertEquals(userDto.getFirstName(), inputUserIdentity.getFirstName());
        Assertions.assertEquals(userDto.getLastName(), inputUserIdentity.getLastName());
    }

    @Test
    void modifyUserRoles_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        InputUserRoles inputUserIdentity = createTestInputUserRoles(user.getId());
        inputUserIdentity.getRoleIds().add(role.getId());
        variables.put("inputUserRoles", objectMapper.convertValue(
                inputUserIdentity,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserRolesQuery, "data.modifyUserRoles", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyUserRoles_ValidUse_ReturnUserDto() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        InputUserRoles inputUserIdentity = createTestInputUserRoles(user.getId());
        inputUserIdentity.getRoleIds().add(role.getId());
        variables.put("inputUserRoles", objectMapper.convertValue(
                inputUserIdentity,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyUserRolesQuery,
                "data.modifyUserRoles", variables);

        UserDto userDto = objectMapper.convertValue(response, UserDto.class);
        Assertions.assertEquals(role.getName(), userDto.getRoles().stream().findFirst().orElseThrow().getName());
    }

    @Test
    void modifyUserEmail_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        variables.put("inputUserEmail", objectMapper.convertValue(
                createTestInputUserEmail(user.getId(), rawPassword),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserEmailQuery, "data.modifyUserEmail", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyUserEmail_UserEmailAlreadyUsed_ThrowUsernameExistsException() {
        InputUserEmail inputUserIdentity = createTestInputUserEmail(user.getId(), rawPassword);
        inputUserIdentity.setNewEmail(userBis.getEmail());
        variables.put("inputUserEmail", objectMapper.convertValue(
                inputUserIdentity,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserEmailQuery, "data.modifyUserEmail", variables));

        Assertions.assertTrue(exception.getMessage().contains("EmailAlreadyUsedException"));
        Assertions.assertTrue(exception.getMessage().contains("Email already used for an other account"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyUserEmail_ValidInput_ReturnAuthDto() {
        InputUserEmail inputUserIdentity = createTestInputUserEmail(user.getId(), rawPassword);
        variables.put("inputUserEmail", objectMapper.convertValue(
                inputUserIdentity,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyUserEmailQuery,
                "data.modifyUserEmail", variables);

        UserDto userDto = objectMapper.convertValue(response.get("user"), UserDto.class);
        Assertions.assertEquals(inputUserIdentity.getNewEmail(), userDto.getEmail());
    }

    @Test
    void modifyUserUsername_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        variables.put("inputUserUsername", objectMapper.convertValue(
                createTestInputUserUsername(user.getId()),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserUsernameQuery, "data.modifyUserUsername", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyUserUsername_UserUsernameAlreadyUsed_ThrowUsernameExistsException() {
        InputUserUsername inputUserUsername = createTestInputUserUsername(user.getId());
        inputUserUsername.setNewUsername(userBis.getUsername());
        variables.put("inputUserUsername", objectMapper.convertValue(
                inputUserUsername,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserUsernameQuery, "data.modifyUserUsername", variables));

        Assertions.assertTrue(exception.getMessage().contains("UsernameExistsException"));
        Assertions.assertTrue(exception.getMessage().contains("Username already used for an other account"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyUserUsername_ValidInput_ReturnUserDto() {
        InputUserUsername inputUserUsername = createTestInputUserUsername(user.getId());
        variables.put("inputUserUsername", objectMapper.convertValue(
                inputUserUsername,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyUserUsernameQuery,
                "data.modifyUserUsername", variables);

        UserDto userDto = objectMapper.convertValue(response, UserDto.class);
        Assertions.assertEquals(inputUserUsername.getNewUsername(), userDto.getUsername());
    }

    @Test
    void modifyUserPassword_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        variables.put("inputUserPassword", objectMapper.convertValue(
                createTestInputUserPassword(user.getId(), rawPassword),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserPasswordQuery, "data.modifyUserPassword", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyUserPassword_ValidUse_ReturnAuthDto() {
        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), rawPassword);
        variables.put("inputUserPassword", objectMapper.convertValue(
                inputUserPassword,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyUserPasswordQuery, "data.modifyUserPassword", variables);

        UserDto userDto = objectMapper.convertValue(response.get("user"), UserDto.class);
        assertUserDtoAndEntity(user, userDto);
        UserEntity modifyUser = userRepository.findById(user.getId()).orElseThrow();
        Assertions.assertTrue(passwordEncoder.matches(inputUserPassword.getNewPassword(), modifyUser.getPassword()));
    }

    @Test
    void deleteUser_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        variables.put("userId", user.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteUserQuery, "data.deleteUser", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteUser_InvalidUserId_ThrowUnsuccessfulUserNotFound() {
        userRepository.delete(user);
        variables.put("userId", user.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteUserQuery, "data.deleteUser", variables));

        Assertions.assertTrue(exception.getMessage().contains("UserNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("User with id %s has been not found", user.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteUser_ValidUse_ReturnId() {
        variables.put("userId", user.getId());

        String id = dgsQueryExecutor.executeAndExtractJsonPath(deleteUserQuery, "data.deleteUser", variables);

        Assertions.assertEquals(user.getId(), Long.valueOf(id));
    }

    private void assertEqualUserList(
            List<UserEntity> userEntities,
            List<UserDto> userDtos
    ) {
        Assertions.assertEquals(userEntities.size(), userDtos.size());
        userDtos.forEach(userDto -> assertUserDtoAndEntity(
                userEntities.stream().filter(
                        userEntity -> Objects.equals(userEntity.getId(), userDto.getId())
                ).toList().getFirst(),
                userDto)
        );
    }

    private void assertUserDtoAndEntity(UserEntity userEntity, UserDto userDto) {
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userEntity.getEmail(), userDto.getEmail());
        Assertions.assertEquals(userEntity.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), userDto.getLastName());
        Assertions.assertEquals(userEntity.getUsername(), userDto.getUsername());
        Assertions.assertEquals(userEntity.getRoles().size(), userDto.getRoles().size());
    }
}