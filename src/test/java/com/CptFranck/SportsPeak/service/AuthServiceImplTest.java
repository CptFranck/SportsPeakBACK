package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailExistsException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailUnknownException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.IncorrectPasswordException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UsernameExistsException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthServiceImpl authServiceImpl;

    private InputCredentials createTestInputCredentials() {
        return new InputCredentials(
                "test@test.test",
                "password"
        );
    }

    @Test
    void authService_Login_UnsuccessfulEmailUnknown() {
        InputCredentials credentials = createTestInputCredentials();

        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());

        assertThrows(EmailUnknownException.class, () -> authServiceImpl.login(credentials));
    }

    @Test
    void authService_Login_UnsuccessfulIncorrectPassword() {
        InputCredentials credentials = createTestInputCredentials();
        UserEntity user = createTestUser(1L);

        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> authServiceImpl.login(credentials));
    }

    @Test
    void authService_Login_Successful() {
        InputCredentials credentials = createTestInputCredentials();
        UserEntity user = createTestUser(1L);

        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(true);

        UserEntity returnedUser = authServiceImpl.login(credentials);

        Assertions.assertEquals(user, returnedUser);
    }

    @Test
    void authService_Register_UnsuccessfulEmailAlreadyUsed() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));

        Assertions.assertThrows(EmailExistsException.class, () -> authServiceImpl.register(user));
    }

    @Test
    void authService_Register_UnsuccessfulUsernameAlreadyUsed() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(user));

        Assertions.assertThrows(UsernameExistsException.class, () -> authServiceImpl.register(user));
    }

    @Test
    void authService_Register_Success() {
        UserEntity user = createTestUser(1L);

        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserEntity registeredUser = authServiceImpl.register(user);

        Assertions.assertEquals(user, registeredUser);
    }
}
