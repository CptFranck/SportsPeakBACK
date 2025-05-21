package com.CptFranck.SportsPeak.mapper.impl;

import com.CptFranck.SportsPeak.domain.dto.ExerciseTypeDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ExerciseTypeMapperImpl implements Mapper<ExerciseTypeEntity, ExerciseTypeDto> {

    private final ModelMapper modelMapper;

    public ExerciseTypeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ExerciseTypeDto mapTo(ExerciseTypeEntity exerciseTypeEntity) {
        return modelMapper.map(exerciseTypeEntity, ExerciseTypeDto.class);
    }

    @Override
    public ExerciseTypeEntity mapFrom(ExerciseTypeDto exerciseTypeDto) {
        return modelMapper.map(exerciseTypeDto, ExerciseTypeEntity.class);
    }
}
