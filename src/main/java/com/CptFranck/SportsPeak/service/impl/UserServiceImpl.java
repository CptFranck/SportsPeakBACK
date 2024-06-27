package com.CptFranck.SportsPeak.service.impl;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
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
        return ids.stream()
                .map(this::findOne)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateRoleRelation(Set<Long> newIds, Set<Long> oldIds, RoleEntity roleEntity) {
        this.findMany(oldIds).forEach(p -> {
            p.getRoles().removeIf(et -> Objects.equals(et.getId(), roleEntity.getId()));
            userRepository.save(p);
        });

        this.findMany(newIds).forEach(p -> {
            p.getRoles().add(roleEntity);
            userRepository.save(p);
        });
    }

    @Override
    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity changeIdentity(Long id, String firstName, String lastName) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(EmailUnknownException::new);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    @Override
    public UserEntity changeRoles(Long id, Collection<RoleEntity> roles) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(EmailUnknownException::new);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public UserEntity changeEmail(Long id, String password, String newEmail) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
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
        UserEntity user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        Optional<UserEntity> userOptionalUsername = userRepository.findByUsername(newUsername);
        if (userOptionalUsername.isPresent()) {
            throw new UsernameExistsException();
        }
        user.setUsername(newUsername);
        return userRepository.save(user);
    }

    @Override
    public UserEntity changePassword(Long id, String oldPassword, String newPassword) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
        } else {
            throw new IncorrectPasswordException();
        }
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() ->
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
