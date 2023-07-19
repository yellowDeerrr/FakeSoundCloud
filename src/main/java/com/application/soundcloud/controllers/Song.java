package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.repositories.UsersRepository;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.Users;
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
    private UsersRepository usersRepository;
    @GetMapping("/@{accountName}/{songName}")
    public String getPageWithSong(@PathVariable String accountName, @PathVariable String songName, Model model){
        Users user = usersRepository.findByUsername(accountName);
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
