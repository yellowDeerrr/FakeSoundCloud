package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.analytic.UserAgentRepository;
import com.application.soundcloud.services.UserService;
import com.application.soundcloud.tables.analytic.UserAgentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserAgentRepository userAgentRepository;
    @GetMapping("/admin")
    public Map<String, Double> getUsersByCountryPercentage() {
        return userService.getUsersByCountryPercentage();
    }

    @GetMapping("/admin/user-agent")
    public List<UserAgentEntity> userAgent(){
        return userAgentRepository.findAll();
    }
}
