package com.CptFranck.SportsPeak.integration.resolvers;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.input.user.InputUserIdentity;
import com.CptFranck.SportsPeak.domain.input.user.InputUserUsername;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.resolvers.UserInputResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.TestUserUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class UserInputResolverIT {

    @Autowired
    private UserInputResolver userInputResolver;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(createTestUser(null));
    }

    @AfterEach
    void afterEach() {
        userRepository.delete(user);
    }

    @Test
    void resolveInput_InvalidUserIdInInputUserUsername_ThrowUserNotFoundException() {
        InputUserUsername inputUserUsername = createTestInputUserUsername(user.getId());
        userRepository.delete(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> userInputResolver.resolveInput(inputUserUsername));
    }

    @Test
    void resolveInput_ValidInputUserUsername_ReturnMuscleEntity() {
        InputUserUsername inputUserUsername = createTestInputUserUsername(user.getId());

        UserEntity userResolved = userInputResolver.resolveInput(inputUserUsername);

        Assertions.assertEquals(inputUserUsername.getNewUsername(), userResolved.getUsername());
    }

    @Test
    void resolveInput_InvalidUserIdInputUserIdentity_ThrowUserNotFoundException() {
        InputUserIdentity inputUserIdentity = createTestInputUserIdentity(user.getId());
        userRepository.delete(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> userInputResolver.resolveInput(inputUserIdentity));
    }

    @Test
    void resolveInput_ValidInputUserIdentity_ReturnMuscleEntity() {
        InputUserIdentity inputUserIdentity = createTestInputUserIdentity(user.getId());

        UserEntity userResolved = userInputResolver.resolveInput(inputUserIdentity);

        Assertions.assertEquals(inputUserIdentity.getFirstName(), userResolved.getFirstName());
        Assertions.assertEquals(inputUserIdentity.getLastName(), userResolved.getLastName());
    }
}
