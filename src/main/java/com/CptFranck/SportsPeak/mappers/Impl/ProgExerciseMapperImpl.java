package com.CptFranck.SportsPeak.mappers.Impl;

import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProgExerciseMapperImpl implements Mapper<ProgExerciseEntity, ProgExerciseDto> {

    private final ModelMapper modelMapper;

    public ProgExerciseMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProgExerciseDto mapTo(ProgExerciseEntity progExerciseEntity) {
        return modelMapper.map(progExerciseEntity, ProgExerciseDto.class);
    }

    @Override
    public ProgExerciseEntity mapFrom(ProgExerciseDto progExerciseDto) {
        return modelMapper.map(progExerciseDto, ProgExerciseEntity.class);
    }
}
