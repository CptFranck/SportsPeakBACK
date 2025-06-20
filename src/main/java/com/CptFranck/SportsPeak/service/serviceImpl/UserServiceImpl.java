package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailAlreadyUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UsernameExistsException;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> findAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public UserEntity findOne(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Set<UserEntity> findMany(Set<Long> ids) {
        return new HashSet<>(userRepository.findAllById(ids));
    }

    @Override
    public Set<UserEntity> findUserBySubscribedProgExercises(ProgExerciseEntity progExercise) {
        return new HashSet<>(userRepository.findAllBySubscribedProgExercisesContaining(progExercise));
    }

    @Override
    public UserEntity findByLogin(String login) {
        return userRepository.findByEmail(login)
                .or(() -> userRepository.findByUsername(login))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + login));
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
            save(u);
        });

        this.findMany(newIds).forEach(u -> {
            u.getRoles().add(roleEntity);
            save(u);
        });
    }

    @Override
    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }
}
