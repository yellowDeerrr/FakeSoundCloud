package com.application.soundcloud.cotrollers;

import com.application.soundcloud.EncryptToBase64;
import com.application.soundcloud.repositores.TracksRepository;
import com.application.soundcloud.tables.Tracks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private TracksRepository tracksRepository;
    @GetMapping("/")
    public String getMainPage(Model model) {
        List<Tracks> songs = tracksRepository.findAll();
        EncryptToBase64.encrypteTracksImageAndSongToBase64(songs);
        // choose first 8 elements
        List<Tracks> first8Songs = songs.subList(0, Math.min(songs.size(), 8));

        model.addAttribute("songs", first8Songs);

        return "main";
    }
}
