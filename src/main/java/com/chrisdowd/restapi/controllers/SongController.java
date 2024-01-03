package com.chrisdowd.restapi.controllers;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chrisdowd.restapi.domain.dto.SongDto;
import com.chrisdowd.restapi.domain.entities.SongEntity;
import com.chrisdowd.restapi.mappers.Mapper;
import com.chrisdowd.restapi.services.SongService;

@RestController
public class SongController {
    
    private SongService songService;

    private Mapper<SongEntity, SongDto> songMapper;

    public SongController(SongService songService, Mapper<SongEntity, SongDto> songMapper) {
        this.songService = songService;
        this.songMapper = songMapper;
    }
    
    @PutMapping(path = "/songs/{isrc}")
    public ResponseEntity<SongDto> createUpdateSong(@PathVariable("isrc") String isrc, @RequestBody SongDto songDto) {

        SongEntity songEntity = songMapper.mapFrom(songDto);
        boolean songExists = songService.isExists(isrc);
        SongEntity savedSongEntity = songService.createUpdateSong(isrc, songEntity);
        SongDto savedUpdatedSongDto = songMapper.mapTo(savedSongEntity);
        
        if(songExists) {
            return new ResponseEntity<>(savedUpdatedSongDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(savedUpdatedSongDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/songs")
    public Page<SongDto> listSongs(Pageable pageable) {
        Page<SongEntity> songs = songService.findAll(pageable);
        return songs.map(songMapper::mapTo);
    }

    @GetMapping(path = "/songs/{isrc}")
    public ResponseEntity<SongDto> getSong(@PathVariable("isrc") String isrc) {
        Optional<SongEntity> foundSong = songService.findOne(isrc);
        return foundSong.map(songEntity -> {
            SongDto songDto = songMapper.mapTo(songEntity);
            return new ResponseEntity<>(songDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/songs/{isrc}")
    public ResponseEntity<SongDto> partialUpdate(@PathVariable("isrc") String isrc, @RequestBody SongDto songDto) {
        
        if(!songService.isExists(isrc)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        SongEntity songEntity = songMapper.mapFrom(songDto);
        SongEntity updatedSongEntity = songService.partialUpdate(isrc, songEntity);
        return new ResponseEntity<>(songMapper.mapTo(updatedSongEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/songs/{isrc}")
    public ResponseEntity<Void> delete(@PathVariable("isrc") String isrc) {
        songService.deleteSong(isrc);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}
