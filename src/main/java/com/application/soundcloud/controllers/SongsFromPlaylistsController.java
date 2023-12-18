package com.application.soundcloud.controllers;

import com.application.soundcloud.payload.request.AddSongToPlaylistRequest;
import com.application.soundcloud.security.CustomUserDetails;
import com.application.soundcloud.services.PlaylistsService;
import com.application.soundcloud.services.SongFromPlaylistService;
import com.application.soundcloud.services.SongService;
import com.application.soundcloud.tables.Playlists;
import com.application.soundcloud.tables.SongFromPlaylist;
import com.application.soundcloud.tables.Tracks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/playlist/song")
public class SongsFromPlaylistsController {
    @Autowired
    private SongFromPlaylistService songFromPlaylistService;
    @Autowired
    private PlaylistsService playlistsService;
    @Autowired
    private SongService songService;
    @PostMapping("/add")
    public ResponseEntity<?> addSongToPlaylist(@Valid @RequestBody AddSongToPlaylistRequest addSongToPlaylistRequest, Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String songKey = addSongToPlaylistRequest.getSongKey();
        String playlistCode = addSongToPlaylistRequest.getPlaylistCode();

        Tracks song = songService.getSongByCode(songKey);
        Playlists playlist = playlistsService.getPlaylist(playlistCode);
        if (song != null || playlist != null){
            if (customUserDetails.getUserEntity().getUUID().equals(playlist.getUUID()))
                songFromPlaylistService.addSong(songKey, playlistCode);
            else
                return ResponseEntity.badRequest().body("Doesn't has privileges");
        }else
            return ResponseEntity.badRequest().body("Not found");
        return ResponseEntity.ok().body("Successful");
    }
}
