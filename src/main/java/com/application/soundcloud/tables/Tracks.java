package com.application.soundcloud.tables;

import javax.persistence.*;

import java.sql.Timestamp;

@Entity
public class Tracks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String songName;
    private String author;
    private int likes;
    private Timestamp date;
    private String songKey; // for identification song in playlist (songs haven't unique name solution - key)
    private String songUrl;
    private String avatarSongUrl;

    public Tracks() {
    }

    public Tracks(String songName, String author, int likes, Timestamp date, String songKey, String songUrl, String avatarSongUrl) {
        this.songName = songName;
        this.author = author;
        this.likes = likes;
        this.date = date;
        this.songKey = songKey;
        this.songUrl = songUrl;
        this.avatarSongUrl = avatarSongUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getSongKey() {
        return songKey;
    }

    public void setSongKey(String songKey) {
        this.songKey = songKey;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getAvatarSongUrl() {
        return avatarSongUrl;
    }

    public void setAvatarSongUrl(String avatarSongUrl) {
        this.avatarSongUrl = avatarSongUrl;
    }
}
