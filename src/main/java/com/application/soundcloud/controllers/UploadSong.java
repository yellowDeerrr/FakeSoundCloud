package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.services.SongService;
import com.application.soundcloud.tables.Tracks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

@Controller
@RequestMapping("/new")
public class UploadSong {
    @Autowired
    private TracksRepository tracksRepository;

    @Autowired
    private SongService songService;

    @GetMapping("/song")
    public String getPageForUploadSong() {
        return "newSong";
    }

    @PostMapping("/song")
    public String checkInfoForUploadSong(@RequestParam MultipartFile avatarSong,
                                         @RequestParam MultipartFile song,
                                         @RequestParam String nameSong,
                                         Model model,
                                         Authentication authentication) {
        String nameAuthor;

        if (authentication == null || !authentication.isAuthenticated()) {
            // Redirect to the homepage if the user is not authenticated
            return "redirect:/";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User){
            OAuth2User oAuth2User = (OAuth2User) principal;
            nameAuthor = oAuth2User.getAttribute("name") != null ? oAuth2User.getAttribute("name") : oAuth2User.getAttribute("login");
        } else if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            nameAuthor = userDetails.getUsername();
        }else{
            return "redirect:/error";
        }

        // Додайте інші необхідні дані з об'єкта CustomUserDetails
        Tracks checkTrack = tracksRepository.findByAuthorAndSongName(nameAuthor, nameSong);
        if (checkTrack == null) {
            String avatarSongKey = songService.generateKey();
            String songKey = songService.generateKey();
            try {
                byte[] bytesOfSong = song.getBytes();
                byte[] bytesOfAvatarSong = avatarSong.getBytes();

                String originalFilenameOfSongFile = song.getOriginalFilename();
                String originalFilenameOfAvatarSongFile = avatarSong.getOriginalFilename();

                if ((originalFilenameOfSongFile != null && !song.isEmpty()) && (originalFilenameOfAvatarSongFile != null && !avatarSong.isEmpty())) {
                    String fileExtensionSong = originalFilenameOfSongFile.substring(originalFilenameOfSongFile.lastIndexOf(".") + 1);
                    String fileExtensionAvatarSong = originalFilenameOfAvatarSongFile.substring(originalFilenameOfAvatarSongFile.lastIndexOf(".") + 1);

                    String songKeyWithExtensionSong = songKey + "." + fileExtensionSong;
                    String avatarSongKeyWithExtension = avatarSongKey + "." + fileExtensionAvatarSong;

                    Path pathToSong = Paths.get("F:\\Java\\intellji\\spring\\projects\\SoundCloud\\src\\main\\resources\\static\\files\\tracks\\@" + nameAuthor + "\\" + songKeyWithExtensionSong);
                    Path pathToAvatarSong = Paths.get("F:\\Java\\intellji\\spring\\projects\\SoundCloud\\src\\main\\resources\\static\\files\\avatarSong\\@" + nameAuthor + "\\" + avatarSongKeyWithExtension);

                    Files.createDirectories(pathToSong.getParent());
                    Files.createDirectories(pathToAvatarSong.getParent());

                    Files.write(pathToSong, bytesOfSong);
                    Files.write(pathToAvatarSong, bytesOfAvatarSong);

                    Tracks newTrack = new Tracks(nameSong, nameAuthor, 0, new Timestamp(System.currentTimeMillis()), songKey, songKeyWithExtensionSong, avatarSongKeyWithExtension);
                    tracksRepository.save(newTrack);
                    model.addAttribute("message", "Successful");
                } else {
                    model.addAttribute("message", "Add photo");
                }
            } catch (IOException e) {
                model.addAttribute("message", "Unsuccessful");
            }
        } else {
            model.addAttribute("message", "Error track");
        }

        return "newSong";
    }
}
