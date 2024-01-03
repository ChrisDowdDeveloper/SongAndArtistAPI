package com.chrisdowd.restapi.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chrisdowd.restapi.domain.dto.ArtistDto;
import com.chrisdowd.restapi.domain.entities.ArtistEntity;
import com.chrisdowd.restapi.mappers.Mapper;
import com.chrisdowd.restapi.services.ArtistService;

@RestController
public class ArtistController {

    private ArtistService artistService;

    private Mapper<ArtistEntity, ArtistDto> artistMapper;

    public ArtistController(ArtistService artistService, Mapper<ArtistEntity, ArtistDto> artistMapper) {
        this.artistService = artistService;
        this.artistMapper = artistMapper;
    }
    
    @PostMapping(path = "/artists")
    public ResponseEntity<ArtistDto> createArtist(@RequestBody ArtistDto artist) {
        ArtistEntity artistEntity = artistMapper.mapFrom(artist);
        ArtistEntity savedArtistEntity = artistService.save(artistEntity);
        return new ResponseEntity<>(artistMapper.mapTo(savedArtistEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/artists")
    public List<ArtistDto> listArtists() {
        List<ArtistEntity> artists = artistService.findAll();
        return artists.stream().map(artistMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/artists/{id}")
    public ResponseEntity<ArtistDto> getArtist(@PathVariable("id") Long id) {
        Optional<ArtistEntity> foundArtist = artistService.findOne(id);
        return foundArtist.map(artistEntity -> {
            ArtistDto artistDto = artistMapper.mapTo(artistEntity);
            return new ResponseEntity<>(artistDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/artists/{id}")
    public ResponseEntity<ArtistDto> fullUpdateArtist(@PathVariable("id") Long id, @RequestBody ArtistDto artistDto) {

        if(!artistService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        artistDto.setId(id);
        ArtistEntity artistEntity = artistMapper.mapFrom(artistDto);
        ArtistEntity savedArtistEntity = artistService.save(artistEntity);
        return new ResponseEntity<>(artistMapper.mapTo(savedArtistEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/artists/{id}")
    public ResponseEntity<ArtistDto> partialUpdate(@PathVariable("id") Long id, @RequestBody ArtistDto artistDto) {
        if(!artistService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ArtistEntity artistEntity = artistMapper.mapFrom(artistDto);
        ArtistEntity updatedArtist = artistService.partialUpdate(id, artistEntity);
        return new ResponseEntity<>(artistMapper.mapTo(updatedArtist), HttpStatus.OK);
    }

    @DeleteMapping(path = "/artists/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable("id") Long id) {
        artistService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
