package com.chrisdowd.restapi.services;

import java.util.List;
import java.util.Optional;

import com.chrisdowd.restapi.domain.entities.ArtistEntity;

public interface ArtistService {

    ArtistEntity save(ArtistEntity artistEntity);

    List<ArtistEntity> findAll();

    Optional<ArtistEntity> findOne(Long id);

    boolean isExists(Long id);

    ArtistEntity partialUpdate(Long id, ArtistEntity artistEntity);

    void delete(Long id);

}
