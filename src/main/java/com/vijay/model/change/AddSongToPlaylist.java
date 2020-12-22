package com.vijay.model.change;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddSongToPlaylist extends Change {

    @JsonProperty("playlist_id")
    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private int playlistID;

    @JsonProperty("song_id")
    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private int songID;

    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }
}
