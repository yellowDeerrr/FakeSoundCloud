package com.application.soundcloud;

import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.Users;

import java.util.Base64;
import java.util.List;

public class EncryptToBase64 {
    public static void encrypteTracks(List<Tracks> songs){
        for (Tracks song : songs) {
            String base64Image = Base64.getEncoder().encodeToString(song.getSongByteCode());
            song.setBase64Image(base64Image);
        }
    }

    public static void encrypteUser(Users user){
        String base64Image = Base64.getEncoder().encodeToString(user.getAvatarByteCode());
        user.setBase64Image(base64Image);
    }
}
