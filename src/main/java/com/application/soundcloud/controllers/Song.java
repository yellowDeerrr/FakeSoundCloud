package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.LikesRepository;
import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.PlaylistsService;
import com.application.soundcloud.tables.Likes;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class Song {
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private PlaylistsService playlistsService;

    @GetMapping("/@{accountName}/{songName}")
    public String getPageWithSong(@PathVariable String accountName, @PathVariable String songName, Authentication authentication, Model model){
        UserEntity author = userRepository.findByUsername(accountName);
        if (author != null){
            Tracks track = tracksRepository.findByAuthorAndSongName(accountName, songName);
            if (track != null){
                if (authentication.getPrincipal() instanceof OAuth2User oAuth2User){
                    Optional<Likes> checkLike = likesRepository.findBySongKeyAndUUID(track.getSongKey(), userRepository.findByUsername(oAuth2User.getAttribute("name") != null ? oAuth2User.getAttribute("name") : oAuth2User.getAttribute("login")).getUUID());
                    if (!checkLike.isPresent()){
                        model.addAttribute("like", true);
                    }else{
                        model.addAttribute("like", false);
                    }
                }else if (authentication.getPrincipal() instanceof CustomUserDetails userDetails){
                    Optional<Likes> checkLike = likesRepository.findBySongKeyAndUUID(track.getSongKey(), userDetails.getUserEntity().getUUID());

                    if (!checkLike.isPresent()){
                        model.addAttribute("like", true);
                    }else{
                        model.addAttribute("like", false);
                    }

                    model.addAttribute("userPlaylists", playlistsService.getUserPlaylists(userDetails.getUserEntity().getUUID()));
                }
                model.addAttribute("song", track);
            }else{
                model.addAttribute("errorMessage", "Song isn't exist");
            }
        }else{
            model.addAttribute("errorMessage", "Account isn't exist");
        }

        return "song";
    }
}
