package com.application.soundcloud.controllers;

import com.application.soundcloud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/admin")
    public Map<String, Double> getUsersByCountryPercentage() {
        return userService.getUsersByCountryPercentage();
    }
}
