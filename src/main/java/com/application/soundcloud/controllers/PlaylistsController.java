package com.application.soundcloud.controllers;

import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.PlaylistsService;
import com.application.soundcloud.services.UserService;
import com.application.soundcloud.tables.Playlists;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class PlaylistsController {
    @Autowired
    private PlaylistsService playlistsService;
    @Autowired
    private UserService userService;
    @GetMapping("/playlist/{code}")
    public String getPlaylistPage(@PathVariable String code, Model model, Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Playlists playlists = playlistsService.getPlaylist(code);
        Optional<UserEntity> ownerOfPlaylist = userService.getUserByUUID(playlists.getUUID());
        if (ownerOfPlaylist.isPresent()){
            if (customUserDetails.getUserEntity().getUUID().equals(playlists.getUUID()))
                model.addAttribute("owner", true);
            else
                model.addAttribute("owner", false);

            model.addAttribute("isAccountPrivate", ownerOfPlaylist.get().getPrivateAccount().equals("close"));
        }

        return "playlist";
    }
    @GetMapping("/you/create/playlist")
    public String getPageForCreatePlaylist(){
        return "createPlaylist";
    }

    @PostMapping("/you/create/playlist")
    public String createPlaylist(@RequestParam String name, Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String code = playlistsService.createPlaylist(name, customUserDetails.getUserEntity().getUUID());
        return "redirect:/playlist/" + code;
    }
}
