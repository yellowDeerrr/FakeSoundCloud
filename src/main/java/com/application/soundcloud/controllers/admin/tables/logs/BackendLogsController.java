package com.application.soundcloud.controllers.admin.tables.logs;

import com.application.soundcloud.repositories.logs.BackendLogRepository;
import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.logs.SqlQueryLogService;
import com.application.soundcloud.tables.logs.BackendLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/tables/logs")
public class BackendLogsController {
    @Autowired
    private BackendLogRepository backendLogRepository;
    @Autowired
    private SqlQueryLogService sqlQueryLogService;
    @GetMapping("/backend")
    public String getBackendLogs(Model model){
        model.addAttribute("table", backendLogRepository.findAll());

        return "admin/tables/logs/backend/backend";
    }

    @GetMapping("/backend/edit")
    public String editRecordPage(Model model, @RequestParam Long id){
        Optional<BackendLog> backendLog = backendLogRepository.findById(id);

        if (backendLog.isPresent()){
            model.addAttribute("isExist", true);
            model.addAttribute("newBackendLogRecord", new BackendLog());
            model.addAttribute("backendLogRecord", backendLog);
        }else{
            model.addAttribute("isExist", false);
        }

        return "admin/tables/logs/backend/editPage";
    }

    @DeleteMapping("/backend/delete")
    public ResponseEntity<String> deleteRecord(@RequestParam Long id, Authentication authentication){
        Object principal = authentication.getPrincipal();
        Optional<BackendLog> backendLog = backendLogRepository.findById(id);

        if (backendLog.isPresent()){
            if (principal instanceof CustomUserDetails userDetails){
                sqlQueryLogService.saveSqlQueryLog("Delete log", backendLog.get().getId(), backendLog.get().getUserId(), userDetails.getUserEntity().getId(), "\"backend_logs\"");
                backendLogRepository.deleteById(id);

                return ResponseEntity.ok("Record deleted successfully");
            }
        }

        return ResponseEntity.badRequest().body("Not found record");
    }
}
