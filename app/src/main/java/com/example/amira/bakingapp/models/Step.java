package com.example.amira.bakingapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "step")
public class Step {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int Id;
    @ColumnInfo(name = "number")
    private int number;
    @ColumnInfo(name = "description")
    private String Description;
    @ColumnInfo(name = "sDescription")
    private String ShortDescription;
    @ColumnInfo(name = "video")
    private String Video;
    @ColumnInfo(name = "thumbnail")
    private String Thumbnail;
    @ForeignKey(entity = Recipe.class , parentColumns = "_id" , childColumns = "recipeId")
    @ColumnInfo(name = "recipeId")
    private int RecipeId;

    @Ignore
    public Step(){

    }
    public Step(int Id, String Description , String ShortDescription , String Video , String Thumbnail , int RecipeId){
        this.Id = Id;
        this.Description = Description;
        this.ShortDescription = ShortDescription;
        this.Video = Video;
        this.Thumbnail = Thumbnail;
        this.RecipeId = RecipeId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRecipeId() {
        return RecipeId;
    }

    public void setRecipeId(int recipeId) {
        RecipeId = recipeId;
    }

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
