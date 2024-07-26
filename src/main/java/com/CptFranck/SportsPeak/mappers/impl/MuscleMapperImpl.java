package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.MuscleDto;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MuscleMapperImpl implements Mapper<MuscleEntity, MuscleDto> {

    private final ModelMapper modelMapper;

    public MuscleMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MuscleDto mapTo(MuscleEntity muscleEntity) {
        return modelMapper.map(muscleEntity, MuscleDto.class);
    }

    @Override
    public MuscleEntity mapFrom(MuscleDto muscleDto) {
        return modelMapper.map(muscleDto, MuscleEntity.class);
    }
}
