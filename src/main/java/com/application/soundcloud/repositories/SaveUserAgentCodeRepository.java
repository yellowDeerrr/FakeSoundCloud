package com.application.soundcloud.repositories;

import com.application.soundcloud.tables.SaveUserAgentCode;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveUserAgentCodeRepository extends JpaRepository<SaveUserAgentCode, Long> {
    SaveUserAgentCode findByUserId(UserEntity userId);
    void deleteByUserId(UserEntity userId);
}
