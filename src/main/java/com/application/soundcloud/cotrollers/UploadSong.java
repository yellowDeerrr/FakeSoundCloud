package com.application.soundcloud.cotrollers;

import com.application.soundcloud.repositores.TracksRepository;
import com.application.soundcloud.repositores.UsersRepository;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;

@Controller
@RequestMapping("/new")
public class UploadSong {
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/song")
    public String getPageForUploadSong(){
        return "newSong";
    }

    @PostMapping("/song")
    public String checkInfoForUploadSong(@RequestParam MultipartFile avatarSong,
                                         @RequestParam MultipartFile song,
                                         @RequestParam String nameSong,
                                         @RequestParam String nameAuthor,
                                         @RequestParam String login,
                                         @RequestParam String password,
                                         Model model) throws IOException {
        Users checkUser = usersRepository.findByUsernameAndLoginAndPassword(nameAuthor, login, password);
        if (checkUser != null){
            Tracks checkTrack = tracksRepository.findByAuthorAndSongName(nameAuthor, nameSong);
            if (checkTrack == null){
                Tracks newTrack = new Tracks(nameSong, nameAuthor, 0, new Timestamp(System.currentTimeMillis()), generateSongKey(), song.getBytes(), avatarSong.getBytes());
                tracksRepository.save(newTrack);

                model.addAttribute("message", "Successful");
            }else{
                model.addAttribute("message", "Error track");
            }
        }else{
            model.addAttribute("message", "Error user");
        }


        return "newSong";
    }

    private String generateSongKey(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            char randomSymbol = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(random.nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length())); // Випадковий символ зі стрічки symbols

            stringBuilder.append(randomSymbol);
        }
        String res = stringBuilder.toString();
        Tracks checkSongKey = tracksRepository.findBySongKey(res);
        if (checkSongKey == null){
            return res;
        } else {
            generateSongKey();
        }

        return null;
    }
}
