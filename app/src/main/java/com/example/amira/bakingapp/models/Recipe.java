package com.example.amira.bakingapp.models;

import java.util.ArrayList;

public class Recipe {
    private int Id;
    private String Name;
    private int Servings;
    private String Image;
    private ArrayList<Ingredient> Ingredients;
    private ArrayList<Step> Steps;

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

    public ArrayList<Ingredient> getIngredients() {
        return Ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        Ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return Steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        Steps = steps;
    }

    public String toString(){
        return "Name : " + getName() + " + " + getServings();
    }
}
