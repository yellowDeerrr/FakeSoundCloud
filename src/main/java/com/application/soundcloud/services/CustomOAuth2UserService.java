package com.application.soundcloud.services;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(DefaultOAuth2UserService defaultOAuth2UserService, UserRepository userRepository) {
        this.defaultOAuth2UserService = defaultOAuth2UserService;
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        User user = new User();

        OAuth2User oauth2User = defaultOAuth2UserService.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String avatarUrl = oauth2User.getAttribute("picture");
        String name = oauth2User.getAttribute("name");

        if (userRepository.findByEmail(email) == null) {
            user.setAvatarUrl(avatarUrl);
            user.setEmail(email);
            if (userRepository.findByLogin(name) == null){
                user.setLogin(name);
            }else{
                user.setLogin(generateUniqueLogin(name));
            }if (userRepository.findByUsername(name) == null){
                user.setUsername(name);
            }else{
                user.setUsername(generateUniqueUsername(name));
            }
            userRepository.save(user);
        }

        return oauth2User;
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
