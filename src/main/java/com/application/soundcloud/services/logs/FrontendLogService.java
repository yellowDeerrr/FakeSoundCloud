package com.application.soundcloud.services.logs;

import com.application.soundcloud.repositories.logs.FrontendLogRepository;
import com.application.soundcloud.tables.logs.FrontendLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrontendLogService {

    private final FrontendLogRepository logRepository;

    @Autowired
    public FrontendLogService(FrontendLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void saveLog(FrontendLog log) {
        logRepository.save(log);
    }

    // Other methods related to log processing
}
