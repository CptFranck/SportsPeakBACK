package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.*;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserEntity> findAll() {
        return StreamSupport.stream(userRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserEntity> findOne(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Set<UserEntity> findMany(Set<Long> ids) {
        return StreamSupport.stream(userRepository
                                .findAllById(ids)
                                .spliterator(),
                        false)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UserEntity> findUserBySubscribedProgExercises(ProgExerciseEntity progExercise) {
        return new HashSet<>(userRepository.findAllBySubscribedProgExercisesContaining(progExercise));
    }
    
    @Override
    public void updateRoleRelation(Set<Long> newIds, Set<Long> oldIds, RoleEntity roleEntity) {
        this.findMany(oldIds).forEach(u -> {
            u.getRoles().removeIf(r -> Objects.equals(r.getId(), roleEntity.getId()));
            userRepository.save(u);
        });

        this.findMany(newIds).forEach(u -> {
            u.getRoles().add(roleEntity);
            userRepository.save(u);
        });
    }

    @Override
    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        UserEntity exercise = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(exercise);
    }

    @Override
    public UserEntity changeIdentity(Long id, String firstName, String lastName) {
        UserEntity user = userRepository.findById(id).orElseThrow(EmailUnknownException::new);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    @Override
    public UserEntity changeRoles(Long id, Set<RoleEntity> roles) {
        UserEntity user = userRepository.findById(id).orElseThrow(EmailUnknownException::new);

        user.getRoles().forEach(role -> {
            if (!roles.contains(role)) {
                user.getRoles().remove(role);
            }
        });
        user.getRoles().addAll(roles);
        return userRepository.save(user);
    }

    @Override
    public UserEntity changeEmail(Long id, String password, String newEmail) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (passwordEncoder.matches(password, user.getPassword())) {
            Optional<UserEntity> userOptionalEmail = userRepository.findByEmail(newEmail);
            if (userOptionalEmail.isPresent()) {
                throw new EmailExistsException();
            }
            user.setEmail(newEmail);
        } else {
            throw new IncorrectPasswordException();
        }
        return userRepository.save(user);
    }

    @Override
    public UserEntity changeUsername(Long id, String newUsername) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Optional<UserEntity> userOptionalUsername = userRepository.findByUsername(newUsername);
        if (userOptionalUsername.isPresent()) {
            throw new UsernameExistsException();
        }
        user.setUsername(newUsername);
        return userRepository.save(user);
    }

    @Override
    public UserEntity changePassword(Long id, String oldPassword, String newPassword) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User name not found in database")
        );
        return new User(user.getEmail(), user.getPassword(), getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<RoleEntity> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<RoleEntity> roles) {
        List<String> privileges = new ArrayList<>();
        List<PrivilegeEntity> collection = new ArrayList<>();
        for (RoleEntity role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (PrivilegeEntity item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
