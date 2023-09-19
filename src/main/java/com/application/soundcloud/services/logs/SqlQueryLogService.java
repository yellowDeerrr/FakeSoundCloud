package com.application.soundcloud.services.logs;

import com.application.soundcloud.repositories.logs.SqlQueryLogRepository;
import com.application.soundcloud.tables.logs.SqlQueryLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public void saveSqlQueryLog(String action, Long recordId, Long userId, Long actionBy, String table){
        SqlQueryLog sqlQueryLog = new SqlQueryLog();

        sqlQueryLog.setTimestamp(LocalDateTime.now());
        sqlQueryLog.setAction(action);
        sqlQueryLog.setRecordId(recordId);
        sqlQueryLog.setUserId(userId);
        sqlQueryLog.setActionBy(actionBy);
        sqlQueryLog.setTable(table);

        saveLog(sqlQueryLog);
    }
}




