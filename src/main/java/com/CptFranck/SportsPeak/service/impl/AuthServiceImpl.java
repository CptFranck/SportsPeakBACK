package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity login(InputCredentials credentials) {
        UserEntity user = userRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new RuntimeException("User email unknown"));
        if (passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Invalid password");
    }

    @Override
    public UserEntity register(UserEntity user) {
        Optional<UserEntity> userOptionalEmail = userRepository.findByEmail(user.getEmail());
        Optional<UserEntity> userOptionalUsername = userRepository.findByUsername(user.getUsername());
        if (userOptionalEmail.isPresent()) {
            throw new RuntimeException("An user has already used this email");
        }
        if (userOptionalUsername.isPresent()) {
            throw new RuntimeException("An user has already used this username");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
