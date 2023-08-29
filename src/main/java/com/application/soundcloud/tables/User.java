package com.application.soundcloud.tables;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "_users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String login;
    private String password;
    private String email;
    private String urlActivationCode;
    private Integer activationCode;
    private String urlActivationCodeForResetPassword;
    private String avatarUrl;
    private LocalDateTime createdAt;
    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(Integer activationCode) {
        this.activationCode = activationCode;
    }

    public String getUrlActivationCode() {
        return urlActivationCode;
    }

    public void setUrlActivationCode(String urlActivationCode) {
        this.urlActivationCode = urlActivationCode;
    }

    public String getUrlActivationCodeForResetPassword() {
        return urlActivationCodeForResetPassword;
    }

    public void setUrlActivationCodeForResetPassword(String urlActivationCodeForResetPassword) {
        this.urlActivationCodeForResetPassword = urlActivationCodeForResetPassword;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
