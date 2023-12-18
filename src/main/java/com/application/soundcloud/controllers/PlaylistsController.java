package com.application.soundcloud.controllers;

import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.PlaylistsService;
import com.application.soundcloud.services.SongFromPlaylistService;
import com.application.soundcloud.services.SongService;
import com.application.soundcloud.services.UserService;
import com.application.soundcloud.tables.Playlists;
import com.application.soundcloud.tables.SongFromPlaylist;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PlaylistsController {
    @Autowired
    private PlaylistsService playlistsService;
    @Autowired
    private UserService userService;
    @Autowired
    private SongFromPlaylistService songFromPlaylistService;
    @Autowired
    private SongService songService;
    @Value("${url}")
    private String webPath;
    @GetMapping("/playlist/{code}")
    public String getPlaylistPage(@PathVariable String code, Model model, Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Playlists playlist = playlistsService.getPlaylist(code);
        Optional<UserEntity> ownerOfPlaylist = userService.getUserByUUID(playlist.getUUID());

        if (ownerOfPlaylist.isPresent()){
            if (customUserDetails.getUserEntity().getUUID().equals(playlist.getUUID()) || !ownerOfPlaylist.get().getPrivateAccount().equals("close")){
                if (customUserDetails.getUserEntity().getUUID().equals(playlist.getUUID()))
                    model.addAttribute("owner", true);
                else
                    model.addAttribute("owner", false);

                addAttributes(code, model, playlist);
            }
            else{
                model.addAttribute("public", false);
                model.addAttribute("errorMessage", "You do not have viewing privileges\nOr playlist doesn't exist");
            }
        }

        return "playlist";
    }

    private void addAttributes(@PathVariable String code, Model model, Playlists playlist) {
        model.addAttribute("public", true);
        model.addAttribute("playlistName", playlist.getName());
        model.addAttribute("linkForShare", webPath + "playlist/share/" + playlist.getCode() + "?share=" + playlist.getShareCode());

        List<SongFromPlaylist> songsFromPlaylist = songFromPlaylistService.getSongsFromPlaylist(code);
        if (songsFromPlaylist != null){

            List<Tracks> tracksList = new ArrayList<>();
            for (SongFromPlaylist song : songsFromPlaylist){
                Tracks track = songService.getSongByCode(song.getSongCode());
                if (track != null)
                    tracksList.add(track);
            }
            model.addAttribute("songs", tracksList);
        }
    }

    @GetMapping("/playlist/share/{code}")
    public String sharePlaylist(@RequestParam String share, @PathVariable String code, Model model){
        Optional<Playlists> playlistOptional = playlistsService.checkCodeWithShareCode(code, share);
        if (playlistOptional.isPresent()){
            Playlists playlist = playlistOptional.get();

            addAttributes(code, model, playlist);
        }
        else{
            model.addAttribute("public", false);
            model.addAttribute("errorMessage", "You do not have viewing privileges\nOr playlist doesn't exist");
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
