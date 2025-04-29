package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;
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
import org.springframework.test.context.TestPropertySource;

import java.util.LinkedHashMap;

import static com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery.AuthQuery.loginQuery;
import static com.CptFranck.SportsPeak.controller.IntegrationTest.graphqlQuery.AuthQuery.registerQuery;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUserBis;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class AuthControllerDGSIntTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtProvider jwtProvider;

    private UserEntity user;
    private String rawPassword;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    public void init() {
        user = createTestUser(null);
        rawPassword = user.getPassword();
        user = authService.register(user);
        roleRepository.save(createTestRole(null, 0));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void AuthController_Login_UnsuccessfulEmailUnknown() {
        variables.put("inputCredentials", objectMapper.convertValue(
                        new InputCredentials("user.getEmail()", rawPassword),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(loginQuery, "data.login", variables));

        Assertions.assertTrue(exception.getMessage().contains("EmailUnknownException"));
        Assertions.assertTrue(exception.getMessage().contains("Email unknown"));
    }

    @Test
    public void AuthController_Login_UnsuccessfulIncorrectPass() {
        variables.put("inputCredentials", objectMapper.convertValue(
                        new InputCredentials(user.getEmail(), "rawPassword"),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(loginQuery, "data.login", variables));

        Assertions.assertTrue(exception.getMessage().contains("IncorrectPasswordException"));
        Assertions.assertTrue(exception.getMessage().contains("Incorrect password"));
    }

    @Test
    public void AuthController_Login_ReturnAuthDto() {
        variables.put("inputCredentials", objectMapper.convertValue(
                        new InputCredentials(user.getEmail(), rawPassword),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        LinkedHashMap<String, Object> response =
                dgsQueryExecutor.executeAndExtractJsonPath(loginQuery, "data.login", variables);

        assertAuthDtoValid(user, response, true);
    }

    @Test
    public void AuthController_Register_UnsuccessfulEmailAlreadyUsed() {
        variables.put("inputRegisterNewUser", objectMapper.convertValue(
                        new InputRegisterNewUser(
                                user.getEmail(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getLastName(),
                                user.getPassword()
                        ),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables));

        Assertions.assertTrue(exception.getMessage().contains("EmailAlreadyUsedException"));
        Assertions.assertTrue(exception.getMessage().contains("Email already used"));
    }

    @Test
    public void AuthController_Register_UnsuccessfulUsernameExists() {
        UserEntity userBis = createTestUserBis(null);
        variables.put("inputRegisterNewUser", objectMapper.convertValue(
                        new InputRegisterNewUser(
                                userBis.getEmail(),
                                userBis.getFirstName(),
                                userBis.getLastName(),
                                user.getUsername(),
                                userBis.getPassword()
                        ),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables));

        Assertions.assertTrue(exception.getMessage().contains("UsernameExistsException"));
        Assertions.assertTrue(exception.getMessage().contains("Username already used for an other account"));
    }

    @Test
    public void AuthController_Register_ReturnAuthDto() {
        UserEntity userBis = createTestUserBis(null);
        variables.put("inputRegisterNewUser", objectMapper.convertValue(
                        new InputRegisterNewUser(
                                userBis.getEmail(),
                                userBis.getFirstName(),
                                userBis.getLastName(),
                                userBis.getUsername(),
                                userBis.getPassword()
                        ),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );

        LinkedHashMap<String, Object> response =
                dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables);

        assertAuthDtoValid(userBis, response, false);
    }

    private void assertAuthDtoValid(UserEntity userEntity, LinkedHashMap<String, Object> response, boolean beenRegistered) {
        Assertions.assertNotNull(response);
        Assertions.assertTrue(jwtProvider.validateToken((String) response.get("accessToken")));

        UserDto userDto = objectMapper.convertValue(response.get("user"), UserDto.class);

        Assertions.assertEquals(userEntity.getEmail(), userDto.getEmail());
        Assertions.assertEquals(userEntity.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), userDto.getLastName());
        Assertions.assertEquals(userEntity.getUsername(), userDto.getUsername());
        if (beenRegistered)
            Assertions.assertEquals(userEntity.getRoles().size(), userDto.getRoles().size());
    }
}
