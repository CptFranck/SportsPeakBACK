package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.mappers.typeConverter.DurationToInputDurationConverter;
import com.CptFranck.SportsPeak.mappers.typeConverter.TargetSetStateToStringConverter;
import com.CptFranck.SportsPeak.mappers.typeConverter.WeightUnitToStringConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TargetSetMapperImpl implements Mapper<TargetSetEntity, TargetSetDto> {

    private final ModelMapper modelMapper;

    public TargetSetMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.addConverter(new DurationToInputDurationConverter());
        this.modelMapper.addConverter(new WeightUnitToStringConverter());
        this.modelMapper.addConverter(new TargetSetStateToStringConverter());
    }

    @Override
    public TargetSetDto mapTo(TargetSetEntity targetSetEntity) {
        return modelMapper.map(targetSetEntity, TargetSetDto.class);
    }

    @Override
    public TargetSetEntity mapFrom(TargetSetDto targetSetDto) {
        return modelMapper.map(targetSetDto, TargetSetEntity.class);
    }
}
