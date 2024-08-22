package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.PerformanceLogDto;
import com.CptFranck.SportsPeak.domain.entity.PerformanceLogEntity;
import com.CptFranck.SportsPeak.domain.enumType.WeightUnit;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.mappers.typeConverter.WeightUnitToStringConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PerformanceLogMapperImpl implements Mapper<PerformanceLogEntity, PerformanceLogDto> {

    private final ModelMapper modelMapper;

    public PerformanceLogMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.addConverter(new WeightUnitToStringConverter());

        Converter<String, WeightUnit> toWeightUnit =
                ctx -> ctx.getSource() == null ? null : WeightUnit.valueOfLabel(ctx.getSource());

        this.modelMapper.createTypeMap(PerformanceLogDto.class, PerformanceLogEntity.class).addMappings(mapper -> {
            mapper.using(toWeightUnit).map(PerformanceLogDto::getWeightUnit, PerformanceLogEntity::setWeightUnit);
        });
    }

    @Override
    public PerformanceLogDto mapTo(PerformanceLogEntity performanceLogEntity) {
        return modelMapper.map(performanceLogEntity, PerformanceLogDto.class);
    }

    @Override
    public PerformanceLogEntity mapFrom(PerformanceLogDto performanceLogDto) {
        return modelMapper.map(performanceLogDto, PerformanceLogEntity.class);
    }
}
