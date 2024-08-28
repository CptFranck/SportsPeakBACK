package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.model.JWToken;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.RoleService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {DgsAutoConfiguration.class, AuthController.class})
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthService authService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private JwtProvider userAuthProvider;

    @MockBean
    private Mapper<UserEntity, UserDto> userMapper;

    private UserEntity user;

    private Authentication authentication;

    private JWToken jwToken;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), user.getPassword());
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        inputCredentials.getEmail(),
                        inputCredentials.getPassword()));
        jwToken = userAuthProvider.generateToken(authentication);
    }

    @Test
    public void AuthController_Login_ReturnAuthDto() throws Exception {
        when(authService.login(Mockito.any(InputCredentials.class))).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userAuthProvider.generateToken(Mockito.any(Authentication.class))).thenReturn(jwToken);

        AuthDto authDto = dgsQueryExecutor.executeAndExtractJsonPath(
                "{login (inputCredentials: {email: , password: }) { tokenType accessToken expiration }}",
                "data.shows[*].title");
        Assertions.assertNotNull(authDto);
    }
}
