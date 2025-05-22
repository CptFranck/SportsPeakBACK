package com.CptFranck.SportsPeak.integration.services.services;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.model.CustomUserDetails;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.CustomUserDetailsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class CustomUserDetailsServiceIT {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(createTestUser(null));
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void loadUserByUsername_ValidUse_ReturnListOfUserEntity() {
        userRepository.delete(user);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(user.getUsername()));
    }

    @Test
    void loadUserByUsername_ValidUseWithEmail_ReturnListOfUserEntity() {
        UserDetails userFound = customUserDetailsService.loadUserByUsername(user.getEmail());

        UserDetails userDetails = new CustomUserDetails(user);
        Assertions.assertEquals(userDetails.getUsername(), userFound.getUsername());
    }

    @Test
    void loadUserByUsername_ValidUseWithUsername_ReturnListOfUserEntity() {
        UserDetails userFound = customUserDetailsService.loadUserByUsername(user.getUsername());

        UserDetails userDetails = new CustomUserDetails(user);
        Assertions.assertEquals(userDetails.getUsername(), userFound.getUsername());
    }
}
