package com.application.soundcloud.controllers;

import com.application.soundcloud.payload.request.LoginRequest;
import com.application.soundcloud.payload.response.MessageResponse;
import com.application.soundcloud.repositories.SaveUserAgentCodeRepository;
import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.security.jwt.JwtUtils;
import com.application.soundcloud.services.RefreshTokenService;
import com.application.soundcloud.services.analytic.UserAgentService;
import com.application.soundcloud.tables.RefreshToken;
import com.application.soundcloud.tables.SaveUserAgentCode;
import com.application.soundcloud.tables.UserEntity;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
public class LoginController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RefreshTokenService refreshTokenService;
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

    @PostMapping("/api/auth/signout")
    public String logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle.toString() != "anonymousUser") {
            Long userId = ((CustomUserDetails) principle).getUserEntity().getId();
            refreshTokenService.deleteByUserId(userId);
        }

        jwtUtils.getCleanJwtCookie();
        jwtUtils.getCleanJwtRefreshCookie();

        return "redirect:/logout";
    }

    @GetMapping("/login-successful")
    public String checkUserAgentCodeAndRedirect(Authentication authentication, HttpServletRequest request) throws IOException, GeoIp2Exception {
        if (authentication == null && !authentication.isAuthenticated())
            return "redirect:/login";

        String userAgentString  = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails){
            jwtUtils.generateJwtCookie(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUserEntity().getId());
            jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

            UserEntity userEntity = userDetails.getUserEntity();

            SaveUserAgentCode saveUserAgentCode = saveUserAgentCodeRepository.findByUserId(userEntity);
            if (saveUserAgentCode == null || saveUserAgentCode.getCode() == null)
                return "redirect:/";
            else {
                userAgentService.addUserAgentInDB(userEntity, userAgent, request.getRemoteAddr());

                saveUserAgentCode.setCode(null);
                saveUserAgentCodeRepository.save(saveUserAgentCode);
            }
        }

        return "redirect:/";
    }


}
