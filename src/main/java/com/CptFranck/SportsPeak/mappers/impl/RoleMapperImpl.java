package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapperImpl implements Mapper<RoleEntity, RoleDto> {

    private final ModelMapper modelMapper;

    public RoleMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleDto mapTo(RoleEntity userEntity) {
        return modelMapper.map(userEntity, RoleDto.class);
    }

    @Override
    public RoleEntity mapFrom(RoleDto userDto) {
        return modelMapper.map(userDto, RoleEntity.class);
    }

}
