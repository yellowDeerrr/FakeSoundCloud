package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.TracksRepository;
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
        List<Tracks> first8Songs = songs.subList(0, Math.min(songs.size(), 8));

        model.addAttribute("songs", first8Songs);

        return "main";
    }
}
