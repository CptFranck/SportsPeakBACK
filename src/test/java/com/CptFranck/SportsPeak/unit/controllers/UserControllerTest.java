package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.controller.UserController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.model.JWToken;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
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
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.CptFranck.SportsPeak.utils.TestUserUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private Mapper<UserEntity, UserDto> userMapper;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private JwtProvider userAuthProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    private UserEntity user;
    private UserDto userDtoMapped;

    @BeforeEach
    void init() {
        user = createTestUser(1L);
        userDtoMapped = createTestUserDto(1L);
    }

    @Test
    void UserController_GetUsers_Success() {
        when(userService.findAll()).thenReturn(List.of(user));
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDtoMapped);

        List<UserDto> userDtos = userController.getUsers();

        Assertions.assertEquals(List.of(this.userDtoMapped), userDtos);
    }

    @Test
    void UserController_GetUserById_Unsuccessful() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> userController.getUserById(1L)
        );
    }

    @Test
    void UserController_GetUserById_Success() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDtoMapped);

        UserDto userDto = userController.getUserById(1L);

        Assertions.assertEquals(this.userDtoMapped, userDto);
    }

    @Test
    void UserController_GetUserProgExercises_UnsuccessfulUserNotFound() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userController.getUserProgExercises(1L));
    }

    @Test
    void UserController_GetUserProgExercises_Success() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(user));

        List<ProgExerciseDto> userProgExercises = userController.getUserProgExercises(1L);

        Assertions.assertEquals(List.of(), userProgExercises);
    }

    @Test
    void UserController_ModifyUserIdentity_Success() {
        when(userService.changeIdentity(Mockito.any(Long.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDtoMapped);

        UserDto userDto = userController.modifyUserIdentity(createTestInputUserIdentity(1L));

        Assertions.assertEquals(this.userDtoMapped, userDto);
    }

    @Test
    void UserController_ModifyUserRoles_Success() {
        Set<RoleEntity> roles = new HashSet<RoleEntity>();
        when(roleService.findMany(Mockito.anySet())).thenReturn(roles);
        when(userService.changeRoles(Mockito.any(Long.class), Mockito.anySet())).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDtoMapped);

        UserDto userDto = userController.modifyUserRoles(createTestInputUserRoles(1L));

        Assertions.assertEquals(this.userDtoMapped, userDto);
    }

    @Test
    void UserController_modifyUserEmailQuery_Success() {
        JWToken jwToken = new JWToken("token", LocalDateTime.now());
        AuthDto returnValue = new AuthDto(jwToken, userDtoMapped);

        when(userService.changeEmail(Mockito.any(Long.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDtoMapped);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userAuthProvider.generateToken(Mockito.any())).thenReturn(jwToken);

        AuthDto authDto = userController.modifyUserEmail(createTestInputUserEmail(1L, "rawPassword"));

        Assertions.assertEquals(returnValue.getAccessToken(), authDto.getAccessToken());
        Assertions.assertEquals(returnValue.getUser(), authDto.getUser());
        Assertions.assertEquals(returnValue.getExpiration(), authDto.getExpiration());
    }

    @Test
    void UserController_ModifyUserUsername_Success() {
        when(userService.changeUsername(Mockito.any(Long.class), Mockito.any(String.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDtoMapped);

        UserDto userDto = userController.modifyUserUsername(createTestInputUserUsername(1L));

        Assertions.assertEquals(this.userDtoMapped, userDto);
    }

    @Test
    void UserController_ModifyUserPassword_Success() {
        when(userService.changePassword(Mockito.any(Long.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDtoMapped);

        UserDto userDto = userController.modifyUserPassword(createTestInputUserPassword(1L, "rawPassword"));

        Assertions.assertEquals(this.userDtoMapped, userDto);
    }

    @Test
    void UserController_DeleteUser_Success() {
        Long id = userController.deleteUser(1L);

        Assertions.assertEquals(1L, id);
    }
}