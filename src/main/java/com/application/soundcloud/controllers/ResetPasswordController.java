package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.services.UserService;
import com.application.soundcloud.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResetPasswordController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login/forgot")
    public String getResetPasswordPage(){
        return "enterEmailForResetPassword";
    }

    @PostMapping("/login/forgot")
    public String checkEmailAndSendLink(Model model, @RequestParam String email){
        if (userService.checkEmailAndSendLink(email))
            model.addAttribute("maskedEmail", userService.maskEmail(email));
        else
            model.addAttribute("errorMessage", "Email doesn't exist");

        return "enterEmailForResetPassword";
    }
    @GetMapping("/login/forgot/{urlActivationCodeForResetPassword}")
    public String checkUrlActivationCodeForResetPasswordAndShowPage(@PathVariable String urlActivationCodeForResetPassword, Model model){
        User user = userRepository.findByUrlActivationCodeForResetPassword(urlActivationCodeForResetPassword);

        if (user != null)
            return "newPassword";
        model.addAttribute("errorMessage", "Wrong link, try again");

        return "newPassword";
    }

    @PostMapping("/login/forgot/{urlActivationCodeForResetPassword}")
    public String changePassword(
            @PathVariable String urlActivationCodeForResetPassword,
            @RequestParam String password,
            @RequestParam String newPassword,
            @RequestParam String repeatNewPassword,
            Model model
    ) {
        User user = userRepository.findByUrlActivationCodeForResetPassword(urlActivationCodeForResetPassword);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (user == null) {
            model.addAttribute("errorMessage", "Wrong link, try again");
            return "newPassword";
        }

        if (newPassword.equals(repeatNewPassword) && passwordEncoder.matches(password, user.getPassword())) {
            String encodedPassword = passwordEncoder.encode(newPassword);

            user.setPassword(encodedPassword);
            user.setUrlActivationCodeForResetPassword(null);
            userRepository.save(user);

            model.addAttribute("errorMessage", "Password changed successfully");
        } else {
            model.addAttribute("errorMessage", "Passwords do not match or you entered an incorrect password");
        }

        return "newPassword";
    }


}
