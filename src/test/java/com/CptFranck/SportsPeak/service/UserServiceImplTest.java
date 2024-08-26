package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.*;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    void UserService_Save_Success() {
        UserEntity user = createTestUser(null);
        UserEntity userSavedInRepository = createTestUser(1L);
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(userSavedInRepository);

        UserEntity userSaved = userServiceImpl.save(user);

        Assertions.assertEquals(userSavedInRepository, userSaved);
    }

    @Test
    void UserService_FindAll_Success() {
        List<UserEntity> userList = createTestUserList();
        when(userRepository.findAll()).thenReturn(userList);

        List<UserEntity> userFound = userServiceImpl.findAll();

        Assertions.assertEquals(userList, userFound);
    }

    @Test
    void UserService_FindOne_Success() {
        UserEntity user = createTestUser(1L);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));

        Optional<UserEntity> userFound = userServiceImpl.findOne(user.getId());

        Assertions.assertTrue(userFound.isPresent());
        Assertions.assertEquals(user, userFound.get());
    }

    @Test
    void UserService_FindMany_Success() {
        List<UserEntity> userList = createTestUserList();
        Set<Long> userIds = userList.stream().map(UserEntity::getId).collect(Collectors.toSet());
        when(userRepository.findAllById(Mockito.anyIterable())).thenReturn(userList);

        Set<UserEntity> userFound = userServiceImpl.findMany(userIds);

        Assertions.assertEquals(new HashSet<>(userList), userFound);
    }

    @Test
    void exerciseService_UpdateRoleRelation_Success() {
        UserEntity userOne = createTestUser(1L);
        UserEntity userTwo = createTestUser(2L);
        Set<Long> oldUserIds = new HashSet<>();
        Set<Long> newUserIds = new HashSet<>();
        oldUserIds.add(userOne.getId());
        newUserIds.add(userTwo.getId());

        RoleEntity role = createTestRole(1L, 0);

        when(userRepository.findAllById(oldUserIds)).thenReturn(List.of(userOne));
        when(userRepository.findAllById(newUserIds)).thenReturn(List.of(userTwo));

        assertAll(() -> userServiceImpl.updateRoleRelation(newUserIds, oldUserIds, role));
    }

    @Test
    void UserService_Exists_Success() {
        UserEntity user = createTestUser(1L);
        when(userRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean userFound = userServiceImpl.exists(user.getId());

        Assertions.assertTrue(userFound);
    }

    @Test
    void UserService_Delete_Success() {
        UserEntity user = createTestUser(1L);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));

        assertAll(() -> userServiceImpl.delete(user.getId()));
    }

    @Test
    void UserService_Delete_Unsuccessful() {
        UserEntity user = createTestUser(1L);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.delete(user.getId()));
    }

    @Test
    void UserService_ChangeIdentity_Unsuccessful() {
        UserEntity user = createTestUser(1L);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(EmailUnknownException.class,
                () -> userServiceImpl.changeIdentity(user.getId(), "firstName", "lastName"));
    }

    @Test
    void UserService_ChangeIdentity_Successful() {
        UserEntity user = createTestUser(1L);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserEntity userSaved = userServiceImpl.changeIdentity(user.getId(), "firstName", "lastName");

        Assertions.assertEquals(userSaved, user);
    }

    @Test
    void UserService_ChangeRoles_Unsuccessful() {
        UserEntity user = createTestUser(1L);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(EmailUnknownException.class,
                () -> userServiceImpl.changeRoles(user.getId(), new HashSet<RoleEntity>()));
    }

    @Test
    void UserService_ChangeRoles_Successful() {
        UserEntity user = createTestUser(1L);
        Set<RoleEntity> oldRoles = new HashSet<RoleEntity>();
        Set<RoleEntity> newRoles = new HashSet<RoleEntity>();
        oldRoles.add(createTestRole(1L, 1));
        newRoles.add(createTestRole(1L, 2));
        user.getRoles().addAll(oldRoles);

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserEntity savedUser = userServiceImpl.changeRoles(user.getId(), newRoles);

        Assertions.assertEquals(user, savedUser);
    }

    @Test
    void UserService_ChangeEmail_UnsuccessfulUserNotFound() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.changeEmail(user.getId(), "password", "newEmail"));
    }

    @Test
    void UserService_ChangeEmail_UnsuccessfulEmailAlreadyUsed() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(true);
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));

        assertThrows(EmailExistsException.class,
                () -> userServiceImpl.changeEmail(user.getId(), "password", "newEmail"));
    }

    @Test
    void UserService_ChangeEmail_UnsuccessfulIncorrectPassword() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(false);

        assertThrows(IncorrectPasswordException.class,
                () -> userServiceImpl.changeEmail(user.getId(), "password", "newEmail"));
    }

    @Test
    void UserService_ChangeEmail_Successful() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(true);
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserEntity userSaved = userServiceImpl.changeEmail(user.getId(), "password", "newEmail");
        Assertions.assertEquals(user, userSaved);
    }

    @Test
    void UserService_ChangeUsername_UnsuccessfulUserNotFound() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.changeUsername(user.getId(), "newUsername"));
    }

    @Test
    void UserService_ChangeUsername_UnsuccessfulUsernameAlreadyUsed() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(user));

        assertThrows(UsernameExistsException.class,
                () -> userServiceImpl.changeUsername(user.getId(), "newUsername"));
    }

    @Test
    void UserService_ChangeUsername_Successful() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserEntity userSaved = userServiceImpl.changeUsername(user.getId(), "newUsername");
        Assertions.assertEquals(userSaved, user);
    }

    @Test
    void UserService_ChangePassword_UnsuccessfulUserNotFound() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.changePassword(user.getId(), "oldPassword", "newPassword"));
    }

    @Test
    void UserService_ChangePassword_UnsuccessfulIncorrectPassword() {
        UserEntity user = createTestUser(1L);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(false);

        assertThrows(IncorrectPasswordException.class,
                () -> userServiceImpl.changePassword(user.getId(), "oldPassword", "newPassword"));
    }

    @Test
    void UserService_ChangePassword_Successful() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserEntity userSaved = userServiceImpl.changeUsername(user.getId(), "newUsername");

        Assertions.assertEquals(user, userSaved);
    }

    @Test
    void UserService_LoadUserByUsername_UnsuccessfulUsernameNotFound() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userServiceImpl.changePassword(user.getId(), "oldPassword", "newPassword"));
    }

    @Test
    void UserService_LoadUserByUsername_Successful() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));

        UserDetails userSaved = userServiceImpl.loadUserByUsername(user.getUsername());

        Assertions.assertEquals(user.getEmail(), userSaved.getUsername());
    }
}
