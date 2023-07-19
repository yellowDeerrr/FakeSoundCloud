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

import java.util.List;

@Controller
public class UserAccount {
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private UsersRepository usersRepository;
    @GetMapping("/@{accountName}")
    public String getPageAccountName(@PathVariable String accountName, Model model){
        List<Tracks> tracksList = tracksRepository.findByAuthor(accountName);
        Users user = usersRepository.findByUsername(accountName);
        if (user != null){
            model.addAttribute("user", user);
            if (!tracksList.isEmpty()){
                model.addAttribute("songs", tracksList);
            } else{
                model.addAttribute("nullSong", "Author hasn't songs");
            }
        }else{
            model.addAttribute("errorMessage", "Account isn't exist");
        }


        return "account";
    }
}
