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
    public void sendActivationCodeForRegister(String specifiedEmail, Integer activationCode, BackendLog backendLog){
        backendLog.setLogLevel("INFO");
        backendLog.setLogTime(LocalDateTime.now());
        backendLog.setMessage("Send activation code to email:" + specifiedEmail + " , with activation code: " + activationCode);

        saveLog(backendLog);
    }

    public void sendUrlForResetPasswordOnEmail(String specifiedEmail, BackendLog backendLog){
        backendLog.setLogLevel("INFO");
        backendLog.setLogTime(LocalDateTime.now());
        backendLog.setMessage("Sent url for reset password to email: " + specifiedEmail);

        saveLog(backendLog);
    }

    //Email doesn't exist or already have 'urlForResetPasswordOnEmail'
    public void errorSendUrlForResetPasswordOnEmail(String specifiedEmail, BackendLog backendLog){
        backendLog.setLogLevel("ERROR");
        backendLog.setLogTime(LocalDateTime.now());
        backendLog.setMessage("Attempted to find user for password reset with email: " + specifiedEmail);

        saveLog(backendLog);
    }

    public void successfulResetPassword(BackendLog backendLog, String urlForResetPasswordOnEmail){
        backendLog.setLogLevel("INFO");
        backendLog.setLogTime(LocalDateTime.now());
        backendLog.setMessage("Successfully reset password with code: " + urlForResetPasswordOnEmail);

        saveLog(backendLog);
    }

    // Wrong login, email or username
    public void errorRegisterUser(BackendLog backendLog){
        backendLog.setLogLevel("WARN");
        backendLog.setLogTime(LocalDateTime.now());

        saveLog(backendLog);
    }
}
