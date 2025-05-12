package com.CptFranck.SportsPeak.unit.services;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailAlreadyUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailUnknownException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.IncorrectPasswordException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UsernameExistsException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.serviceImpl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.CptFranck.SportsPeak.utils.AuthUtils.createInputCredentials;
import static com.CptFranck.SportsPeak.utils.AuthUtils.createInputRegisterNewUser;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private RoleService roleService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private RoleEntity role;
    private UserEntity user;
    private InputCredentials inputCredentials;
    private InputRegisterNewUser inputRegisterNewUser;

    @BeforeEach
    void setUp() {
        role = createTestRole(1L, 0);
        user = createTestUser(1L);
        inputCredentials = createInputCredentials(user);
        inputRegisterNewUser = createInputRegisterNewUser(user);
    }

    @Test
    void login_EmailNotFound_ThrowEmailUnknownException() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());

        assertThrows(EmailUnknownException.class, () -> authServiceImpl.login(inputCredentials));
    }

    @Test
    void login_IncorrectPassword_ThrowIncorrectPasswordException() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> authServiceImpl.login(inputCredentials));
    }

    @Test
    void login_CorrectCredentials_ReturnUserEntity() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(true);

        UserEntity returnedUser = authServiceImpl.login(inputCredentials);

        Assertions.assertEquals(user, returnedUser);
    }

    @Test
    void register_MissingUserRoleInDB_ThrowRoleNotFoundException() {
        when(roleService.findByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));

        Assertions.assertThrows(RoleNotFoundException.class, () -> authServiceImpl.register(inputRegisterNewUser));
    }

    @Test
    void register_UserEmailAlreadyTaken_ThrowEmailAlreadyUsedException() {
        when(roleService.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));

        Assertions.assertThrows(EmailAlreadyUsedException.class, () -> authServiceImpl.register(inputRegisterNewUser));
    }

    @Test
    void register_UserUsernameAlreadyUsed_ThrowUsernameExistsException() {
        when(roleService.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(user));

        Assertions.assertThrows(UsernameExistsException.class, () -> authServiceImpl.register(inputRegisterNewUser));
    }

    @Test
    void register_CorrectCredentials_ReturnUserEntity() {
        when(roleService.findByName(Mockito.any(String.class))).thenReturn(Optional.of(role));
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserEntity registeredUser = authServiceImpl.register(inputRegisterNewUser);

        Assertions.assertEquals(user, registeredUser);
    }
}
