package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByUUID(String UUID);
    UserEntity findByLogin(String login);
    UserEntity findByEmail(String email);
    UserEntity findByAvatarUrl(String avatarUrl);
    UserEntity findByUsername(String username);
    UserEntity findByUrlActivationCode(String urlActivationCode);
    UserEntity findByUrlActivationCodeAndActivationCode(String urlActivationCode, Integer activationCode);
    UserEntity findByUrlActivationCodeForResetPassword(String urlActivationCodeForResetPassword);
}
