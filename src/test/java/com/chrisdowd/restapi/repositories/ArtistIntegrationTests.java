package com.chrisdowd.restapi.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.chrisdowd.restapi.TestDataUtil;
import com.chrisdowd.restapi.domain.entities.ArtistEntity;
import com.chrisdowd.restapi.domain.entities.SongEntity;

import jakarta.transaction.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class ArtistIntegrationTests {
    
    private final ArtistRepository underTest;
    
    @Autowired
    private final SongRepository songRepository;

    @Autowired
    public ArtistIntegrationTests(ArtistRepository underTest, SongRepository songRepository) {
        this.underTest = underTest;
        this.songRepository = songRepository;
    }

    @Test
    public void testThatArtistCanBeCreatedAndRecalled() {
        ArtistEntity artist = TestDataUtil.createTestArtistA();
        underTest.save(artist);
        Optional<ArtistEntity> result = underTest.findById(artist.getId());
        assertTrue(result.isPresent());
        assertEquals(artist, result.get());
    }

    @Test
    public void testThatMultipleArtistsCanBeCreatedAndRecalled() {

        ArtistEntity artistA = TestDataUtil.createTestArtistA();
        underTest.save(artistA);

        ArtistEntity artistB = TestDataUtil.createTestArtistB();
        underTest.save(artistB);

        ArtistEntity artistC = TestDataUtil.createTestArtistC();
        underTest.save(artistC);

        Iterable<ArtistEntity> resultIterable = underTest.findAll();
        List<ArtistEntity> resultList = new ArrayList<>();
        resultIterable.forEach(resultList::add);

        assertEquals(3, resultList.size());
        List<ArtistEntity> expectedArtists = Arrays.asList(artistA, artistB, artistC);
        assertEquals(expectedArtists, resultList);
    }

    @Test
    public void testThatUpdateChangesArtist() {
        ArtistEntity artistA = TestDataUtil.createTestArtistA();
        artistA.setName("UPDATED");
        underTest.save(artistA);
        Optional<ArtistEntity> result = underTest.findById(artistA.getId());
        assertTrue(result.isPresent());
        assertEquals(artistA, result.get());
    }

    @Test
    public void testThatDeleteRemovesArtist() {
        ArtistEntity artistA = TestDataUtil.createTestArtistA();
        underTest.save(artistA);
        SongEntity songA = TestDataUtil.createTestSongEntityA(artistA);
        songRepository.save(songA);

        List<SongEntity> songs = songRepository.findByArtistId(artistA.getId());
        songRepository.deleteAll(songs);

        underTest.deleteById(artistA.getId());
        Optional<ArtistEntity> result = underTest.findById(artistA.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testThatGetArtistWithAgeLessThan() {
        ArtistEntity artistA = TestDataUtil.createTestArtistA();
        underTest.save(artistA);

        ArtistEntity artistB = TestDataUtil.createTestArtistB();
        underTest.save(artistB);

        ArtistEntity artistC = TestDataUtil.createTestArtistC();
        underTest.save(artistC);
        
        Iterable<ArtistEntity> result = underTest.ageLessThan(35);
        List<ArtistEntity> expected = new ArrayList<>();
        expected = Arrays.asList(artistB, artistC);
        assertEquals(expected, result);
    }
}
