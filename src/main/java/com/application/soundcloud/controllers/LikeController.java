package com.application.soundcloud.controllers;

import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.security.jwt.JwtUtils;
import com.application.soundcloud.services.LikesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


@RestController
public class LikeController {
    @Autowired
    private LikesService likesService;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


    @GetMapping("/getLikeStatus")
    public ResponseEntity<String> getLikeStatus(@RequestParam String songKey, Authentication authentication) {
        String UUID = null;
        logger.error(songKey);

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            UUID = customUserDetails.getUserEntity().getUUID();
        }

        String likeStatus = likesService.getLikeStatus(songKey, UUID);

        if (likeStatus.equals("incorrect UUID or SongKey")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(likeStatus);
        } else {
            return ResponseEntity.ok(likeStatus);
        }
    }


    @PostMapping("/updateLikeStatus")
    public ResponseEntity<String> updateLikeStatus(@RequestBody Map<String, String> requestBody, Authentication authentication) {
        String songKey = requestBody.get("songKey");
        String UUID = null;

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails){
            UUID = customUserDetails.getUserEntity().getUUID();
        }
        String likeStatus = likesService.updateLikeStatus(songKey, UUID);

        if (likeStatus.equals("incorrect UUID or SongKey")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(likeStatus);
        } else {
            return ResponseEntity.ok(likeStatus);
        }
    }
}
