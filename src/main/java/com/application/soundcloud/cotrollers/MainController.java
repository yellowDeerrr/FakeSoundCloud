package com.application.soundcloud.cotrollers;

import com.application.soundcloud.EncrypteToBase64;
import com.application.soundcloud.repositores.TracksRepository;
import com.application.soundcloud.tables.Tracks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private TracksRepository tracksRepository;
    @GetMapping("/")
    public String getMainPage(Model model) {
        List<Tracks> songs = tracksRepository.findAll();
        EncrypteToBase64.encrypteTracks(songs);

        // Вибір перших 15 елементів
        List<Tracks> first15Songs = songs.subList(0, Math.min(songs.size(), 8));

        model.addAttribute("songs", first15Songs);

        return "main";
    }
}
