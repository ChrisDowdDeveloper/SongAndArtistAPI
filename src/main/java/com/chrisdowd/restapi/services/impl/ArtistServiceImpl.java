package com.chrisdowd.restapi.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.chrisdowd.restapi.domain.entities.ArtistEntity;
import com.chrisdowd.restapi.repositories.ArtistRepository;
import com.chrisdowd.restapi.services.ArtistService;

@Service
public class ArtistServiceImpl implements ArtistService{
    
    private ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public ArtistEntity save(ArtistEntity artistEntity) {
        return artistRepository.save(artistEntity);
    }

    @Override 
    public List<ArtistEntity> findAll() {
        return StreamSupport.stream(artistRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<ArtistEntity> findOne(Long id) {
        return artistRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return artistRepository.existsById(id);
    }

    @Override
    public ArtistEntity partialUpdate(Long id, ArtistEntity artistEntity) {
        artistEntity.setId(id);

        return artistRepository.findById(id).map(existingArtist -> {
            Optional.ofNullable(artistEntity.getAge()).ifPresent(existingArtist::setAge);
            Optional.ofNullable(artistEntity.getName()).ifPresent(existingArtist::setName);
            return artistRepository.save(existingArtist);
        }).orElseThrow(() -> new RuntimeException("Artist does not exist"));
    }

    @Override
    public void delete(Long id) {
        artistRepository.deleteById(id);
    }
}
