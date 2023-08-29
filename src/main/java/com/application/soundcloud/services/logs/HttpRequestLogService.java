package com.application.soundcloud.services.logs;

import com.application.soundcloud.repositories.logs.HttpRequestLogRepository;
import com.application.soundcloud.tables.logs.HttpRequestLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HttpRequestLogService {

    private final HttpRequestLogRepository logRepository;

    @Autowired
    public HttpRequestLogService(HttpRequestLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void saveLog(HttpRequestLog log) {
        logRepository.save(log);
    }

    // Other methods related to log processing
}