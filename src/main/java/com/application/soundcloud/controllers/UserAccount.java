package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.text.Normalizer;
import java.util.List;

@Controller
public class UserAccount {
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/@{accountName}")
    public String getPageAccountName(@PathVariable String accountName, Model model){
        List<Tracks> tracksList = tracksRepository.findByAuthor(accountName);
        User user = userRepository.findByLogin(accountName);
        if (user != null){
            model.addAttribute("user", user);
            if (user.getAvatarUrl().contains("http://localhost:8080/files/avatar") && !user.getAvatarUrl().contains("http://localhost:8080/files/avatar/@standard")){
                model.addAttribute("userAvatar", "ownUserAvatar");
            }else if (user.getAvatarUrl().equals("http://localhost:8080/files/avatar/standard/KpH8YmV4eT.jpg")){
                model.addAttribute("userAvatar", "standard");
            }
            else {
                model.addAttribute("userAvatar", "userOauth2Avatar");
            }if (!tracksList.isEmpty()){
                model.addAttribute("songs", tracksList);
            } else{
                model.addAttribute("nullSong", "Author hasn't songs");
            }
        }else{
            model.addAttribute("errorMessage", "Account isn't exist");
        }


        return "account";
    }

    @GetMapping("/you")
    public String viewYourAccount(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Redirect to the homepage if the user is not authenticated
            return "redirect:/";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User) {
            // Handle OAuth2 authentication
            OAuth2User oAuth2User = (OAuth2User) principal;
            String username = oAuth2User.getAttribute("name") != null ? oAuth2User.getAttribute("name") : oAuth2User.getAttribute("login");
            return "redirect:/@" + username;
        } else if (principal instanceof UserDetails) {
            // Handle standard username/password authentication
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            // You can use userDetails to get more information about the user if needed
            return "redirect:/@" + username;
        } else {
            // Handle other types of authentication (if needed)
            return "redirect:/";
        }
    }
}
