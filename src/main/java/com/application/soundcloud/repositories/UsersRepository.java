package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    Users findByUsernameAndLoginAndPassword(String username, String login, String password);
}
