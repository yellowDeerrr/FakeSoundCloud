package com.application.soundcloud.services;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkAndAddUser(String username, String email, String avatarUrl) {
        if (userRepository.findByLogin(username) == null) {

            User user = new User();
            user.setLogin(username);
            user.setEmail(email);
            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);
        }
    }

    public String generateKeyForAvatarUrl(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            char randomSymbol = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(random.nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length())); // Випадковий символ зі стрічки symbols

            stringBuilder.append(randomSymbol);
        }
        String res = stringBuilder.toString();
        User checkUsersKey = userRepository.findByAvatarUrl(res);
        if (checkUsersKey == null) {
            return res;
        } else {
            generateKeyForAvatarUrl();
        }

        return null;
    }
}