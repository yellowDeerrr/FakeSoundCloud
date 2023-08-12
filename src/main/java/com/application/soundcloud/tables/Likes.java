package com.application.soundcloud.tables;

import javax.persistence.*;

@Entity
@Table(name = "users_likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String loginUser;
    private String songKey;

    public Likes() {
    }

    public Likes(String id_user, String songKey) {
        this.loginUser = id_user;
        this.songKey = songKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getSongKey() {
        return songKey;
    }

    public void setSongKey(String songKey) {
        this.songKey = songKey;
    }
}
