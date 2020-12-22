package com.vijay.model;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Song {

    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private int id;
    private String artist;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
