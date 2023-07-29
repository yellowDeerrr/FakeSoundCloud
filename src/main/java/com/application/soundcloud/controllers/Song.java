package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Song {
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/@{accountName}/{songName}")
    public String getPageWithSong(@PathVariable String accountName, @PathVariable String songName, Model model){
        User user = userRepository.findByLogin(accountName);
        if (user != null){
            Tracks song = tracksRepository.findByAuthorAndSongName(accountName, songName);
            if (song != null){
                model.addAttribute("song", song);
            }else{
                model.addAttribute("errorMessage", "Song isn't exist");
            }
        }else{
            model.addAttribute("errorMessage", "Account isn't exist");
        }

        return "song";
    }
}
