package com.application.soundcloud.controllers.admin.tables.general;

import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.logs.SqlQueryLogService;
import com.application.soundcloud.tables.Tracks;
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
public class TracksController {
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private SqlQueryLogService sqlQueryLogService;

    @GetMapping("/tracks")
    public String getTracks(Model model){
        model.addAttribute("table", tracksRepository.findAll());

        return "admin/tables/general/tracks/tracks";
    }

    @GetMapping("/tracks/edit")
    public String editRecordPage(Model model, @RequestParam Long id){
        Optional<Tracks> tracks = tracksRepository.findById(id);

        if (tracks.isPresent()){
            model.addAttribute("isExist", true);
            model.addAttribute("trackRecord", tracks.get());
        }else{
            model.addAttribute("isExist", false);
        }

        return "admin/tables/general/tracks/editPage";
    }

    @PutMapping("/tracks/edit")
    public ResponseEntity<String> updateRecord(@RequestBody Tracks updatedTrack) {
        Optional<Tracks> trackOptional = tracksRepository.findById(updatedTrack.getId());

        if (trackOptional.isPresent()) {
            if (updatedTrack.getAuthor() != null) {
                trackOptional.get().setAuthor(updatedTrack.getAuthor());
            }
            if (updatedTrack.getSongName() != null) {
                trackOptional.get().setSongName(updatedTrack.getSongName());
            }
            if (updatedTrack.getLikes() != null) {
                trackOptional.get().setLikes(updatedTrack.getLikes());
            }
            if (updatedTrack.getSongKey() != null) {
                trackOptional.get().setSongKey(updatedTrack.getSongKey());
            }
            if (updatedTrack.getSongUrl() != null) {
                trackOptional.get().setSongUrl(updatedTrack.getSongUrl());
            }
            if (updatedTrack.getAvatarSongUrl() != null) {
                trackOptional.get().setAvatarSongUrl(updatedTrack.getAvatarSongUrl());
            }

            tracksRepository.save(trackOptional.get());
            return ResponseEntity.ok("Record has been updated successfully");
        }

        return ResponseEntity.ok("Record not found");
    }

    @DeleteMapping("/tracks/delete")
    public ResponseEntity<String> deleteRecord(@RequestParam Long id, Authentication authentication){
        Object principal = authentication.getPrincipal();
        Optional<Tracks> tracksOptional = tracksRepository.findById(id);

        if (tracksOptional.isPresent()){
            if (principal instanceof CustomUserDetails userDetails){
                sqlQueryLogService.saveSqlQueryLog("Delete record", tracksOptional.get().getId(), tracksOptional.get().getId(), userDetails.getUserEntity().getId(), "\"tracks\"");

                return ResponseEntity.ok("Record deleted successfully");
            }
        }

        return ResponseEntity.badRequest().body("Not found record");
    }
}
