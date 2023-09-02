package com.application.soundcloud.services.logs;

import com.application.soundcloud.repositories.logs.BackendLogRepository;
import com.application.soundcloud.tables.logs.BackendLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public void successfulSentUrlForResetPasswordOnEmail(String userEmail, BackendLog backendLog){
        backendLog.setLogLevel("INFO");
        backendLog.setLogTime(LocalDateTime.now());
        backendLog.setMessage("Sent url for reset password to email: " + userEmail);

        saveLog(backendLog);
    }

    public void errorSentUrlForResetPasswordOnEmail(String userEmail, BackendLog backendLog){
        backendLog.setLogLevel("ERROR");
        backendLog.setLogTime(LocalDateTime.now());
        backendLog.setMessage("Attempted to find user for password reset with email: " + userEmail);

        saveLog(backendLog);
    }

    public void successfulResetPassword(BackendLog backendLog, String urlForResetPasswordOnEmail){
        backendLog.setLogLevel("INFO");
        backendLog.setLogTime(LocalDateTime.now());
        backendLog.setMessage("Successfully reset password with code: " + urlForResetPasswordOnEmail);

        saveLog(backendLog);
    }
}
