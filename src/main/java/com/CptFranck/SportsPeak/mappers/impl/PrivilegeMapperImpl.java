package com.CptFranck.SportsPeak.mappers.impl;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PrivilegeMapperImpl implements Mapper<PrivilegeEntity, PrivilegeDto> {

    private final ModelMapper modelMapper;

    public PrivilegeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PrivilegeDto mapTo(PrivilegeEntity userEntity) {
        return modelMapper.map(userEntity, PrivilegeDto.class);
    }

    @Override
    public PrivilegeEntity mapFrom(PrivilegeDto userDto) {
        return modelMapper.map(userDto, PrivilegeEntity.class);
    }

}
