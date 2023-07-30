package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.services.UserService;
import com.application.soundcloud.tables.Tracks;
import com.application.soundcloud.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

@Controller
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/register")
    public String checkInfoUserWhileRegistration(Model model, User user, @RequestParam MultipartFile avatarFile) {
        String login = user.getLogin();

        if (userRepository.findByLogin(user.getLogin()) != null){
            model.addAttribute("errorMessage", "User name is already using");
            return "signup_form";
        }if (userRepository.findByEmail(user.getEmail()) != null){
            model.addAttribute("errorMessage", "Email is already using");
            return "signup_form";
        }if (avatarFile != null && !avatarFile.isEmpty()){
            String avatarUrlKey = userService.generateKeyForAvatarUrl();
            try {
                byte[] bytesOfAvatarFile = avatarFile.getBytes();

                String originalFilenameOfAvatarFile = avatarFile.getOriginalFilename();

                if (originalFilenameOfAvatarFile != null && !avatarFile.isEmpty()) {
                    String fileExtensionAvatar = originalFilenameOfAvatarFile.substring(originalFilenameOfAvatarFile.lastIndexOf(".") + 1);

                    String avatarUrlKeyWithExtensionAvatarFile = avatarUrlKey + "." + fileExtensionAvatar;

                    Path pathToAvatarFile = Paths.get("F:\\Java\\intellji\\spring\\projects\\SoundCloud\\src\\main\\resources\\static\\files\\avatar\\@" + login + "\\" + avatarUrlKeyWithExtensionAvatarFile);
                    Files.createDirectories(pathToAvatarFile.getParent());
                    Files.write(pathToAvatarFile, bytesOfAvatarFile);

                    user.setAvatarUrl("http://localhost:8080/files/avatar/@" + login + "/" + avatarUrlKeyWithExtensionAvatarFile);
                } else {
                    model.addAttribute("errorMessage", "Add photo");
                    return "signup_form";
                }
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Error server");
                return "signup_form";
            }
        }else if (avatarFile == null || avatarFile.isEmpty()){
            user.setAvatarUrl("http://localhost:8080/files/avatar/standard/KpH8YmV4eT.jpg");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        return "register_success";
    }
}
