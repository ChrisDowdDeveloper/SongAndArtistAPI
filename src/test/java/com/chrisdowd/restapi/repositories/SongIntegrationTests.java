package com.chrisdowd.restapi.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

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
public class SongIntegrationTests {
    
    private final SongRepository underTest;

    @Autowired
    public SongIntegrationTests(SongRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatSongCanBeCreatedAndRecalled() {
        ArtistEntity artistA = TestDataUtil.createTestArtistA();
        SongEntity songA = TestDataUtil.createTestSongEntityA(artistA);
        underTest.save(songA);
        Optional<SongEntity> result = underTest.findById(songA.getIsrc());
        assertTrue(result.isPresent());
        assertEquals(songA, result.get());
    }

    @Test
    public void testThatMultipleSongsCanBeCreatedAndRecalled() {
        ArtistEntity artistA = TestDataUtil.createTestArtistA();
        SongEntity songA = TestDataUtil.createTestSongEntityA(artistA);
        underTest.save(songA);

        ArtistEntity artistB = TestDataUtil.createTestArtistB();
        SongEntity songB = TestDataUtil.createTestSongB(artistB);
        underTest.save(songB);

        ArtistEntity artistC = TestDataUtil.createTestArtistC();
        SongEntity songC = TestDataUtil.createTestSongC(artistC);
        underTest.save(songC);

        Iterable<SongEntity> result = underTest.findAll();
        List<SongEntity> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        assertEquals(3, resultList.size());
        List<SongEntity> expectedSongs = Arrays.asList(songA, songB, songC);
        assertEquals(expectedSongs, resultList);
    }

    @Test
    public void testThatSongCanBeUpdated() {
        ArtistEntity artistA = TestDataUtil.createTestArtistA();
        SongEntity songA = TestDataUtil.createTestSongEntityA(artistA);
        underTest.save(songA);
        songA.setTitle("UPDATED");
        underTest.save(songA);
        Optional<SongEntity> result = underTest.findById(songA.getIsrc());
        assertTrue(result.isPresent());
        assertEquals(songA, result.get());
    }

    @Test
    public void testThatSongCanBeDeleted() {
        ArtistEntity artistA = TestDataUtil.createTestArtistA();
        SongEntity songA = TestDataUtil.createTestSongEntityA(artistA);
        underTest.save(songA);
        underTest.deleteById(songA.getIsrc());
        Optional<SongEntity> result = underTest.findById(songA.getIsrc());
        assertTrue(result.isEmpty());
    }
}
