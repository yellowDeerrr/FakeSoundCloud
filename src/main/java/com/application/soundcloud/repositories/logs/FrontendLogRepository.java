package com.application.soundcloud.repositories.logs;

import com.application.soundcloud.tables.logs.FrontendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontendLogRepository extends JpaRepository<FrontendLog, Long> {
    // Custom query methods, if needed
}
