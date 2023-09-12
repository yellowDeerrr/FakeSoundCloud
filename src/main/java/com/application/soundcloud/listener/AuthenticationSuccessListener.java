package com.application.soundcloud.listener;

import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.UserService;
import com.application.soundcloud.services.analytic.UserAgentService;
import com.application.soundcloud.tables.UserEntity;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class AuthenticationSuccessListener {
    private final UserService userService;
    private UserAgentService userAgentService;

    @Autowired
    public AuthenticationSuccessListener(UserService userService, UserAgentService userAgentService) {
        this.userService = userService;
        this.userAgentService = userAgentService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event){
        Authentication authentication = event.getAuthentication();

        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String login = oAuth2User.getAttribute("name") != null ? oAuth2User.getAttribute("name") : oAuth2User.getAttribute("login");
            String email = oAuth2User.getAttribute("email");
            String avatarUrl = oAuth2User.getAttribute("picture");
            userService.checkAndAddUserForOauth2(login, email, avatarUrl);
        }
    }
}
