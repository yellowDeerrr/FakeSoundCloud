package com.application.soundcloud.controllers.admin.tables.general;

import com.application.soundcloud.repositories.SaveUserAgentCodeRepository;
import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.repositories.analytic.UserAgentRepository;
import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.logs.SqlQueryLogService;
import com.application.soundcloud.tables.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/tables/general")
public class UsersController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SaveUserAgentCodeRepository saveUserAgentCodeRepository;
    @Autowired
    private UserAgentRepository userAgentRepository;
    @Autowired
    private SqlQueryLogService sqlQueryLogService;

    @GetMapping("/users")
    public String getUsers(Model model){
        model.addAttribute("table", userRepository.findAll());

        return "admin/tables/general/users/users";
    }

    @GetMapping("/users/edit")
    public String editRecordPage(Model model, @RequestParam Long id){
        Optional<UserEntity> userEntity = userRepository.findById(id);

        if (userEntity.isPresent()){
            model.addAttribute("isExist", true);
            model.addAttribute("userRecord", userEntity);
        }else{
            model.addAttribute("isExist", false);
        }

        return "admin/tables/general/users/editPage";
    }

    @PutMapping("/users/edit")
    public ResponseEntity<String> updateRecord(@RequestBody UserEntity updatedUser) {
        Optional<UserEntity> userEntity = userRepository.findById(updatedUser.getId());

        if (userEntity.isPresent()) {
            if (updatedUser.getUsername() != null) {
                userEntity.get().setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getLogin() != null) {
                userEntity.get().setLogin(updatedUser.getLogin());
            }

            userRepository.save(userEntity.get());
            return ResponseEntity.ok("Record has been updated successfully");
        }

        return ResponseEntity.ok("Record not found");
    }

    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteRecord(@RequestParam Long id, Authentication authentication){
        Object principal = authentication.getPrincipal();
        Optional<UserEntity> userEntity = userRepository.findById(id);

        if (userEntity.isPresent()){
            if (principal instanceof CustomUserDetails userDetails){
                sqlQueryLogService.saveSqlQueryLog("Delete record", userEntity.get().getId(), userEntity.get().getId(), userDetails.getUserEntity().getId(), "\"_users\"");

                saveUserAgentCodeRepository.deleteByUserId(userEntity.get());
                userAgentRepository.deleteByUserId(userEntity.get());
                userRepository.deleteById(id);

                return ResponseEntity.ok("Record deleted successfully");
            }
        }

        return ResponseEntity.badRequest().body("Not found record");
    }
}
