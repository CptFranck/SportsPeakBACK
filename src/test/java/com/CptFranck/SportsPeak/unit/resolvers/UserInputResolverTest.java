package com.CptFranck.SportsPeak.unit.resolvers;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.InputUserIdentity;
import com.CptFranck.SportsPeak.domain.input.user.InputUserUsername;
import com.CptFranck.SportsPeak.resolver.UserInputResolver;
import com.CptFranck.SportsPeak.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.CptFranck.SportsPeak.utils.TestUserUtils.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserInputResolverTest {

    @InjectMocks
    private UserInputResolver userInputResolver;

    @Mock
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
    }


    @Test
    void resolveInput_ValidInputUserUsername_ReturnMuscleEntity() {
        InputUserUsername inputUserUsername = createTestInputUserUsername(user.getId());
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);


        UserEntity userResolved = userInputResolver.resolveInput(inputUserUsername);

        Assertions.assertEquals(inputUserUsername.getNewUsername(), userResolved.getUsername());
    }

    @Test
    void resolveInput_ValidInputUserIdentity_ReturnMuscleEntity() {
        InputUserIdentity inputUserIdentity = createTestInputUserIdentity(user.getId());
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);


        UserEntity userResolved = userInputResolver.resolveInput(inputUserIdentity);

        Assertions.assertEquals(inputUserIdentity.getFirstName(), userResolved.getFirstName());
        Assertions.assertEquals(inputUserIdentity.getLastName(), userResolved.getLastName());
    }
}
