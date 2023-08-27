package com.application.soundcloud.controllers;

import com.application.soundcloud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ActivateCodeController {
    @Autowired
    private UserService userService;
    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model){
        boolean isActivated = userService.activateUser(code);

        if (isActivated)
            model.addAttribute("isActivated", true);
        else
            model.addAttribute("isActivated", false);

        return "activate";
    }
}
