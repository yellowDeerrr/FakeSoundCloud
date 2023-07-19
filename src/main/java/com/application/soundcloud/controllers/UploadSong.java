package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.userDetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Random;

@Controller
@RequestMapping("/new")
public class UploadSong {
@Autowired
private TracksRepository tracksRepository;

@GetMapping("/song")
public String getPageForUploadSong() {
    return "newSong";
}

@PostMapping("/song")
public String checkInfoForUploadSong(@RequestParam MultipartFile avatarSong,
                                     @RequestParam MultipartFile song,
                                     @RequestParam String nameSong,
                                     @AuthenticationPrincipal OAuth2User principal,
                                     Model model) {
    String nameAuthor = principal.getAttribute("name") != null ? principal.getAttribute("name") : principal.getAttribute("login");

    // Додайте інші необхідні дані з об'єкта CustomUserDetails
    Tracks checkTrack = tracksRepository.findByAuthorAndSongName(nameAuthor, nameSong);
    if (checkTrack == null) {
        String avatarSongKey = generateKey();
        String songKey = generateKey();
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

                Path pathToSong = Paths.get("F:\\Java\\intellji\\spring\\projects\\soundcloud\\src\\main\\resources\\static\\tracks\\@" + nameAuthor + "\\" + songKeyWithExtensionSong);
                Path pathToAvatarSong = Paths.get("F:\\Java\\intellji\\spring\\projects\\soundcloud\\src\\\\main\\resources\\static\\avatarSong\\@" + nameAuthor + "\\" + avatarSongKeyWithExtension);

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

private String generateKey() {
    Random random = new Random();
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < 10; i++) {
        char randomSymbol = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(random.nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length())); // Випадковий символ зі стрічки symbols

        stringBuilder.append(randomSymbol);
    }
    String res = stringBuilder.toString();
    Tracks checkSongKey = tracksRepository.findBySongKey(res);
    if (checkSongKey == null) {
        return res;
    } else {
        generateKey();
    }

    return null;
}
}
