package com.example.amira.bakingapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredient")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int Id;
    @ColumnInfo(name = "name")
    private String Name;
    @ColumnInfo(name = "quantity")
    private double Quantity;
    @ColumnInfo(name = "measure")
    private String Measure;

    //@ForeignKey(entity = Recipe.class , parentColumns = "_id" , childColumns = "recipeId")
    @ColumnInfo(name = "recipeId")
    private int RecipeId;

    @Ignore
    public Ingredient(){

    }
    public Ingredient(int Id , String Name , double Quantity , String Measure , int RecipeId){
        this.Name = Name;
        this.Id = Id;
        this.Quantity = Quantity;
        this.Measure = Measure;
        this.RecipeId = RecipeId;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public int getRecipeId() {
        return RecipeId;
    }

    public void setRecipeId(int recipeId) {
        RecipeId = recipeId;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setMeasure(String measure) {
        Measure = measure;
    }

    public String getMeasure() {
        return Measure;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }

    public double getQuantity() {
        return Quantity;
    }
}
