package com.application.soundcloud.controllers.admin.tables.general;

import com.application.soundcloud.repositories.analytic.UserAgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tables/general")
public class UserAgentController {
    @Autowired
    private UserAgentRepository userAgentRepository;

    @GetMapping("/user-agent")
    public String getUsersAgent(Model model){
        model.addAttribute("table", userAgentRepository.findAll());

        return "admin/tables/general/user-agent/user-agent";
    }
}
