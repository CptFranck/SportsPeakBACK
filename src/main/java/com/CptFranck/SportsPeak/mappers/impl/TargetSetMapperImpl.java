package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.domain.input.duration.InputDuration;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.mappers.typeConverter.DurationToInputDurationConverter;
import com.CptFranck.SportsPeak.mappers.typeConverter.TargetSetStateToStringConverter;
import com.CptFranck.SportsPeak.mappers.typeConverter.WeightUnitToStringConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
public class TargetSetMapperImpl implements Mapper<TargetSetEntity, TargetSetDto> {

    private final ModelMapper modelMapper;

    public TargetSetMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.addConverter(new DurationToInputDurationConverter());
        this.modelMapper.addConverter(new WeightUnitToStringConverter());
        this.modelMapper.addConverter(new TargetSetStateToStringConverter());

        Converter<String, WeightUnit> toWeightUnit =
                ctx -> ctx.getSource() == null ? null : WeightUnit.valueOfLabel(ctx.getSource());
        Converter<String, TargetSetState> toTargetSetState =
                ctx -> ctx.getSource() == null ? null : TargetSetState.valueOfLabel(ctx.getSource());
        Converter<InputDuration, Duration> toInputDuration =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().InputDurationToDuration();

        this.modelMapper.createTypeMap(TargetSetDto.class, TargetSetEntity.class).addMappings(mapper -> {
            mapper.using(toWeightUnit).map(TargetSetDto::getWeightUnit, TargetSetEntity::setWeightUnit);
            mapper.using(toTargetSetState).map(TargetSetDto::getState, TargetSetEntity::setState);
            mapper.using(toInputDuration).map(TargetSetDto::getPhysicalExertionUnitTime, TargetSetEntity::setPhysicalExertionUnitTime);
            mapper.using(toInputDuration).map(TargetSetDto::getRestTime, TargetSetEntity::setRestTime);
        });
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
