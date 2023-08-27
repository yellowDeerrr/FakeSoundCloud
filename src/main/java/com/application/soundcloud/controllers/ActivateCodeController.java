package com.application.soundcloud.controllers;

import com.application.soundcloud.services.UserService;
import com.application.soundcloud.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActivateCodeController {
    private String maskedEmail;
    @Autowired
    private UserService userService;
    @GetMapping("/activate/{urlForActivationCode}")
    public String showActivatePage(@PathVariable String urlForActivationCode, Model model){
        User isActivated = userService.isActivateUser(urlForActivationCode);
        maskedEmail = maskEmail(isActivated.getEmail());
        if (isActivated != null && isActivated.getUrlForActivationCode() != null) {
            model.addAttribute("isActivated", true);
            model.addAttribute("maskedEmail", maskedEmail);
        }else {
            model.addAttribute("isActivated", false);
        }
        return "activate";
    }

    @PostMapping("/activate/{urlForActivationCode}")
    public String checkActivateCode(@PathVariable String urlForActivationCode, @RequestParam Integer code, Model model){
        User checkActivationCode = userService.checkActivationCode(urlForActivationCode, code);

        if (checkActivationCode != null)
            return "register_success";

        model.addAttribute("isActivated", true);
        model.addAttribute("errorMessage", "Wrong code, try again");
        model.addAttribute("maskedEmail", maskedEmail);

        return "activate";
    }

    private String maskEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length == 2) {
            String username = parts[0];
            String domain = parts[1];
            int lengthToShow = 2; // Кількість символів, які залишити видимими
            String maskedUsername = username.substring(0, Math.min(lengthToShow, username.length())) + "*****";
            return maskedUsername + "@" + domain;
        }
        return email;
    }
}
