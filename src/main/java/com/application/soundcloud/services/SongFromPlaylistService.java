package com.application.soundcloud.services;

import com.application.soundcloud.repositories.SongFromPlaylistRepository;
import com.application.soundcloud.tables.SongFromPlaylist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongFromPlaylistService {
    @Autowired
    private SongFromPlaylistRepository songFromPlaylistRepository;
    public List<SongFromPlaylist> getSongsFromPlaylist(String playlistCode){
        return songFromPlaylistRepository.findByPlaylistCode(playlistCode);
    }

    public void addSong(String songKey, String playlistCode){
        SongFromPlaylist addSong = new SongFromPlaylist();

        addSong.setSongCode(songKey);
        addSong.setPlaylistCode(playlistCode);

        songFromPlaylistRepository.save(addSong);
    }
}
