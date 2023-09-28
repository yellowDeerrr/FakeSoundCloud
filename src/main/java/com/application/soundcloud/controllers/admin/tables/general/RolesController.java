package com.application.soundcloud.controllers.admin.tables.general;

import com.application.soundcloud.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tables/general")
public class RolesController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/roles")
    public String getRoles(Model model){
        model.addAttribute("table", roleRepository.findAll());
        return "admin/tables/general/roles/roles";
    }
}
