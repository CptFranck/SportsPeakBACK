package com.CptFranck.SportsPeak.unit.services.services;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.model.CustomUserDetails;
import com.CptFranck.SportsPeak.service.UserService;
import com.CptFranck.SportsPeak.service.serviceImpl.CustomUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserService userService;

    private UserEntity user;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
        userDetails = new CustomUserDetails(user);
    }

    @Test
    void loadUserByUsername_ValidUse_ReturnListOfUserEntity() {
        when(userService.findByLogin(Mockito.any(String.class))).thenReturn(user);

        UserDetails userFound = customUserDetailsService.loadUserByUsername(user.getUsername());

        Assertions.assertEquals(userDetails.getUsername(), userFound.getUsername());
    }
}
