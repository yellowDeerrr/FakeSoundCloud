package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
    User findByEmail(String email);
    User findByAvatarUrl(String avatarUrl);
    User findByUsername(String username);
    User findByUrlForActivationCode(String urlForActivationCode);
    User findByUrlForActivationCodeAndActivationCode(String urlForActivationCode, Integer activationCode);
}
