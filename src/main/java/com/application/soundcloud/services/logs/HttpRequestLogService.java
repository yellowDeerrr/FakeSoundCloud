package com.application.soundcloud.services.logs;

import com.application.soundcloud.repositories.logs.HttpRequestLogRepository;
import com.application.soundcloud.tables.logs.HttpRequestLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public void wrongUrlActivationCodeForResetPassword(HttpRequestLog httpRequestLog, String urlActivationCodeForResetPassword){
        httpRequestLog.setRequestTime(LocalDateTime.now());
        httpRequestLog.setMessage("WARN");
        httpRequestLog.setMessage("Failed to log in to the password reset page using the code "+ urlActivationCodeForResetPassword);
        httpRequestLog.setEndpoint("/login/forgot/" + urlActivationCodeForResetPassword);

        saveLog(httpRequestLog);
    }

    public void successfulUrlActivationCodeForResetPassword(HttpRequestLog httpRequestLog, String urlActivationCodeForResetPassword){
        httpRequestLog.setRequestTime(LocalDateTime.now());
        httpRequestLog.setMessage("INFO");
        httpRequestLog.setMessage("Successfully logged in to the password reset page using the code " + urlActivationCodeForResetPassword);

        saveLog(httpRequestLog);
    }
}