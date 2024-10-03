package com.CptFranck.SportsPeak.controller.UnitTest;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.controller.AuthController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;
import com.CptFranck.SportsPeak.domain.model.JWToken;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private RoleService roleService;

    @Mock
    private JwtProvider userAuthProvider;

    @Mock
    private Mapper<UserEntity, UserDto> userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    private UserEntity user;
    private UserDto userDto;
    private JWToken jwToken;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
        userDto = createTestUserDto(1L);
        jwToken = new JWToken("token", LocalDateTime.now());
    }

    @Test
    public void AuthController_Login_ReturnAuthDto() {
        when(authService.login(Mockito.any(InputCredentials.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userAuthProvider.generateToken(Mockito.any())).thenReturn(jwToken);

        AuthDto authDto = authController.login(new InputCredentials(user.getEmail(), user.getPassword()));

        Assertions.assertNotNull(authDto);
    }

    @Test
    public void AuthController_Register_Unsuccessful() {
        when(roleService.findByName(Mockito.any(String.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(RoleNotFoundException.class,
                () -> authController.register(new InputRegisterNewUser(
                                user.getEmail(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getLastName(),
                                user.getPassword()
                        )
                )
        );
    }

    @Test
    public void AuthController_Register_ReturnAuthDto() {
        RoleEntity role = new RoleEntity(1L, "Role", new HashSet<>(), new HashSet<>());
        when(roleService.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));
        when(authService.register(Mockito.any(UserEntity.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userAuthProvider.generateToken(Mockito.any())).thenReturn(jwToken);

        AuthDto authDto = authController.register(new InputRegisterNewUser(
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getLastName(),
                        user.getPassword()
                )
        );

        Assertions.assertNotNull(authDto);
    }
}
