package com.application.soundcloud.repositories.logs;

import com.application.soundcloud.tables.logs.GeneralLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralLogRepository extends JpaRepository<GeneralLog, Long> {
    // Custom query methods, if needed
}
