package com.application.soundcloud.controllers.admin;

import com.application.soundcloud.services.analytic.UserAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private UserAgentService userAgentService;
    @GetMapping("/admin")
    public String getUsersByCountryPercentage(Model model) {
        Map<String, Double> usersByCountryPercentage = userAgentService.getUsersByCountryPercentage();
        model.addAttribute("usersByCountryPercentage", usersByCountryPercentage);

        Map<String, Double> usersByOperatingSystemGroupPercentage = userAgentService.getUsersByPopularOperatingSystemsPercentage();
        model.addAttribute("usersByOperatingSystemGroupPercentage", usersByOperatingSystemGroupPercentage);

        Map<String, Double> usersByPopularBrowsersPercentage = userAgentService.getUsersByPopularBrowsersPercentage();
        model.addAttribute("usersByPopularBrowsersPercentage", usersByPopularBrowsersPercentage);

        Map<String, Double> usersByPopularDeviceTypesPercentage = userAgentService.getUsersByPopularDeviceTypesPercentage();
        model.addAttribute("usersByPopularDeviceTypesPercentage", usersByPopularDeviceTypesPercentage);

        return "admin/admin";
    }

    @GetMapping("/admin/tables")
    public String getTables(){
        return "admin/generalDB";
    }

    @GetMapping("/admin/docs/search")
    public String getDocsForSearch(){
        return "admin/logs/docsForSearch";
    }
}
