package com.application.soundcloud.controllers.admin.tables.logs;

import com.application.soundcloud.repositories.logs.HttpRequestLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tables/logs")
public class HttpRequestsController {
    @Autowired
    private HttpRequestLogRepository httpRequestLogRepository;
    @GetMapping("/httpRequests")
    public String getBackendLogs(Model model){
        model.addAttribute("table", httpRequestLogRepository.findAll());

        return "admin/tables/logs/httpRequests/httpRequests";
    }
}
