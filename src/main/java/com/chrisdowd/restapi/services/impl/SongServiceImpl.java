package com.chrisdowd.restapi.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.chrisdowd.restapi.domain.entities.SongEntity;
import com.chrisdowd.restapi.repositories.SongRepository;
import com.chrisdowd.restapi.services.SongService;

@Service
public class SongServiceImpl implements SongService {

    private SongRepository songRepository;

    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public SongEntity createUpdateSong(String isrc, SongEntity songEntity) {
        songEntity.setIsrc(isrc);
        return songRepository.save(songEntity);
    }

    @Override
    public List<SongEntity> findAll() {
        return StreamSupport.stream(songRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Page<SongEntity> findAll(Pageable pageable) {
        return songRepository.findAll(pageable);
    }

    @Override
    public Optional<SongEntity> findOne(String isrc) {
        return songRepository.findById(isrc);
    }

    @Override
    public boolean isExists(String isrc) {
        return songRepository.existsById(isrc);
    }

    @Override
    public SongEntity partialUpdate(String isrc, SongEntity songEntity) {
        songEntity.setIsrc(isrc);

        return songRepository.findById(isrc).map(existingSong -> {
            Optional.ofNullable(songEntity.getTitle()).ifPresent(existingSong::setTitle);
            return songRepository.save(existingSong);
        }).orElseThrow(() -> new RuntimeException("Song does not exist"));
    }

    @Override
    public void deleteSong(String isrc) {
        songRepository.deleteById(isrc);
    }
}
