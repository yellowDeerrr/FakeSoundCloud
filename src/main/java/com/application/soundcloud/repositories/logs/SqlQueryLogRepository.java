package com.application.soundcloud.repositories.logs;

import com.application.soundcloud.tables.logs.SqlQueryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlQueryLogRepository extends JpaRepository<SqlQueryLog, Long> {
    // Custom query methods, if needed
}
