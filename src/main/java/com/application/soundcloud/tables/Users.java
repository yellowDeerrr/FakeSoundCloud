package com.application.soundcloud.tables;

import jakarta.persistence.*;

@Table(name = "_users")
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String login;
    private String password;
    private byte[] avatarByteCode;
    private String base64Image;


    public Users() {
    }

    public Users(String username, String login, String password, byte[] avatarByteCode) {
        this.username = username;
        this.login = login;
        this.password = password;
        this.avatarByteCode = avatarByteCode;
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

    public byte[] getAvatarByteCode() {
        return avatarByteCode;
    }

    public void setAvatarByteCode(byte[] avatarByteCode) {
        this.avatarByteCode = avatarByteCode;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

}
