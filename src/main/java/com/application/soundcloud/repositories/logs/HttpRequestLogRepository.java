package com.application.soundcloud.repositories.logs;

import com.application.soundcloud.tables.logs.HttpRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpRequestLogRepository extends JpaRepository<HttpRequestLog, Long> {
    // Custom query methods, if needed
}