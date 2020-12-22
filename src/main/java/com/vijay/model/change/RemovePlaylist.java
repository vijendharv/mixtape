package com.vijay.model.change;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RemovePlaylist extends Change {

    @JsonProperty("playlist_id")
    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private int playlistId;

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }
}
