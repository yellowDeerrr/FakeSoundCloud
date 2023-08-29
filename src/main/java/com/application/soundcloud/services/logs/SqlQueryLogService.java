package com.application.soundcloud.services.logs;

import com.application.soundcloud.repositories.logs.SqlQueryLogRepository;
import com.application.soundcloud.tables.logs.SqlQueryLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SqlQueryLogService {

    private final SqlQueryLogRepository logRepository;

    @Autowired
    public SqlQueryLogService(SqlQueryLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void saveLog(SqlQueryLog log) {
        logRepository.save(log);
    }

    // Other methods related to log processing
}




