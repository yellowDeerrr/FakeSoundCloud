package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Likes findByLoginUserAndSongKey(String loginUser, String songKey);
}
