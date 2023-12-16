package com.application.soundcloud.services;

import com.application.soundcloud.repositories.PlaylistsRepository;
import com.application.soundcloud.tables.Likes;
import com.application.soundcloud.tables.Playlists;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PlaylistsService {
    @Autowired
    private PlaylistsRepository playlistsRepository;

    public String createPlaylist(String name, String UUID){
        String code = generateCodeForPlaylist();
        Playlists playlist = new Playlists();

        playlist.setName(name);
        playlist.setUUID(UUID);
        playlist.setCode(code);

        playlistsRepository.save(playlist);
        return code; // code of new playlist
    }

    public List<Playlists> getUserPlaylists(String UUID){
        return playlistsRepository.findByUUID(UUID);
    }

    public Playlists getPlaylist(String code){
        return playlistsRepository.findByCode(code);
    }

    public String generateCodeForPlaylist(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            char randomSymbol = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(random.nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length())); // Випадковий символ зі стрічки symbols

            stringBuilder.append(randomSymbol);
        }
        String res = stringBuilder.toString();
        if (!playlistsRepository.existsByCode(res)) {
            return res;
        } else {
            generateCodeForPlaylist();
        }

        return null;
    }
}
