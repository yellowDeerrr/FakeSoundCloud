package com.application.soundcloud.controllers.rest;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.application.soundcloud.exception.TokenRefreshException;
import com.application.soundcloud.payload.request.LoginRequest;
import com.application.soundcloud.payload.request.SignupRequest;
import com.application.soundcloud.payload.response.MessageResponse;
import com.application.soundcloud.repositories.RoleRepository;
import com.application.soundcloud.repositories.SaveUserAgentCodeRepository;
import com.application.soundcloud.repositories.UserRepository;
import com.application.soundcloud.security.CustomAuthenticationManager;
import com.application.soundcloud.security.CustomAuthenticationProvider;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private CustomAuthenticationManager authenticationManager;
//    @Autowired
//    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    public UserAgentService userAgentService;
    @Autowired
    private SaveUserAgentCodeRepository saveUserAgentCodeRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUserEntity().getId());

        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body("Successful");
    }
//
//    @GetMapping("/signout")
//    public ResponseEntity<?> logoutUser() {
//        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principle.toString() != "anonymousUser") {
//            Long userId = ((CustomUserDetails) principle).getUserEntity().getId();
//            refreshTokenService.deleteByUserId(userId);
//        }
//
//        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
//        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
//                .body(new MessageResponse("You've been signed out!"));
//    }




    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new MessageResponse("Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
    }
}
