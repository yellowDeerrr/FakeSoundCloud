package com.application.soundcloud.payload.request;

import javax.validation.constraints.NotBlank;

public class AddSongToPlaylistRequest {
    @NotBlank
    private String songKey;
    @NotBlank
    private String playlistCode;

    public String getSongKey() {
        return songKey;
    }

    public void setSongKey(String songKey) {
        this.songKey = songKey;
    }

    public String getPlaylistCode() {
        return playlistCode;
    }

    public void setPlaylistCode(String playlistCode) {
        this.playlistCode = playlistCode;
    }
}
