package com.application.soundcloud.tables.logs;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sql_query_logs")
public class SqlQueryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    @Column(name = "action")
    private String action;
    @Column(name = "record_id")
    private Long recordId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "action_by")
    private Long actionBy;
    @Column(name = "table_name")
    private String table;

    public SqlQueryLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getActionBy() {
        return actionBy;
    }

    public void setActionBy(Long actionBy) {
        this.actionBy = actionBy;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}