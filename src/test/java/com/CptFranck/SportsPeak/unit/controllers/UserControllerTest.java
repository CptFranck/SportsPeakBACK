package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.UserController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.*;
import com.CptFranck.SportsPeak.domain.model.UserToken;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolvers.UserInputResolver;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.UserManager;
import com.CptFranck.SportsPeak.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

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
    private UserInputResolver userInputResolver;

    @Mock
    private UserManager userManager;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    private UserEntity user;
    private UserDto userDto;

    @BeforeEach
    void init() {
        user = createTestUser(1L);
        userDto = createTestUserDto(1L);
    }

    @Test
    void getUsers_ValidUse_ReturnListOfUserDto() {
        when(userService.findAll()).thenReturn(List.of(user));
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        List<UserDto> userDtos = userController.getUsers();

        Assertions.assertEquals(List.of(this.userDto), userDtos);
    }

    @Test
    void getUserById_ValidInput_ReturnUserDto() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        UserDto userDto = userController.getUserById(1L);

        Assertions.assertEquals(this.userDto, userDto);
    }

    @Test
    void getUserProgExercises_ValidInput_ReturnProgExerciseDto() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);

        List<ProgExerciseDto> userProgExercises = userController.getUserProgExercises(1L);

        Assertions.assertEquals(List.of(), userProgExercises);
    }

    @Test
    void modifyUserIdentity_ValidInput_ReturnUserDto() {
        when(userInputResolver.resolveInput(Mockito.any(InputUserIdentity.class))).thenReturn(user);
        when(userService.save(Mockito.any(UserEntity.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        UserDto userDto = userController.modifyUserIdentity(createTestInputUserIdentity(1L));

        Assertions.assertEquals(this.userDto, userDto);
    }

    @Test
    void modifyUserRoles_ValidInput_ReturnUserDto() {
        when(userManager.updateUserRoles(Mockito.any(InputUserRoles.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        UserDto userDto = userController.modifyUserRoles(createTestInputUserRoles(1L));

        Assertions.assertEquals(this.userDto, userDto);
    }

    @Test
    void modifyUserEmail_ValidInput_ReturnUserDto() {
        UserToken userToken = new UserToken("token", user);
        AuthDto returnValue = new AuthDto("token", userDto);

        when(authService.updateEmail(Mockito.any(InputUserEmail.class))).thenReturn(userToken);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        AuthDto authDto = userController.modifyUserEmail(createTestInputUserEmail(1L, "rawPassword"));

        Assertions.assertEquals(returnValue.getToken(), authDto.getToken());
        Assertions.assertEquals(returnValue.getUser(), authDto.getUser());
    }

    @Test
    void modifyUserUsername_ValidInput_ReturnUserDto() {
        when(userInputResolver.resolveInput(Mockito.any(InputUserUsername.class))).thenReturn(user);
        when(userService.save(Mockito.any(UserEntity.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        UserDto userDto = userController.modifyUserUsername(createTestInputUserUsername(1L));

        Assertions.assertEquals(this.userDto, userDto);
    }

    @Test
    void modifyUserPassword_ValidInput_ReturnUserDto() {
        UserToken userToken = new UserToken("token", user);
        AuthDto returnValue = new AuthDto("token", userDto);

        when(authService.updatePassword(Mockito.any(InputUserPassword.class))).thenReturn(userToken);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        AuthDto authDto = userController.modifyUserPassword(createTestInputUserPassword(1L, "rawPassword"));

        Assertions.assertEquals(returnValue.getToken(), authDto.getToken());
        Assertions.assertEquals(returnValue.getUser(), authDto.getUser());
    }

    @Test
    void deleteUser_ValidInput_ReturnUserId() {
        Long id = userController.deleteUser(1L);

        Assertions.assertEquals(1L, id);
    }
}