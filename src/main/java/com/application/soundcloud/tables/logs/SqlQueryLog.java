package com.application.soundcloud.tables.logs;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sql_query_logs")
public class SqlQueryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "execution_time")
    private LocalDateTime executionTime;

    @Column(name = "query_text")
    private String queryText;

    @Column(name = "query_type")
    private String queryType;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "success")
    private boolean success;

    public SqlQueryLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(LocalDateTime executionTime) {
        this.executionTime = executionTime;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}