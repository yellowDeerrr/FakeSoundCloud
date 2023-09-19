package com.application.soundcloud.repositories.logs;

import com.application.soundcloud.tables.logs.BackendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackendLogRepository extends JpaRepository<BackendLog, Long> {
    Optional<BackendLog> findById(Long id);
}
