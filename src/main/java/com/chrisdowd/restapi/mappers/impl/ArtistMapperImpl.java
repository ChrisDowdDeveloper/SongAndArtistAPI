package com.chrisdowd.restapi.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.chrisdowd.restapi.domain.dto.ArtistDto;
import com.chrisdowd.restapi.domain.entities.ArtistEntity;
import com.chrisdowd.restapi.mappers.Mapper;

@Component
public class ArtistMapperImpl implements Mapper<ArtistEntity, ArtistDto> {

    private ModelMapper modelMapper;

    public ArtistMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    @Override
    public ArtistDto mapTo(ArtistEntity artistEntity) {
        return modelMapper.map(artistEntity, ArtistDto.class);
    }

    @Override
    public ArtistEntity mapFrom(ArtistDto artistDto) {
        return modelMapper.map(artistDto, ArtistEntity.class);
    }
    
}
