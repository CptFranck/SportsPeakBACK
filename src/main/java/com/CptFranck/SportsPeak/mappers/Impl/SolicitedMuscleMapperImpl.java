package com.CptFranck.SportsPeak.mappers.Impl;

import com.CptFranck.SportsPeak.domain.dto.SolicitedMuscleDto;
import com.CptFranck.SportsPeak.domain.entity.relationEntity.SolicitedMuscleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SolicitedMuscleMapperImpl implements Mapper<SolicitedMuscleEntity, SolicitedMuscleDto> {

    private final ModelMapper modelMapper;

    public SolicitedMuscleMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SolicitedMuscleDto mapTo(SolicitedMuscleEntity solicitedMuscleEntity) {
        return modelMapper.map(solicitedMuscleEntity, SolicitedMuscleDto.class);
    }

    @Override
    public SolicitedMuscleEntity mapFrom(SolicitedMuscleDto solicitedMuscleDto) {
        return modelMapper.map(solicitedMuscleDto, SolicitedMuscleEntity.class);
    }
}
