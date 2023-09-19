package com.application.soundcloud.services;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.repositories.analytic.UserAgentRepository;
import com.application.soundcloud.tables.UserEntity;
import com.application.soundcloud.tables.analytic.UserAgentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private MailSenderService mailSenderService;
    private final UserRepository userRepository;
    @Value("${url}")
    private String url;
    @Value("${pathToSoundCloudFiles}")
    private String path;

    @Autowired
    public UserService(UserRepository userRepository, MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.mailSenderService = mailSenderService;
    }

    public int generateFiveDigitNumber() {
        Random random = new Random();
        return 10000 + random.nextInt(90000);
    }

    public String[] checkAvatarAndLoadAvatar(String login, MultipartFile avatarFile){
        String[] res = new String[2]; // res[0] - avatarUrl, res[1] - message with error or successful

        String avatarUrlKey = generateKeyForAvatarUrl();
        try {
            byte[] bytesOfAvatarFile = avatarFile.getBytes();

            String originalFilenameOfAvatarFile = avatarFile.getOriginalFilename();

            if (originalFilenameOfAvatarFile != null && !avatarFile.isEmpty()) {
                String fileExtensionAvatar = originalFilenameOfAvatarFile.substring(originalFilenameOfAvatarFile.lastIndexOf(".") + 1);

                String avatarUrlKeyWithExtensionAvatarFile = avatarUrlKey + "." + fileExtensionAvatar;

                Path pathToAvatarFile = Paths.get(path + "avatar/@" + login + "/" + avatarUrlKeyWithExtensionAvatarFile);
                Files.createDirectories(pathToAvatarFile.getParent());
                Files.write(pathToAvatarFile, bytesOfAvatarFile);

                res[0] = url + "files/avatar/@" + login + "/" + avatarUrlKeyWithExtensionAvatarFile;
            } else {
                res[1] = "Add photo";
                return res;
            }
        } catch (IOException e) {
            res[1] = "Add photo";
            return res;
        }

        res[1] = "successful";
        return res;
    }

    public void checkAndAddUserForOauth2(String username, String email, String avatarUrl) {
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

        mailSenderService.send(userEntity.getEmail(), "Reset password", message);

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
            int lengthToShow = 2;
            String maskedUsername = username.substring(0, Math.min(lengthToShow, username.length())) + "*****";
            return maskedUsername + "@" + domain;
        }
        return email;
    }
}