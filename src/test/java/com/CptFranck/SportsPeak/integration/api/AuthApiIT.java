package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.config.security.JwtUtils;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.AuthService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;

import java.util.LinkedHashMap;

import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.AuthQuery.loginQuery;
import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.AuthQuery.registerQuery;
import static com.CptFranck.SportsPeak.utils.AuthUtils.createRegisterInput;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUserBis;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class AuthApiIT {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtProvider;

    private UserEntity user;
    private String rawPassword;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    public void init() {
        roleRepository.save(createTestRole(null, 0));
        user = createTestUser(null);
        rawPassword = user.getPassword();
        user = authService.register(createRegisterInput(user)).getUser();
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void login_EmailNotFound_ThrowEmailUnknownException() {
        variables.put("inputCredentials", objectMapper.convertValue(
                new InputCredentials("login", rawPassword),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(loginQuery, "data.login", variables));

        Assertions.assertTrue(exception.getMessage().contains("InvalidCredentialsException"));
        Assertions.assertTrue(exception.getMessage().contains("Invalid credentials"));
    }

    @Test
    public void login_IncorrectPassword_ThrowsInvalidCredentialsException() {
        variables.put("inputCredentials", objectMapper.convertValue(
                new InputCredentials(user.getEmail(), "rawPassword"),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(loginQuery, "data.login", variables));

        Assertions.assertTrue(exception.getMessage().contains("InvalidCredentialsException"));
        Assertions.assertTrue(exception.getMessage().contains("Invalid credentials"));
    }

    @Test
    public void login_UserDeleted_ThrowsInvalidCredentialsException() {
        variables.put("inputCredentials", objectMapper.convertValue(
                new InputCredentials(user.getEmail(), rawPassword),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        userRepository.delete(user);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(loginQuery, "data.login", variables));

        Assertions.assertTrue(exception.getMessage().contains("InvalidCredentialsException"));
        Assertions.assertTrue(exception.getMessage().contains("Invalid credentials"));
    }

    @Test
    public void login_CorrectCredentialsWithEmail_ReturnAuthDto() {
        variables.put("inputCredentials", objectMapper.convertValue(
                new InputCredentials(user.getEmail(), rawPassword),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(loginQuery,
                "data.login", variables);

        assertAuthDtoValid(user, response, true);
    }

    @Test
    public void login_CorrectCredentialsWithUsername_ReturnAuthDto() {
        variables.put("inputCredentials", objectMapper.convertValue(
                new InputCredentials(user.getUsername(), rawPassword),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(loginQuery,
                "data.login", variables);

        assertAuthDtoValid(user, response, true);
    }

    @Test
    public void register_MissingUserRoleInDB_ThrowRoleNotFoundException() {
        variables.put("registerInput", objectMapper.convertValue(
                createRegisterInput(user),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        userRepository.deleteAll();
        roleRepository.deleteAll();

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables));

        Assertions.assertTrue(exception.getMessage().contains("RoleNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The role id or name %s has not been found", "USER")));
    }

    @Test
    public void register_UserEmailAlreadyTaken_ThrowEmailAlreadyUsedException() {
        variables.put("registerInput", objectMapper.convertValue(
                createRegisterInput(user),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables));

        Assertions.assertTrue(exception.getMessage().contains("EmailAlreadyUsedException"));
        Assertions.assertTrue(exception.getMessage().contains("Email already used"));
    }

    @Test
    public void register_UserUsernameAlreadyUsed_ThrowUsernameExistsException() {
        UserEntity userBis = createTestUserBis(null);
        RegisterInput registerInput = createRegisterInput(userBis);
        registerInput.setUsername(user.getUsername());
        variables.put("registerInput", objectMapper.convertValue(
                registerInput,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables));

        Assertions.assertTrue(exception.getMessage().contains("UsernameExistsException"));
        Assertions.assertTrue(exception.getMessage().contains("Username already used for an other account"));
    }

    @Test
    public void register_CorrectCredentials_ReturnAuthDto() {
        UserEntity userBis = createTestUserBis(null);
        RegisterInput registerInput = createRegisterInput(userBis);
        variables.put("registerInput", objectMapper.convertValue(
                registerInput,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(registerQuery,
                "data.register", variables);

        assertAuthDtoValid(userBis, response, false);
    }

    private void assertAuthDtoValid(UserEntity userEntity, LinkedHashMap<String, Object> response, boolean beenRegistered) {
        Assertions.assertNotNull(response);
        UserDto userDto = objectMapper.convertValue(response.get("user"), UserDto.class);
        String token = objectMapper.convertValue(response.get("token"), String.class);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getEmail());
        Assertions.assertTrue(jwtProvider.validateToken(token, userDetails));

        Assertions.assertEquals(userEntity.getEmail(), userDto.getEmail());
        Assertions.assertEquals(userEntity.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), userDto.getLastName());
        Assertions.assertEquals(userEntity.getUsername(), userDto.getUsername());
        if (beenRegistered)
            Assertions.assertEquals(userEntity.getRoles().size(), userDto.getRoles().size());
    }
}
