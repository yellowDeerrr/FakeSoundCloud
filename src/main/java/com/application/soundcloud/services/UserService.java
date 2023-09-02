package com.application.soundcloud.services;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private MailSender mailSender;
    @Autowired
    private final UserRepository userRepository;
    @Value("${url}")
    private String url;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkAndAddUser(String username, String email, String avatarUrl) {
        UserEntity userEntity = new UserEntity();

        if (userRepository.findByEmail(email) == null) {
            userEntity.setAvatarUrl(avatarUrl);
            userEntity.setEmail(email);
            if (userRepository.findByLogin(username) == null){
                userEntity.setLogin(username);
            }else{
                userEntity.setLogin(generateUniqueLogin(username));
            }if (userRepository.findByUsername(username) == null){
                userEntity.setUsername(username);
            }else{
                userEntity.setUsername(generateUniqueUsername(username));
            }
            userRepository.save(userEntity);
        }
    }

    public UserEntity isActivateUser(String urlForActivationCode){
        return userRepository.findByUrlActivationCode(urlForActivationCode);
    }


    public UserEntity checkActivationCode(String urlForActivationCode, Integer code) {
        UserEntity userEntity = userRepository.findByUrlActivationCodeAndActivationCode(urlForActivationCode, code);

        if (userEntity == null){
            return userEntity;
        }
        userEntity.setUrlActivationCode(null);
        userEntity.setActivationCode(null);
        userRepository.save(userEntity);

        return userEntity;
    }
    public boolean checkEmailAndSendLink(String email){
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null || userEntity.getUrlActivationCodeForResetPassword() != null){
            return false;
        }
        userEntity.setUrlActivationCodeForResetPassword(UUID.randomUUID().toString());
        userRepository.save(userEntity);

        String message = String.format(
                "Hello, %s! \n" +
                        "Your reset password link: %slogin/forgot/%s",
                userEntity.getUsername(),
                url,
                userEntity.getUrlActivationCodeForResetPassword());

        mailSender.send(userEntity.getEmail(), "Reset password", message);

        return true;
    }

    public String generateKeyForAvatarUrl(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            char randomSymbol = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(random.nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length())); // Випадковий символ зі стрічки symbols

            stringBuilder.append(randomSymbol);
        }
        String res = stringBuilder.toString();
        UserEntity checkUsersKey = userRepository.findByAvatarUrl(res);
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

    public String maskEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length == 2) {
            String username = parts[0];
            String domain = parts[1];
            int lengthToShow = 2; // Кількість символів, які залишити видимими
            String maskedUsername = username.substring(0, Math.min(lengthToShow, username.length())) + "*****";
            return maskedUsername + "@" + domain;
        }
        return email;
    }
}