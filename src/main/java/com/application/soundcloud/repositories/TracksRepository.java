package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.Tracks;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TracksRepository extends JpaRepository<Tracks, Long> {
    Tracks findBySongKey(String songKey);
    boolean existsBySongKey(String songKey);

    List<Tracks> findByAuthor(String author);
    Tracks findByAuthorAndSongName(String author, String songName);
}
