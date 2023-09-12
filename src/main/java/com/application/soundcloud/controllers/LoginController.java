package com.application.soundcloud.controllers;

import com.application.soundcloud.repositories.SaveUserAgentCodeRepository;
import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.analytic.UserAgentService;
import com.application.soundcloud.tables.SaveUserAgentCode;
import com.application.soundcloud.tables.UserEntity;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
public class LoginController {
    @Autowired
    public UserAgentService userAgentService;
    @Autowired
    private SaveUserAgentCodeRepository saveUserAgentCodeRepository;
    @GetMapping("/login")
    public String showLoginForm(){
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/login-successful")
    public String checkUserAgentCodeAndRedirect(Authentication authentication, HttpServletRequest request) throws IOException, GeoIp2Exception {
        if (authentication == null && !authentication.isAuthenticated())
            return "redirect:/login";

        String userAgentString  = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails && authentication.getPrincipal() instanceof UserDetails){
            UserEntity userEntity = userDetails.getUserEntity();

            SaveUserAgentCode saveUserAgentCode = saveUserAgentCodeRepository.findByUserId(userEntity);
            if (saveUserAgentCode == null)
                return "redirect:/";
            if (saveUserAgentCode.getCode() == null)
                return "redirect:/";
            else {
                userAgentService.addUserAgentInDB(userEntity, userAgent, request);

                saveUserAgentCode.setCode(null);
                saveUserAgentCodeRepository.save(saveUserAgentCode);
            }
        }

        return "redirect:/";
    }
}
