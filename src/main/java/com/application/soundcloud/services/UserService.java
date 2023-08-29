package com.application.soundcloud.services;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.tables.User;
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

    public User isActivateUser(String urlForActivationCode){
        return userRepository.findByUrlActivationCode(urlForActivationCode);
    }


    public User checkActivationCode(String urlForActivationCode, Integer code) {
        User user = userRepository.findByUrlActivationCodeAndActivationCode(urlForActivationCode, code);

        if (user == null){
            return user;
        }
        user.setUrlActivationCode(null);
        user.setActivationCode(null);
        userRepository.save(user);

        return user;
    }
    public boolean checkEmailAndSendLink(String email){
        User user = userRepository.findByEmail(email);

        if (user == null || user.getUrlActivationCodeForResetPassword() != null){
            return false;
        }
        user.setUrlActivationCodeForResetPassword(UUID.randomUUID().toString());
        userRepository.save(user);

        String message = String.format(
                "Hello, %s! \n" +
                        "Your reset password link: %slogin/forgot/%s",
                user.getUsername(),
                url,
                user.getUrlActivationCodeForResetPassword());

        mailSender.send(user.getEmail(), "Reset password", message);

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