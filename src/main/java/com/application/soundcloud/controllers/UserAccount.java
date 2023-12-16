package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.LikesService;
import com.application.soundcloud.services.PlaylistsService;
import com.application.soundcloud.tables.Likes;
import com.application.soundcloud.tables.Playlists;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserAccount {
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikesService likesService;
    @Autowired
    private PlaylistsService playlistsService;
    @Value("${url}")
    private String url;

    @GetMapping("/@{accountName}")
    public String getPageAccountName(@PathVariable String accountName, Authentication authentication, Model model){
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
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            model.addAttribute("ownAccount", customUserDetails.getUsername().equals(accountName));
        }else{
            model.addAttribute("errorMessage", "Account isn't exist");
        }


        return "account";
    }

    @GetMapping("/you")
    public String viewYourAccount(Authentication authentication) {
        UserEntity userEntity;

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
        } else if (principal instanceof CustomUserDetails userDetails) {

            String username = userDetails.getUsername();
            System.out.println(username);

            return "redirect:/@" + username;
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/you/likes")
    public String getUserLikes(Authentication authentication, Model model){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        ArrayList<Tracks> userLikes = likesService.getUserLikes(customUserDetails.getUserEntity().getUUID());
        model.addAttribute("songs", userLikes);

        return "userLikes";
    }

    @GetMapping("/you/playlists")
    public String getUserPlaylists(Authentication authentication, Model model){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        List<Playlists> userPlaylists = playlistsService.getUserPlaylists(customUserDetails.getUserEntity().getUUID());
        model.addAttribute("userPlaylists", userPlaylists);

        return "userPlaylists";
    }
}
