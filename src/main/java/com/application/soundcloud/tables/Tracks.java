package com.application.soundcloud.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Base64;

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
    private byte[] songByteCode;
    private byte[] avatarSongByteCode;
    private String base64Image;
    private String base64Song;

    public Tracks() {
    }

    public Tracks(String songName, String author, int likes, Timestamp date, String songKey, byte[] songByteCode, byte[] avatarSongByteCode) {
        this.songName = songName;
        this.author = author;
        this.likes = likes;
        this.date = date;
        this.songKey = songKey;
        this.songByteCode = songByteCode;
        this.avatarSongByteCode = avatarSongByteCode;
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

    public byte[] getSongByteCode() {
        return songByteCode;
    }

    public void setSongByteCode(byte[] songByteCode) {
        this.songByteCode = songByteCode;
    }

    public byte[] getAvatarSongByteCode() {
        return avatarSongByteCode;
    }

    public void setAvatarSongByteCode(byte[] avatarSongByteCode) {
        this.avatarSongByteCode = avatarSongByteCode;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getBase64Song() {
        return base64Song;
    }

    public void setBase64Song(String base64Song) {
        this.base64Song = base64Song;
    }
}
