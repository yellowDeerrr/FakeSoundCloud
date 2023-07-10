package com.application.soundcloud.cotrollers;

import com.application.soundcloud.repositores.TracksRepository;
import com.application.soundcloud.repositores.UsersRepository;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.application.soundcloud.EncryptToBase64.encrypteTracks;
import static com.application.soundcloud.EncryptToBase64.encrypteUser;

@Controller
public class userAccount {
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private UsersRepository usersRepository;
    @GetMapping("/{accountName}")
    public String getPageAccountName(@PathVariable String accountName, Model model){
        List<Tracks> tracksList = tracksRepository.findByAuthor(accountName);
        Users user = usersRepository.findByUsername(accountName);
        if (user != null){
            encrypteUser(user);
            encrypteTracks(tracksList);
            model.addAttribute("user", user);
            if (!tracksList.isEmpty()){
                model.addAttribute("songs", tracksList);
            } else{
                model.addAttribute("nullSong", "Author hasn't songs");
            }
        }else{
            model.addAttribute("nullSong", "Account isn't exist");
        }


        return "account";
    }
}
