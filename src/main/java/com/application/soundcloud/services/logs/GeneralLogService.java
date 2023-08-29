package com.application.soundcloud.services.logs;

import com.application.soundcloud.repositories.logs.GeneralLogRepository;
import com.application.soundcloud.tables.logs.GeneralLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralLogService {

    private final GeneralLogRepository logRepository;

    @Autowired
    public GeneralLogService(GeneralLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void saveLog(GeneralLog log) {
        logRepository.save(log);
    }

    // Other methods related to log processing
}
