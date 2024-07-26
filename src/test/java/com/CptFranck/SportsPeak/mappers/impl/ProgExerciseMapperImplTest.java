package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.*;

@ExtendWith(MockitoExtension.class)
public class ProgExerciseMapperImplTest {

    private final Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper;

    public ProgExerciseMapperImplTest() {
        this.progExerciseMapper = new ProgExerciseMapperImpl(new ModelMapper());
    }

    @Test
    void testProgExerciseTypeMapperMapTo_Success() {
        UserEntity user = createTestUser();
        ExerciseEntity exercise = createTestExercise();
        ProgExerciseEntity progExercise = createTestProgExercise(user, exercise);
        TargetSetEntity targetSet = createTestTargetSet(progExercise);

        progExercise.getTargetSets().add(targetSet);
        progExercise.getSubscribedUsers().add(user);

        ProgExerciseDto progExerciseDto = progExerciseMapper.mapTo(progExercise);

        Assertions.assertEquals(progExercise.getId(), progExerciseDto.getId());
        Assertions.assertEquals(progExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(progExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(progExercise.getVisibility().label, progExerciseDto.getVisibility());
        Assertions.assertEquals(progExercise.getTrustLabel().label, progExerciseDto.getTrustLabel());
        Assertions.assertEquals(progExercise.getCreator(), progExerciseDto.getCreator());
        Assertions.assertEquals(progExercise.getTargetSets().size(), progExerciseDto.getTargetSets().size());
        Assertions.assertArrayEquals(
                progExercise.getTargetSets().stream().map(TargetSetEntity::getId).toArray(),
                progExerciseDto.getTargetSets().stream().map(TargetSetDto::getId).toArray()
        );
    }
}
