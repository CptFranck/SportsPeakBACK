package com.CptFranck.SportsPeak.mapper.impl;

import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.mapper.typeConverter.StringToTrustLabelConverter;
import com.CptFranck.SportsPeak.mapper.typeConverter.StringToVisibilityConverter;
import com.CptFranck.SportsPeak.mapper.typeConverter.TrustLabelToStringConverter;
import com.CptFranck.SportsPeak.mapper.typeConverter.VisibilityToStringConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProgExerciseMapperImpl implements Mapper<ProgExerciseEntity, ProgExerciseDto> {

    private final ModelMapper modelMapper;

    public ProgExerciseMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.addConverter(new VisibilityToStringConverter());
        this.modelMapper.addConverter(new StringToVisibilityConverter());
        this.modelMapper.addConverter(new TrustLabelToStringConverter());
        this.modelMapper.addConverter(new StringToTrustLabelConverter());
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
