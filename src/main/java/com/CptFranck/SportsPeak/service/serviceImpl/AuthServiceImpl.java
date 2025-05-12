package com.CptFranck.SportsPeak.service.serviceImpl;

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
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.RoleService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(RoleService roleService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity login(InputCredentials credentials) {
        UserEntity user = userRepository.findByEmail(credentials.getEmail())
                .orElseThrow(EmailUnknownException::new);
        if (passwordEncoder.matches(credentials.getPassword(), user.getPassword()))
            return user;
        throw new IncorrectPasswordException();
    }

    @Override
    public UserEntity register(InputRegisterNewUser inputRegisterNewUser) {
        Optional<RoleEntity> userRole = roleService.findByName("ROLE_USER");
        Optional<UserEntity> userOptionalEmail = userRepository.findByEmail(inputRegisterNewUser.getEmail());
        Optional<UserEntity> userOptionalUsername = userRepository.findByUsername(inputRegisterNewUser.getUsername());

        if (userRole.isEmpty())
            throw new RoleNotFoundException("ROLE_USER");
        if (userOptionalEmail.isPresent())
            throw new EmailAlreadyUsedException();
        if (userOptionalUsername.isPresent())
            throw new UsernameExistsException();

        UserEntity userEntity = new UserEntity(
                null,
                inputRegisterNewUser.getEmail(),
                inputRegisterNewUser.getFirstName(),
                inputRegisterNewUser.getLastName(),
                inputRegisterNewUser.getUsername(),
                inputRegisterNewUser.getPassword(),
                Set.of(userRole.get()),
                new HashSet<>(),
                new HashSet<>()
        );

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }
}
