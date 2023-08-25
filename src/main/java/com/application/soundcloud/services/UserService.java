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
        User user = new User();

        if (userRepository.findByEmail(email) == null) {
            user.setAvatarUrl(avatarUrl);
            user.setEmail(email);
            if (userRepository.findByLogin(username) == null){
                user.setLogin(username);
            }else{
                user.setLogin(generateUniqueLogin(username));
            }if (userRepository.findByUsername(username) == null){
                user.setUsername(username);
            }else{
                user.setUsername(generateUniqueUsername(username));
            }
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



    private String generateUniqueUsername(String username) {
        String newUsername = username;
        Random random = new Random();

        while (userRepository.findByUsername(newUsername) != null) {
            int randomDigits = random.nextInt(9000) + 1000;
            newUsername = username + randomDigits;
        }

        return newUsername;
    }

    private String generateUniqueLogin(String login) {
        String newLogin = login;
        Random random = new Random();

        while (userRepository.findByLogin(newLogin) != null) {
            int randomDigits = random.nextInt(9000) + 1000;
            newLogin = login + randomDigits;
        }

        return newLogin;
    }
}