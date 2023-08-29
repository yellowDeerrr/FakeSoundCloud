package com.application.soundcloud.services.logs;

import com.application.soundcloud.repositories.logs.BackendLogRepository;
import com.application.soundcloud.tables.logs.BackendLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackendLogService {

    private final BackendLogRepository logRepository;

    @Autowired
    public BackendLogService(BackendLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void saveLog(BackendLog log) {
        logRepository.save(log);
    }

    // Other methods related to log processing
}
