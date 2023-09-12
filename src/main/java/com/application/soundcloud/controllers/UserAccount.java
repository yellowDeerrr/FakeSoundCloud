package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class UserAccount {
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${url}")
    private String url;

    @GetMapping("/@{accountName}")
    public String getPageAccountName(@PathVariable String accountName, Model model){
        List<Tracks> tracksList = tracksRepository.findByAuthor(accountName);
        UserEntity userEntity = userRepository.findByUsername(accountName);

        if (userEntity != null){
            model.addAttribute("userEntity", userEntity);
            if (userEntity.getAvatarUrl().contains(url + "files/avatar") && !userEntity.getAvatarUrl().contains(url + "files/avatar/standard")){
                model.addAttribute("userAvatar", "ownUserAvatar");
            }else if (userEntity.getAvatarUrl().equals(url + "files/avatar/standard/KpH8YmV4eT.jpg")){
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
        UserEntity userEntity;
        if (authentication == null || !authentication.isAuthenticated()) {
            // Redirect to the homepage if the user is not authenticated
            return "redirect:/";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;

            String email = oAuth2User.getAttribute("email");
            userEntity = userRepository.findByEmail(email);
            String username = userEntity.getUsername();
            if (userEntity == null){
                return "redirect:/@" + username;
            }

            return "redirect:/@" + userEntity.getLogin();
        } else if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            String username = userDetails.getUsername();
            System.out.println(username);

            return "redirect:/@" + username;
        } else {
            return "redirect:/";
        }
    }
}
