package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.services.UserService;
import com.application.soundcloud.services.logs.BackendLogService;
import com.application.soundcloud.services.logs.HttpRequestLogService;
import com.application.soundcloud.tables.UserEntity;
import com.application.soundcloud.tables.logs.BackendLog;
import com.application.soundcloud.tables.logs.HttpRequestLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ResetPasswordController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BackendLogService backendLogService;
    @Autowired
    private HttpRequestLogService httpRequestLogService;

    @GetMapping("/login/forgot")
    public String getResetPasswordPage(){
        return "enterEmailForResetPassword";
    }

    @PostMapping("/login/forgot")
    public String checkEmailAndSendLink(Model model, @RequestParam String email, Authentication authentication){
        BackendLog backendLog = new BackendLog();

        backendLog.setUserId(getUserIdFromAuthenticatedUser(authentication));

        if (userService.checkEmailAndSendLink(email)){
            model.addAttribute("maskedEmail", userService.maskEmail(email));
            backendLogService.sendUrlForResetPasswordOnEmail(email, backendLog);
        } else{
            model.addAttribute("errorMessage", "Email doesn't exist \nOr we already sent url for reset password");
            backendLogService.errorSendUrlForResetPasswordOnEmail(email, backendLog);
        }

        return "enterEmailForResetPassword";
    }
    @GetMapping("/login/forgot/{urlActivationCodeForResetPassword}")
    public String checkUrlActivationCodeForResetPasswordAndShowPage(HttpServletResponse response, HttpServletRequest request, @PathVariable String urlActivationCodeForResetPassword, Authentication authentication, Model model){
        HttpRequestLog httpRequestLog = new HttpRequestLog();
        UserEntity userEntity = userRepository.findByUrlActivationCodeForResetPassword(urlActivationCodeForResetPassword);

        httpRequestLog.setUserId(getUserIdFromAuthenticatedUser(authentication));

        if (userEntity != null) {
            model.addAttribute("wrongLink", "W");
            return "newPassword";
        }
        model.addAttribute("errorMessage", "Wrong link");
        model.addAttribute("wrongLink", "Wrong link");

        httpRequestLog.setStatusCode(response.getStatus());
        httpRequestLog.setMethod(request.getMethod());
        httpRequestLogService.wrongUrlActivationCodeForResetPassword(httpRequestLog, urlActivationCodeForResetPassword);

        return "newPassword";
    }

    @PostMapping("/login/forgot/{urlActivationCodeForResetPassword}")
    public String changePassword(
            @PathVariable String urlActivationCodeForResetPassword,
            @RequestParam String newPassword,
            @RequestParam String repeatNewPassword,
            HttpServletResponse response,
            HttpServletRequest request,
            @RequestBody String requestBody,
            Authentication authentication,
            Model model
    ) {
        Long id = getUserIdFromAuthenticatedUser(authentication);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        BackendLog backendLog = new BackendLog();
        HttpRequestLog httpRequestLog = new HttpRequestLog();
        UserEntity userEntity = userRepository.findByUrlActivationCodeForResetPassword(urlActivationCodeForResetPassword);

        if (userEntity == null) {
            model.addAttribute("wrongLink", "Wrong link");
            model.addAttribute("errorMessage", "Wrong link");

            httpRequestLog.setUserId(id);
            httpRequestLog.setRequestBody(requestBody);
            httpRequestLog.setStatusCode(response.getStatus());
            httpRequestLog.setMethod(request.getMethod());
            httpRequestLogService.wrongUrlActivationCodeForResetPassword(httpRequestLog, urlActivationCodeForResetPassword);

            return "newPassword";
        }

        if (newPassword.equals(repeatNewPassword) || !passwordEncoder.matches(newPassword, userEntity.getPassword())) {
            String encodedPassword = passwordEncoder.encode(newPassword);

            userEntity.setPassword(encodedPassword);
            userEntity.setUrlActivationCodeForResetPassword(null);
            userRepository.save(userEntity);

            model.addAttribute("wrongLink", "W");
            model.addAttribute("errorMessage", "Password changed successfully");

            backendLog.setUserId(id);
            backendLogService.successfulResetPassword(backendLog, urlActivationCodeForResetPassword);
        } else {
            model.addAttribute("wrongLink", "W");
            model.addAttribute("errorMessage", "Passwords do not match or new password match with your password");
        }

        return "newPassword";
    }

    private Long getUserIdFromAuthenticatedUser(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()){
            Object authenticationPrincipal = authentication.getPrincipal();

            return  authenticationPrincipal instanceof UserDetails userDetails ? userRepository.findByLogin(userDetails.getUsername()).getId() :
                    (authenticationPrincipal instanceof OAuth2User oAuth2User ? userRepository.findByEmail(oAuth2User.getAttribute("email")).getId() : null);

        }
        return null;
    }
}
