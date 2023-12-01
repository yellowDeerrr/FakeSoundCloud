package com.application.soundcloud.services;

import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.tables.Tracks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class SongService {
    private final TracksRepository tracksRepository;

    @Autowired
    public SongService(TracksRepository tracksRepository) {
        this.tracksRepository = tracksRepository;
    }

    public String generateKey(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            char randomSymbol = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(random.nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length()));

            stringBuilder.append(randomSymbol);
        }
        String res = stringBuilder.toString();
        if (!tracksRepository.existsBySongKey(res)) {
            return res;
        } else {
            generateKey();
        }
        return null;
    }
}
