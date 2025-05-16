package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailAlreadyUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UsernameExistsException;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public UserEntity findOne(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
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
    public UserEntity findByLogin(String login) {
        return userRepository.findByEmail(login)
                .or(() -> userRepository.findByUsername(login))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + login));
    }

    @Override
    public UserEntity save(UserEntity user) {
        Optional<UserEntity> OptionalUserEmail = userRepository.findByEmail(user.getEmail());
        Optional<UserEntity> OptionalUserUsername = userRepository.findByUsername(user.getUsername());
        if (user.getId() == null) {
            if (OptionalUserEmail.isPresent())
                throw new EmailAlreadyUsedException();
            if (OptionalUserUsername.isPresent())
                throw new UsernameExistsException();
            return userRepository.save(user);
        }
        if (exists(user.getId())) {
            if (OptionalUserEmail.isPresent() &&
                    !Objects.equals(OptionalUserEmail.get().getId(), user.getId()))
                throw new EmailAlreadyUsedException();
            if (OptionalUserUsername.isPresent() &&
                    !Objects.equals(OptionalUserUsername.get().getId(), user.getId()))
                throw new UsernameExistsException();
            return userRepository.save(user);
        }
        throw new UserNotFoundException(user.getId());
    }

    @Override
    public void delete(Long id) {
        UserEntity exercise = this.findOne(id);
        userRepository.delete(exercise);
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
    public UserEntity changeIdentity(Long id, String firstName, String lastName) {
        UserEntity user = this.findOne(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    @Override
    public UserEntity changeRoles(Long id, Set<RoleEntity> roles) {
        UserEntity user = this.findOne(id);
        user.getRoles().forEach(role -> {
            if (!roles.contains(role)) user.getRoles().remove(role);
        });
        user.getRoles().addAll(roles);
        return userRepository.save(user);
    }

    @Override
    public UserEntity changeUsername(Long id, String newUsername) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Optional<UserEntity> userOptionalUsername = userRepository.findByUsername(newUsername);
        if (userOptionalUsername.isPresent())
            throw new UsernameExistsException();
        user.setUsername(newUsername);
        return userRepository.save(user);
    }

    @Override
    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }
}
