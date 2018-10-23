package com.example.amira.bakingapp.models;

public class Step {
    private int Id;
    private String Description;
    private String ShortDescription;
    private String Video;
    private String Thumbnail;

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getVideo() {
        return Video;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return Thumbnail;
    }
}
