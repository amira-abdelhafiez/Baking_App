package com.example.amira.bakingapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Entity(tableName = "recipe")
public class Recipe {
    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int Id;
    @ColumnInfo(name = "name")
    private String Name;
    @ColumnInfo(name = "servings")
    private int Servings;
    @ColumnInfo(name = "image")
    private String Image;
    @Ignore
    private ArrayList<Ingredient> Ingredients;
    @Ignore
    private ArrayList<Step> Steps;

    @Ignore
    public Recipe(){

    }
    public Recipe(int Id , String Name , int Servings , String Image){
        this.Id = Id;
        this.Name = Name;
        this.Servings = Servings;
        this.Image = Image;
        this.Ingredients = Ingredients;
        this.Steps = Steps;
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setServings(int servings) {
        Servings = servings;
    }

    public int getServings() {
        return Servings;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @Ignore
    public ArrayList<Ingredient> getIngredients() {
        return Ingredients;
    }

    @Ignore
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        Ingredients = ingredients;
    }

    @Ignore
    public ArrayList<Step> getSteps() {
        return Steps;
    }

    @Ignore
    public void setSteps(ArrayList<Step> steps) {
        Steps = steps;
    }
}
