package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findBySongKeyAndUUID(String songKey, String UUID);

    List<Likes> findByUUID(String UUID);

}
