package com.chrisdowd.restapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.chrisdowd.restapi.domain.entities.SongEntity;

public interface SongService {

    SongEntity createUpdateSong(String isrc, SongEntity songEntity);

    List<SongEntity> findAll();

    Page<SongEntity> findAll(Pageable pageable);

    Optional<SongEntity> findOne(String isrc);

    boolean isExists(String isrc);

    SongEntity partialUpdate(String isrc, SongEntity songEntity);

    void deleteSong(String isrc);
    
}
