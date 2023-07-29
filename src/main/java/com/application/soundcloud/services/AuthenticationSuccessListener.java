package com.application.soundcloud.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

@Service
public class AuthenticationSuccessListener {

    private final UserService userService;

    @Autowired
    public AuthenticationSuccessListener(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();

        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            // Handle OAuth2 authentication
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String login = oAuth2User.getAttribute("name") != null ? oAuth2User.getAttribute("name") : oAuth2User.getAttribute("login");
            String email = oAuth2User.getAttribute("email");
            String avatarUrl = oAuth2User.getAttribute("picture");
            userService.checkAndAddUser(login, email, avatarUrl);
        }
    }
}
