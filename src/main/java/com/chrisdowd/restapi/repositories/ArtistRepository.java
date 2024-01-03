package com.chrisdowd.restapi.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chrisdowd.restapi.domain.entities.ArtistEntity;

@Repository
public interface ArtistRepository extends CrudRepository<ArtistEntity, Long>{
    
    Iterable<ArtistEntity> ageLessThan(int age);

    @Query("SELECT a FROM ArtistEntity a WHERE a.age > ?1")
    Iterable<ArtistEntity> findArtistWithAgeGreaterThan(int age);
}
