package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.RoleRepository;
import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.services.MailSender;
import com.application.soundcloud.services.UserService;
import com.application.soundcloud.tables.Role;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.*;

@Controller
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;
    @Autowired
    private RoleRepository roleRepository;

    @Value("${url}")
    private String url;

    @Value("${pathToSoundCloudFiles}")
    private String path;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userEntity", new UserEntity());

        return "signup_form";
    }

    @PostMapping("/register")
    public String checkInfoUserWhileRegistration(Model model, UserEntity userEntity, @RequestParam MultipartFile avatarFile) {
        String login = userEntity.getLogin();

        if (userRepository.findByLogin(userEntity.getLogin()) != null){
            model.addAttribute("errorMessage", "Login is already using");
            return "signup_form";
        }if (userRepository.findByEmail(userEntity.getEmail()) != null){
            model.addAttribute("errorMessage", "Email is already using");
            return "signup_form";
        }if (userRepository.findByUsername(userEntity.getUsername()) != null){
            model.addAttribute("errorMessage", "Username is already using");
        }
        if (avatarFile != null && !avatarFile.isEmpty()){
            String avatarUrlKey = userService.generateKeyForAvatarUrl();
            try {
                byte[] bytesOfAvatarFile = avatarFile.getBytes();

                String originalFilenameOfAvatarFile = avatarFile.getOriginalFilename();

                if (originalFilenameOfAvatarFile != null && !avatarFile.isEmpty()) {
                    String fileExtensionAvatar = originalFilenameOfAvatarFile.substring(originalFilenameOfAvatarFile.lastIndexOf(".") + 1);

                    String avatarUrlKeyWithExtensionAvatarFile = avatarUrlKey + "." + fileExtensionAvatar;

                    Path pathToAvatarFile = Paths.get(path + "avatar/@" + login + "/" + avatarUrlKeyWithExtensionAvatarFile);
                    Files.createDirectories(pathToAvatarFile.getParent());
                    Files.write(pathToAvatarFile, bytesOfAvatarFile);

                    userEntity.setAvatarUrl(url + "files/avatar/@" + login + "/" + avatarUrlKeyWithExtensionAvatarFile);
                } else {
                    model.addAttribute("errorMessage", "Add photo");
                    return "signup_form";
                }
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Error server");
                return "signup_form";
            }
        }else if (avatarFile == null || avatarFile.isEmpty()){
            userEntity.setAvatarUrl(url + "files/avatar/standard/KpH8YmV4eT.jpg");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);

        userEntity.setUrlActivationCode(UUID.randomUUID().toString());
        userEntity.setActivationCode(generateFiveDigitNumber());

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        userEntity.setRoles(Collections.singletonList(roles));

        userRepository.save(userEntity);

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to FakeSoundcloud. Your activation code: %s",
                userEntity.getUsername(),
                userEntity.getActivationCode());

        mailSender.send(userEntity.getEmail(), "Activation code", message);

        return "redirect:/activate/" + userEntity.getUrlActivationCode();
    }

    public int generateFiveDigitNumber() {
        Random random = new Random();
        return 10000 + random.nextInt(90000);
    }
}
