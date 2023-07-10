package com.application.soundcloud.cotrollers;

import com.application.soundcloud.EncryptToBase64;
import com.application.soundcloud.repositores.TracksRepository;
import com.application.soundcloud.repositores.UsersRepository;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
                EncryptToBase64.encrypteTrackImageAndSongToBase64(song);
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
