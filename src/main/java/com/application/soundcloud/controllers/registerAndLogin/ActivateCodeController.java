package com.application.soundcloud.controllers.registerAndLogin;

import com.application.soundcloud.services.UserService;
import com.application.soundcloud.tables.UserEntity;
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
    @GetMapping("/activate/{urlActivationCode}")
    public String showActivatePage(@PathVariable String urlActivationCode, Model model){
        UserEntity isActivated = userService.isActivateUser(urlActivationCode);

        if (isActivated != null && isActivated.getUrlActivationCode() != null) {
            maskedEmail = userService.maskEmail(isActivated.getEmail());

            model.addAttribute("isActivated", true);
            model.addAttribute("maskedEmail", maskedEmail);
        }else {
            model.addAttribute("isActivated", false);
        }
        return "activate";
    }

    @PostMapping("/activate/{urlActivationCode}")
    public String checkActivateCode(@PathVariable String urlActivationCode, @RequestParam Integer code, Model model){
        UserEntity checkActivationCode = userService.checkActivationCode(urlActivationCode, code);

        if (checkActivationCode != null)
            return "register_success";

        model.addAttribute("isActivated", true);
        model.addAttribute("errorMessage", "Wrong code, try again");
        model.addAttribute("maskedEmail", maskedEmail);

        return "activate";
    }
}
