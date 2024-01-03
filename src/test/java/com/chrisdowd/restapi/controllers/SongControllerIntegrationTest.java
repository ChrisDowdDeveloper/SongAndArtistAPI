package com.chrisdowd.restapi.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.chrisdowd.restapi.TestDataUtil;
import com.chrisdowd.restapi.domain.dto.SongDto;
import com.chrisdowd.restapi.domain.entities.SongEntity;
import com.chrisdowd.restapi.services.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureMockMvc
public class SongControllerIntegrationTest {
    
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private SongService songService; 

    @Autowired
    public SongControllerIntegrationTest(MockMvc mockMvc, SongService songService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.songService = songService;
    }

    @Test
    public void testThatCreateSongReturnsHttpStatus201Created() throws Exception {
        SongDto songDto = TestDataUtil.createTestSongDtoA(null);
        String createSongJson = objectMapper.writeValueAsString(songDto);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/songs/" + songDto.getIsrc())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createSongJson)
            
        ).andExpect(
            MockMvcResultMatchers.status().isCreated()
        );
    }
    
    @Test
    public void testThatUpdateSongReturnsHttpStatus200Ok() throws Exception {
        SongEntity testSongEntityA = TestDataUtil.createTestSongEntityA(null);
        SongEntity savedSongEntity = songService.createUpdateSong(testSongEntityA.getIsrc(), testSongEntityA);

        SongDto testSongA = TestDataUtil.createTestSongDtoA(null);
        testSongA.setIsrc(savedSongEntity.getIsrc());
        String songJson = objectMapper.writeValueAsString(testSongA);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/songs/" + savedSongEntity.getIsrc())
                .contentType(MediaType.APPLICATION_JSON)
                .content(songJson)
            
        ).andExpect(
            MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateSongReturnsUpdatedSong() throws Exception {
        SongEntity testSongEntityA = TestDataUtil.createTestSongEntityA(null);
        SongEntity savedSongEntity = songService.createUpdateSong(testSongEntityA.getIsrc(), testSongEntityA);

        SongDto testSongA = TestDataUtil.createTestSongDtoA(null);
        testSongA.setIsrc(savedSongEntity.getIsrc());
        testSongA.setTitle("UPDATED");
        String songJson = objectMapper.writeValueAsString(testSongA);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/songs/" + savedSongEntity.getIsrc())
                .contentType(MediaType.APPLICATION_JSON)
                .content(songJson)
            
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.isrc").value("USUG12001749")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        );
    }
    
    @Test
    public void testThatCreateSongReturnsCreatedSong() throws Exception {
        SongDto songDto = TestDataUtil.createTestSongDtoA(null);
        String createSongJson = objectMapper.writeValueAsString(songDto);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/songs/" + songDto.getIsrc())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createSongJson)
            
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.isrc").value(songDto.getIsrc())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.title").value(songDto.getTitle())
        );
    }

    @Test
    public void testThatGetAllSongsSuccessfullyReturnsHttp200() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.get("/songs")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAllSongsSuccessfullyReturnsListOfSongs() throws Exception{
        SongEntity testSongEntityA = TestDataUtil.createTestSongEntityA(null);
        songService.createUpdateSong(testSongEntityA.getIsrc(), testSongEntityA);
        
        mockMvc.perform(
            MockMvcRequestBuilders.get("/songs")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].isrc").value("USUG12001749")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].title").value("LAUGH NOW CRY LATER")
        );
    }

    @Test
    public void testThatGetSongSuccessfullyReturnsHttp200() throws Exception{
        SongEntity testSongEntityA = TestDataUtil.createTestSongEntityA(null);
        songService.createUpdateSong(testSongEntityA.getIsrc(), testSongEntityA);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/songs/" + testSongEntityA.getIsrc())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetSongReturnsHttp404WhenSongDoesNotExist() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.get("/songs/abc123")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatPartialUpdateSongReturnsHttpStatus200() throws Exception {
        SongEntity testSongEntityA = TestDataUtil.createTestSongEntityA(null);
        songService.createUpdateSong(testSongEntityA.getIsrc(), testSongEntityA);

        SongDto testSongA = TestDataUtil.createTestSongDtoA(null);
        testSongA.setTitle("UPDATED");
        String songJson = objectMapper.writeValueAsString(testSongA);
        

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/songs/" + testSongEntityA.getIsrc())
                .contentType(MediaType.APPLICATION_JSON)
                .content(songJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateExistingSongReturnsUpdatedSong() throws Exception {
        SongEntity testSongEntityA = TestDataUtil.createTestSongEntityA(null);
        songService.createUpdateSong(testSongEntityA.getIsrc(), testSongEntityA);

        SongDto testSongDtoA = TestDataUtil.createTestSongDtoA(null);
        testSongDtoA.setTitle("UPDATED");
        String songDtoJson = objectMapper.writeValueAsString(testSongDtoA);

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/songs/" + testSongEntityA.getIsrc())
                .contentType(MediaType.APPLICATION_JSON)
                .content(songDtoJson)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.isrc").value(testSongEntityA.getIsrc())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        );
    }

    @Test
    public void testThatDeleteSongReturns204ForNonExistingSongs() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/songs/165216")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteSongReturns204() throws Exception {
        SongEntity testSongEntityA = TestDataUtil.createTestSongEntityA(null);
        songService.createUpdateSong(testSongEntityA.getIsrc(), testSongEntityA);

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/songs/" + testSongEntityA.getIsrc())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isNoContent());
    }

}
