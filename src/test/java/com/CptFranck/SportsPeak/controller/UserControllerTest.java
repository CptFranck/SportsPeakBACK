package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.model.JWToken;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
import java.util.*;

import static com.CptFranck.SportsPeak.controller.graphqlQuery.UserQuery.*;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        UserController.class
})
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    private Mapper<UserEntity, UserDto> userMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private JwtProvider userAuthProvider;

    @MockBean
    private AuthenticationManager authenticationManager;

    private UserEntity user;
    private UserDto userDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        user = createTestUser(1L);
        userDto = createTestUserDto(1L);
        variables = new LinkedHashMap<>();
    }

    @Test
    void UserController_GetUsers_Success() {
        when(userService.findAll()).thenReturn(List.of(user));
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        List<LinkedHashMap<String, Object>> userDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(getUsersQuery, "data.getUsers");

        Assertions.assertNotNull(userDtos);
    }

    @Test
    void UserController_GetUserById_Unsuccessful() {
        variables.put("id", 1);
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        LinkedHashMap<String, Object> userDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getUserByIdQuery, "data.getUserById", variables);

        Assertions.assertNull(userDto);
    }

    @Test
    void UserController_GetUserById_Success() {
        variables.put("id", 1);
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        LinkedHashMap<String, Object> userDto =
                dgsQueryExecutor.executeAndExtractJsonPath(getUserByIdQuery, "data.getUserById", variables);

        Assertions.assertNotNull(userDto);
    }

    @Test
    void UserController_ModifyUserIdentity_Success() {
        variables.put("inputUserIdentity", objectMapper.convertValue(
                        createTestInputUserIdentity(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(userService.changeIdentity(Mockito.any(Long.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        LinkedHashMap<String, Object> userDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyUserIdentityQuery, "data.modifyUserIdentity", variables);

        Assertions.assertNotNull(userDto);
    }

    @Test
    void UserController_ModifyUserRoles_Success() {
        variables.put("inputUserRoles", objectMapper.convertValue(
                        createTestInputUserRoles(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        Set<RoleEntity> roles = new HashSet<RoleEntity>();
        when(roleService.findMany(Mockito.anySet())).thenReturn(roles);
        when(userService.changeRoles(Mockito.any(Long.class), Mockito.anySet())).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        LinkedHashMap<String, Object> userDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyUserRolesQuery, "data.modifyUserRoles", variables);

        Assertions.assertNotNull(userDto);
    }

    @Test
    void UserController_modifyUserEmailQuery_Success() {
        variables.put("inputUserEmail", objectMapper.convertValue(
                        createTestInputUserEmail(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        JWToken jwToken = new JWToken("token", LocalDateTime.now());
        when(userService.changeEmail(Mockito.any(Long.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userAuthProvider.generateToken(Mockito.any())).thenReturn(jwToken);

        LinkedHashMap<String, Object> userDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyUserEmailQuery, "data.modifyUserEmail", variables);

        Assertions.assertNotNull(userDto);
    }

    @Test
    void UserController_ModifyUserUsername_Success() {
        variables.put("inputUserUsername", objectMapper.convertValue(
                        createTestInputUserUsername(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(userService.changeUsername(Mockito.any(Long.class), Mockito.any(String.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        LinkedHashMap<String, Object> userDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyUserUsernameQuery, "data.modifyUserUsername", variables);

        Assertions.assertNotNull(userDto);
    }

    @Test
    void UserController_ModifyUserPassword_Success() {
        variables.put("inputUserPassword", objectMapper.convertValue(
                        createTestInputUserPassword(1L),
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }
                )
        );
        when(userService.changePassword(Mockito.any(Long.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        LinkedHashMap<String, Object> userDto =
                dgsQueryExecutor.executeAndExtractJsonPath(modifyUserPasswordQuery, "data.modifyUserPassword", variables);

        Assertions.assertNotNull(userDto);
    }

    @Test
    void UserController_DeleteUser_Unsuccessful() {
        variables.put("userId", 1);
        when(userService.exists(Mockito.any(Long.class))).thenReturn(false);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteUserQuery, "data.deleteUser", variables);

        Assertions.assertNull(id);
    }

    @Test
    void UserController_DeleteUser_Success() {
        variables.put("userId", 1);
        when(userService.exists(Mockito.any(Long.class))).thenReturn(true);

        Integer id =
                dgsQueryExecutor.executeAndExtractJsonPath(deleteUserQuery, "data.deleteUser", variables);

        Assertions.assertNotNull(id);
    }
}