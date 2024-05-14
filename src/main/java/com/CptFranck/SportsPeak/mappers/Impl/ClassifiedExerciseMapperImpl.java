package com.CptFranck.SportsPeak.mappers.Impl;

import com.CptFranck.SportsPeak.domain.dto.ClassifiedExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.relationEntity.ClassifiedExerciseEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedExerciseMapperImpl implements Mapper<ClassifiedExerciseEntity, ClassifiedExerciseDto> {

    private final ModelMapper modelMapper;

    public ClassifiedExerciseMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ClassifiedExerciseDto mapTo(ClassifiedExerciseEntity classifiedExerciseEntity) {
        return modelMapper.map(classifiedExerciseEntity, ClassifiedExerciseDto.class);
    }

    @Override
    public ClassifiedExerciseEntity mapFrom(ClassifiedExerciseDto classifiedExerciseDto) {
        return modelMapper.map(classifiedExerciseDto, ClassifiedExerciseEntity.class);
    }
}
