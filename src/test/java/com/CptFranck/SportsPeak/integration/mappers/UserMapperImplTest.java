package com.CptFranck.SportsPeak.integration.mappers;

import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.mappers.impl.UserMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRoleDto;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;

@ExtendWith(MockitoExtension.class)
public class UserMapperImplTest {

    private final Mapper<UserEntity, UserDto> userMapper;

    public UserMapperImplTest() {
        this.userMapper = new UserMapperImpl(new ModelMapper());
    }

    @Test
    void userTypeMapper_MapTo_Success() {
        UserEntity user = createTestUser(1L);
        RoleEntity role = createTestRole(1L, 0);
        user.getRoles().add(role);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        user.getProgExercisesCreated().add(progExercise);
        user.getSubscribedProgExercises().add(progExercise);

        UserDto userDto = userMapper.mapTo(user);

        Assertions.assertEquals(user.getId(), userDto.getId());
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
        Assertions.assertEquals(user.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(user.getLastName(), userDto.getLastName());
        Assertions.assertEquals(user.getUsername(), userDto.getUsername());
        Assertions.assertEquals(user.getRoles().size(), userDto.getRoles().size());
        Assertions.assertArrayEquals(
                user.getRoles().stream().map(RoleEntity::getId).toArray(),
                userDto.getRoles().stream().map(RoleDto::getId).toArray()
        );

    }

    @Test
    void userTypeMapper_MapFrom_Success() {
        UserDto user = createTestUserDto(1L);
        RoleDto role = createTestRoleDto(1L);
        user.getRoles().add(role);
        UserEntity userEntity = userMapper.mapFrom(user);

        Assertions.assertEquals(user.getId(), userEntity.getId());
        Assertions.assertEquals(user.getEmail(), userEntity.getEmail());
        Assertions.assertEquals(user.getFirstName(), userEntity.getFirstName());
        Assertions.assertEquals(user.getLastName(), userEntity.getLastName());
        Assertions.assertEquals(user.getUsername(), userEntity.getUsername());
        Assertions.assertEquals(user.getRoles().size(), userEntity.getRoles().size());
        Assertions.assertArrayEquals(
                user.getRoles().stream().map(RoleDto::getId).toArray(),
                userEntity.getRoles().stream().map(RoleEntity::getId).toArray()
        );
    }
}
