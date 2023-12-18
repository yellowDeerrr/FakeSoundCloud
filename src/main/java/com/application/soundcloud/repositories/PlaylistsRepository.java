package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.Playlists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistsRepository extends JpaRepository<Playlists, Long> {
    List<Playlists> findByUUID(String UUID);
    boolean existsByCode(String code);
    Playlists findByCode(String code);

    Optional<Playlists> findByCodeAndShareCode(String code, String shareCode);
}
