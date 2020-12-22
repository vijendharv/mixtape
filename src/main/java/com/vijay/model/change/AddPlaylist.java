package com.vijay.model.change;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vijay.model.Playlist;
import com.vijay.model.change.Change;

import java.util.List;

public class AddPlaylist extends Change {

    @JsonFormat(shape= JsonFormat.Shape.STRING)
    @JsonProperty("user_id")
    private int userId;

    @JsonFormat(shape= JsonFormat.Shape.STRING)
    @JsonProperty("song_ids")
    private List<Integer> songIds;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<Integer> songIds) {
        this.songIds = songIds;
    }
}
