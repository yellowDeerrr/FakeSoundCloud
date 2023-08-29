package com.application.soundcloud.tables.logs;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "http_request_logs")
public class HttpRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Column(name = "method")
    private String method;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "status_code")
    private int statusCode;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "response_body")
    private String responseBody;

    public HttpRequestLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
