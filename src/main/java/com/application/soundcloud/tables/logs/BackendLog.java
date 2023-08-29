package com.application.soundcloud.tables.logs;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "backend_logs")
public class BackendLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "log_time")
    private LocalDateTime logTime;

    @Column(name = "log_level")
    private String logLevel;

    @Column(name = "message")
    private String message;

    @Column(name = "user_id")
    private Long userId;

    public BackendLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalDateTime logTime) {
        this.logTime = logTime;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
