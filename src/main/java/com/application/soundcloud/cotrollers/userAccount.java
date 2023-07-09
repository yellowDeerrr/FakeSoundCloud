package com.application.soundcloud.cotrollers;

import com.application.soundcloud.repositores.TracksRepository;
import com.application.soundcloud.tables.Tracks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class userAccount {
    @Autowired
    private TracksRepository tracksRepository;
    @GetMapping("/{accountName}")
    public String getPageAccountName(@PathVariable String accountName, Model model){
        List<Tracks> tracksList = tracksRepository.findByAuthor(accountName);
        if (!tracksList.isEmpty()){
               return "account";
        }

        return "account";
    }
}
