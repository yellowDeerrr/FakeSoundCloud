package com.application.soundcloud.repositories;
import java.util.Optional;

import com.application.soundcloud.tables.RefreshToken;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(UserEntity user);
}
