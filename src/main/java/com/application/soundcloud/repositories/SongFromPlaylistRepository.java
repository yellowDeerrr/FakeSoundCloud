package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.SongFromPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongFromPlaylistRepository extends JpaRepository<SongFromPlaylist, Long> {
    List<SongFromPlaylist> findByPlaylistCode(String playlistCode);
}
