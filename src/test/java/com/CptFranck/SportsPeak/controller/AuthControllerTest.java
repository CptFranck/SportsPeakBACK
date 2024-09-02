package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;
import com.CptFranck.SportsPeak.domain.model.JWToken;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Optional;

import static com.CptFranck.SportsPeak.controller.graphqlQuery.AuthQuery.loginQuery;
import static com.CptFranck.SportsPeak.controller.graphqlQuery.AuthQuery.registerQuery;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        AuthController.class
})
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private AuthService authService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private JwtProvider userAuthProvider;

    @MockBean
    private Mapper<UserEntity, UserDto> userMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    private UserEntity user;
    private UserDto userDto;
    private JWToken jwToken;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
        userDto = createTestUserDto();
        jwToken = new JWToken("token", LocalDateTime.now());
        variables = new LinkedHashMap<>();
    }

    @Test
    public void AuthController_Login_ReturnAuthDto() {
        variables.put("inputCredentials", objectMapper.convertValue(
                new InputCredentials(user.getEmail(), user.getPassword()),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }
                )
        );
        when(authService.login(Mockito.any(InputCredentials.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userAuthProvider.generateToken(Mockito.any())).thenReturn(jwToken);

        LinkedHashMap<String, Object> authDto =
                dgsQueryExecutor.executeAndExtractJsonPath(loginQuery, "data.login", variables);

        Assertions.assertNotNull(authDto);
    }

    @Test
    public void AuthController_Register_Unsuccessful() {
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
        when(roleService.findByName(Mockito.any(String.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables));
    }

    @Test
    public void AuthController_Register_ReturnAuthDto() {
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
        RoleEntity role = new RoleEntity(1L, "Role", new HashSet<>(), new HashSet<>());
        when(roleService.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));
        when(authService.register(Mockito.any(UserEntity.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userAuthProvider.generateToken(Mockito.any())).thenReturn(jwToken);

        LinkedHashMap<String, Object> authDto =
                dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables);

        Assertions.assertNotNull(authDto);
    }
}
