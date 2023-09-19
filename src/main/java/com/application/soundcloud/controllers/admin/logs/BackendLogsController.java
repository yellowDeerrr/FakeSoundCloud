package com.application.soundcloud.controllers.admin.logs;

import com.application.soundcloud.repositories.logs.BackendLogRepository;
import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.logs.SqlQueryLogService;
import com.application.soundcloud.tables.logs.BackendLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin/logs")
public class BackendLogsController {
    @Autowired
    private BackendLogRepository backendLogRepository;
    @Autowired
    private SqlQueryLogService sqlQueryLogService;
    @GetMapping("/backend")
    public String getBackendLogs(Model model){
        model.addAttribute("table", backendLogRepository.findAll());

        return "admin/logs/backend";
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
