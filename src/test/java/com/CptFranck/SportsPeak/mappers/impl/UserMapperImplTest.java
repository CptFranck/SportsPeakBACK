package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.*;

@ExtendWith(MockitoExtension.class)
public class UserMapperImplTest {

    private final Mapper<UserEntity, UserDto> userMapper;

    public UserMapperImplTest() {
        this.userMapper = new UserMapperImpl(new ModelMapper());
    }

    @Test
    void testExerciseTypeMapperMapTo_Success() {
        UserEntity user = createTestUser();
        RoleEntity role = createTestRoleEntity();
        user.getRoles().add(role);
        ExerciseEntity exercise = createTestExercise();
        ProgExerciseEntity progExercise = createTestProgExercise(user, exercise);
        user.getProgExercisesCreated().add(progExercise);
        user.getSubscribedProgExercises().add(progExercise);

        UserDto userDto = userMapper.mapTo(user);

        Assertions.assertEquals(user.getId(), userDto.getId());
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
        Assertions.assertEquals(user.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(user.getLastName(), userDto.getLastName());
        Assertions.assertEquals(user.getUsername(), userDto.getUsername());
        Assertions.assertEquals(user.getSubscribedProgExercises().size(), userDto.getSubscribedProgExercises().size());
        Assertions.assertEquals(user.getRoles().size(), userDto.getRoles().size());
        Assertions.assertArrayEquals(
                user.getRoles().stream().map(RoleEntity::getId).toArray(),
                userDto.getRoles().stream().map(RoleDto::getId).toArray()
        );
        Assertions.assertArrayEquals(
                user.getSubscribedProgExercises().stream().map(ProgExerciseEntity::getId).toArray(),
                userDto.getSubscribedProgExercises().stream().map(ProgExerciseDto::getId).toArray()
        );
        Assertions.assertEquals(user.getProgExercisesCreated().size(), userDto.getProgExercisesCreated().size());
        Assertions.assertArrayEquals(
                user.getProgExercisesCreated().stream().map(ProgExerciseEntity::getId).toArray(),
                userDto.getProgExercisesCreated().stream().map(ProgExerciseDto::getId).toArray()
        );
    }
}
