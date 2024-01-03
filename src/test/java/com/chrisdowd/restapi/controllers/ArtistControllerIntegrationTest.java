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
import com.chrisdowd.restapi.domain.dto.ArtistDto;
import com.chrisdowd.restapi.domain.entities.ArtistEntity;
import com.chrisdowd.restapi.services.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureMockMvc
public class ArtistControllerIntegrationTest {
    
    private ArtistService artistService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public ArtistControllerIntegrationTest(MockMvc mockMvc, ArtistService artistService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.artistService = artistService;
    }

    @Test
    public void testThatCreateArtistSuccessfullyReturnsHttp201Created() throws Exception{
        ArtistEntity artistA = TestDataUtil.createTestArtistA();
        artistA.setId(null);
        String artistJson = objectMapper.writeValueAsString(artistA);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(artistJson)
            
        ).andExpect(
            MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedArtist() throws Exception{
        ArtistEntity artistA = TestDataUtil.createTestArtistA();
        artistA.setId(null);
        String artistJson = objectMapper.writeValueAsString(artistA);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(artistJson)
            
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.name").value("Drake")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.age").value(37)
        );
    }

    @Test
    public void testThatGetAllArtistsSuccessfullyReturnsHttp200() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.get("/artists")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAllArtistsSuccessfullyReturnsListOfArtists() throws Exception{
        ArtistEntity testArtistEntityA = TestDataUtil.createTestArtistA();
        artistService.save(testArtistEntityA);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/artists")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$[0].name").value("Drake")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$[0].age").value(37)
        );
    }

    @Test
    public void testThatGetOneArtistsSuccessfullyReturnsHttp200() throws Exception {
        ArtistEntity testArtistEntityA = TestDataUtil.createTestArtistA();
        ArtistEntity savedArtistEntity = artistService.save(testArtistEntityA);
    
        mockMvc.perform(
            MockMvcRequestBuilders.get("/artists/" + savedArtistEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetOneArtistsReturnsHttpStatus404WhenNoAuthorExist() throws Exception{
        ArtistEntity testArtistEntityA = TestDataUtil.createTestArtistA();
        artistService.save(testArtistEntityA);
        
        mockMvc.perform(
            MockMvcRequestBuilders.get("/artists/99")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetArtistReturnsArtistWhenArtistExists() throws Exception {
        ArtistEntity testArtistEntityA = TestDataUtil.createTestArtistA();
        ArtistEntity savedArtistEntity = artistService.save(testArtistEntityA);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/artists/" + savedArtistEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.id").value(savedArtistEntity.getId())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.name").value("Drake")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.age").value(37)
        );
    }

    @Test
    public void testThatFullUpdateArtistsSuccessfullyReturnsHttp200() throws Exception{
        ArtistEntity testArtistEntityA = TestDataUtil.createTestArtistA();
        ArtistEntity savedArtistEntity = artistService.save(testArtistEntityA);

        ArtistDto testArtistDtoA = TestDataUtil.createTestArtistDtoA();
        String artistDtoJson = objectMapper.writeValueAsString(testArtistDtoA);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/artists/" + savedArtistEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(artistDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateArtistsReturnsHttpStatus404WhenNoAuthorExist() throws Exception{
        ArtistDto testArtistDtoA = TestDataUtil.createTestArtistDtoA();
        String artistDtoJson = objectMapper.writeValueAsString(testArtistDtoA);
        
        mockMvc.perform(
            MockMvcRequestBuilders.get("/artists/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(artistDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingArtist() throws Exception{
        ArtistEntity testArtistEntityA = TestDataUtil.createTestArtistA();
        ArtistEntity savedArtistEntity = artistService.save(testArtistEntityA);

        ArtistEntity artistDto = TestDataUtil.createTestArtistB();
        artistDto.setId(savedArtistEntity.getId());

        String artistDtoUpdateJson = objectMapper.writeValueAsString(artistDto);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/artists/" + savedArtistEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(artistDtoUpdateJson)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.id").value(savedArtistEntity.getId())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.name").value(artistDto.getName())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.age").value(artistDto.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateExistingArtistReturnsHttp200() throws Exception{
        ArtistEntity testArtistEntityA = TestDataUtil.createTestArtistA();
        ArtistEntity savedArtistEntity = artistService.save(testArtistEntityA);

        ArtistDto testArtistDtoA = TestDataUtil.createTestArtistDtoA();
        testArtistDtoA.setName("UPDATED");
        String artistDtoJson = objectMapper.writeValueAsString(testArtistDtoA);

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/artists/" + savedArtistEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(artistDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateExistingArtistReturnsUpdatedArtist() throws Exception {
        ArtistEntity testArtistEntityA = TestDataUtil.createTestArtistA();
        ArtistEntity savedArtistEntity = artistService.save(testArtistEntityA);

        ArtistDto testArtistDtoA = TestDataUtil.createTestArtistDtoA();
        testArtistDtoA.setName("UPDATED");
        String artistDtoJson = objectMapper.writeValueAsString(testArtistDtoA);

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/artists/" + savedArtistEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(artistDtoJson)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.id").value(savedArtistEntity.getId())
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.age").value(testArtistDtoA.getAge())
        );
    }

    @Test
    public void testThatDeleteArtistReturnsHttp204ForNonExistingArtist() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/artists/456321")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteArtistReturnsHttp204WhenDeleted() throws Exception {
        ArtistEntity testArtistEntityA = TestDataUtil.createTestArtistA();
        ArtistEntity savedArtistEntity = artistService.save(testArtistEntityA);


        mockMvc.perform(
            MockMvcRequestBuilders.delete("/artists/" + savedArtistEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());;
    }
}
