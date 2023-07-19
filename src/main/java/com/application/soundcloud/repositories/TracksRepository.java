package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.Tracks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TracksRepository extends JpaRepository<Tracks, Long> {
    List<Tracks> findByAuthor(String author);
    Tracks findByAuthorAndSongName(String author, String songName);
    Tracks findBySongKey(String songKey);
}
