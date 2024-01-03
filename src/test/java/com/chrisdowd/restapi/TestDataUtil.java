package com.chrisdowd.restapi;

import com.chrisdowd.restapi.domain.dto.ArtistDto;
import com.chrisdowd.restapi.domain.dto.SongDto;
import com.chrisdowd.restapi.domain.entities.ArtistEntity;
import com.chrisdowd.restapi.domain.entities.SongEntity;

public class TestDataUtil {
    private TestDataUtil(){
    }

    public static ArtistEntity createTestArtistA() {
        return ArtistEntity.builder()
            .name("Drake")
            .age(37)
            .build();
    }

    public static ArtistDto createTestArtistDtoA() {
        return ArtistDto.builder()
            .name("Drake")
            .age(37)
            .build();
    }

    public static ArtistEntity createTestArtistB() {
        return ArtistEntity.builder()
            .name("Justin Bieber")
            .age(29)
            .build();
    }

    public static ArtistEntity createTestArtistC() {
        return ArtistEntity.builder()
            .name("Taylor Swift")
            .age(34)
            .build();
    }

    public static SongEntity createTestSongEntityA(final ArtistEntity artist) {
        return SongEntity.builder()
            .isrc("USUG12001749")
            .title("LAUGH NOW CRY LATER")
            .artist(artist)
            .build();
    }

    public static SongDto createTestSongDtoA(final ArtistDto artist) {
        return SongDto.builder()
            .isrc("USUG12001749")
            .title("LAUGH NOW CRY LATER")
            .artist(artist)
            .build();
    }

    public static SongEntity createTestSongB(final ArtistEntity artist) {
        return SongEntity.builder()
            .isrc("USUM71516760")
            .title("Sorry")
            .artist(artist)
            .build();
    }

    public static SongEntity createTestSongC(final ArtistEntity artist) {
        return SongEntity.builder()
            .isrc("USCJY1431409")
            .title("Blank Space")
            .artist(artist) 
            .build();
    }
}
