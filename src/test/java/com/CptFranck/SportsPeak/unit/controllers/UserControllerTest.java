package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.UserController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.*;
import com.CptFranck.SportsPeak.domain.model.UserTokens;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.resolver.UserInputResolver;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExerciseDto;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper;

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
        ReflectionTestUtils.setField(userController, "progExerciseMapper", progExerciseMapper);
        ReflectionTestUtils.setField(userController, "userMapper", userMapper);
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
    void getUserProgExercises_ValidInput_ReturnListOfProgExerciseDto() {
        ExerciseEntity exercise = createTestExercise(1L);
        ExerciseDto exerciseDto = createTestExerciseDto(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(null, user, exercise);
        ProgExerciseDto progExerciseDto = createTestProgExerciseDto(null, userDto, exerciseDto);
        user.getSubscribedProgExercises().add(progExercise);
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(progExerciseMapper.mapTo(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExerciseDto);

        List<ProgExerciseDto> userProgExercises = userController.getUserProgExercises(1L);

        Assertions.assertEquals(List.of(progExerciseDto), userProgExercises);
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
    void modifyUserEmail_ValidInput_ReturnAuthDto() {
        UserTokens userToken = new UserTokens(user, "accessToken", "refreshToken");
        AuthDto returnValue = new AuthDto("accessToken", userDto);

        when(authService.updateEmail(Mockito.any(InputUserEmail.class))).thenReturn(userToken);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        AuthDto authDto = userController.modifyUserEmail(createTestInputUserEmail(1L, "rawPassword"));

        Assertions.assertEquals(returnValue.getAccessToken(), authDto.getAccessToken());
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
    void modifyUserPassword_ValidInput_ReturnAuthDto() {
        UserTokens userToken = new UserTokens(user, "accessToken", "refreshToken");
        AuthDto returnValue = new AuthDto("accessToken", userDto);

        when(authService.updatePassword(Mockito.any(InputUserPassword.class))).thenReturn(userToken);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        AuthDto authDto = userController.modifyUserPassword(createTestInputUserPassword(1L, "rawPassword"));

        Assertions.assertEquals(returnValue.getAccessToken(), authDto.getAccessToken());
        Assertions.assertEquals(returnValue.getUser(), authDto.getUser());
    }

    @Test
    void deleteUser_ValidInput_ReturnUserId() {
        Long id = userController.deleteUser(1L);

        Assertions.assertEquals(1L, id);
    }
}