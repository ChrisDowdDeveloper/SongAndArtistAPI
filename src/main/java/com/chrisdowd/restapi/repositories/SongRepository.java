package com.chrisdowd.restapi.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.chrisdowd.restapi.domain.entities.SongEntity;

@Repository
public interface SongRepository extends CrudRepository<SongEntity, String>, PagingAndSortingRepository<SongEntity, String> {
    List<SongEntity> findByArtistId(Long artistId);
}
