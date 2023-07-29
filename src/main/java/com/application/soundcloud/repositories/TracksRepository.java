package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.Tracks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TracksRepository extends JpaRepository<Tracks, Long> {
    List<Tracks> findByAuthor(String author);
    Tracks findByAuthorAndSongName(String author, String songName);
    Tracks findBySongKey(String songKey);
}
