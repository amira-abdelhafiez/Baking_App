package com.example.amira.bakingapp.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredient WHERE recipeId = :RecipeId")
    Cursor getIngredients(int RecipeId);

    @Query("SELECT * FROM ingredient WHERE _id = :Id")
    Cursor getIngredientById(int Id);

    @Insert
    long insert(Ingredient ingredient);
}
