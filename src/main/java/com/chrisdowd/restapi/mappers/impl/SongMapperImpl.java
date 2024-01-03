package com.chrisdowd.restapi.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.chrisdowd.restapi.domain.dto.SongDto;
import com.chrisdowd.restapi.domain.entities.SongEntity;
import com.chrisdowd.restapi.mappers.Mapper;

@Component
public class SongMapperImpl implements Mapper<SongEntity, SongDto> {

    private ModelMapper modelMapper;

    public SongMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    @Override
    public SongDto mapTo(SongEntity songEntity) {
        return modelMapper.map(songEntity, SongDto.class);
    }

    @Override
    public SongEntity mapFrom(SongDto songDto) {
        return modelMapper.map(songDto, SongEntity.class);
    }
    
}
