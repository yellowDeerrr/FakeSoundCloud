package com.application.soundcloud.repositories.analytic;

import com.application.soundcloud.tables.UserEntity;
import com.application.soundcloud.tables.analytic.UserAgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAgentRepository extends JpaRepository<UserAgentEntity, Long> {
    void deleteByUserId(UserEntity userEntity);
}
