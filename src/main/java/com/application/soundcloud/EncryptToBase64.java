package com.application.soundcloud;

import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.Users;

import java.util.Base64;
import java.util.List;

public class EncryptToBase64 {
    public static void encrypteTracksImageAndSongToBase64(List<Tracks> songs){
        for (Tracks song : songs) {
            String base64Image = Base64.getEncoder().encodeToString(song.getAvatarSongByteCode());
            song.setBase64Image(base64Image);

            String base64Song = Base64.getEncoder().encodeToString(song.getSongByteCode());
            song.setBase64Song(base64Song);
        }
    }

    public static void encrypteTrackImageAndSongToBase64(Tracks song){
        String base64Image = Base64.getEncoder().encodeToString(song.getAvatarSongByteCode());
        song.setBase64Image(base64Image);

        String base64Song = Base64.getEncoder().encodeToString(song.getSongByteCode());
        song.setBase64Song(base64Song);
    }

    public static void encrypteUserAvatarToBase64(Users user){
        String base64Image = Base64.getEncoder().encodeToString(user.getAvatarByteCode());
        user.setBase64Image(base64Image);
    }
}
