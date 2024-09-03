package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.controller.graphqlQuery.UserQuery.getUserByIdQuery;
import static com.CptFranck.SportsPeak.controller.graphqlQuery.UserQuery.getUsersQuery;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;
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

//    @Test
//    void UserController_AddUser_UnsuccessfulUserNotFound() {
//        variables.put("inputNewUser", objectMapper.convertValue(
//                        createTestInputNewUser(1L, 1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(addUserQuery, "data.addUser", variables));
//    }
//
//    @Test
//    void UserController_AddUser_UnsuccessfulExerciseNotFound() {
//        variables.put("inputNewUser", objectMapper.convertValue(
//                        createTestInputNewUser(1L, 1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(user));
//        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(addUserQuery, "data.addUser", variables));
//    }
//
//    @Test
//    void UserController_AddUser_Success() {
//        variables.put("inputNewUser", objectMapper.convertValue(
//                        createTestInputNewUser(1L, 1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(userService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(user));
//        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
//        when(userAuthProvider.save(Mockito.any(UserEntity.class))).thenReturn(progExercise);
//        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);
//
//        LinkedHashMap<String, Object> userDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(addUserQuery, "data.addUser", variables);
//
//        Assertions.assertNotNull(userDto);
//    }
//
//    @Test
//    void UserController_ModifyUser_UnsuccessfulUserNotFound() {
//        variables.put("inputUser", objectMapper.convertValue(
//                        createTestInputUser(1L, 1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(userAuthProvider.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserQuery, "data.modifyUser", variables));
//    }
//
//    @Test
//    void UserController_ModifyUser_Unsuccessful() {
//        variables.put("inputUser", objectMapper.convertValue(
//                        createTestInputUser(1L, 1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(userAuthProvider.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
//        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserQuery, "data.modifyUser", variables));
//    }
//
//    @Test
//    void UserController_ModifyUser_Success() {
//        variables.put("inputUser", objectMapper.convertValue(
//                        createTestInputUser(1L, 1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(userAuthProvider.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
//        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
//        when(userAuthProvider.save(Mockito.any(UserEntity.class))).thenReturn(progExercise);
//        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);
//
//        LinkedHashMap<String, Object> userDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(modifyUserQuery, "data.modifyUser", variables);
//
//        Assertions.assertNotNull(userDto);
//    }
//
//    @Test
//    void UserController_ModifyUserTrustLabel_UnsuccessfulUserNotFound() {
//        variables.put("inputUserTrustLabel", objectMapper.convertValue(
//                        createTestInputUserTrustLabel(1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(userAuthProvider.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyUserTrustLabelQuery, "data.modifyUserTrustLabel", variables));
//    }
//
//    @Test
//    void UserController_ModifyUserTrustLabel_Success() {
//        variables.put("inputUserTrustLabel", objectMapper.convertValue(
//                        createTestInputUserTrustLabel(1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(userAuthProvider.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(progExercise));
//        when(userAuthProvider.save(Mockito.any(UserEntity.class))).thenReturn(progExercise);
//        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);
//
//        LinkedHashMap<String, Object> userDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(modifyUserTrustLabelQuery, "data.modifyUserTrustLabel", variables);
//
//        Assertions.assertNotNull(userDto);
//    }
//
//    @Test
//    void UserController_DeleteUser_Unsuccessful() {
//        variables.put("progExerciseId", 1);
//        when(userAuthProvider.exists(Mockito.any(Long.class))).thenReturn(false);
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(deleteUserQuery, "data.deleteUser", variables);
//
//        Assertions.assertNull(id);
//    }
//
//    @Test
//    void UserController_DeleteUser_Success() {
//        variables.put("progExerciseId", 1);
//        when(userAuthProvider.exists(Mockito.any(Long.class))).thenReturn(true);
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(deleteUserQuery, "data.deleteUser", variables);
//
//        Assertions.assertNotNull(id);
//    }
}