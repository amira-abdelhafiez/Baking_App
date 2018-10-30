package com.example.amira.bakingapp.models;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    long insertRecipe(Recipe insertedRecipe);

    @Query("SELECT * FROM recipe")
    Cursor getRecipes();

    @Query("SELECT * FROM recipe WHERE _id = :Id")
    Cursor getRecipeById(int Id);

    @Query("SELECT COUNT(*) FROM recipe")
    int getCount();
}
