package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ExerciseMapperImpl implements Mapper<ExerciseEntity, ExerciseDto> {

    private final ModelMapper modelMapper;

    public ExerciseMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ExerciseDto mapTo(ExerciseEntity exerciseEntity) {
        return modelMapper.map(exerciseEntity, ExerciseDto.class);
    }

    @Override
    public ExerciseEntity mapFrom(ExerciseDto exerciseDto) {
        return modelMapper.map(exerciseDto, ExerciseEntity.class);
    }
}
