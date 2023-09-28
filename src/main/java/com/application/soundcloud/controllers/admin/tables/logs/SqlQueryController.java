package com.application.soundcloud.controllers.admin.tables.logs;

import com.application.soundcloud.repositories.logs.SqlQueryLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tables/logs")
public class SqlQueryController {
    @Autowired
    private SqlQueryLogRepository sqlQueryLogRepository;
    @GetMapping("/sql")
    public String getBackendLogs(Model model){
        model.addAttribute("table", sqlQueryLogRepository.findAll());

        return "admin/tables/logs/sql/sqlQuery";
    }
}
