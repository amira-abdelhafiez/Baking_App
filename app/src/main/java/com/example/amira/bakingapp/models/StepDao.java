package com.example.amira.bakingapp.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

@Dao
public interface StepDao {
    @Query("SELECT * FROM step WHERE recipeId = :RecipeId")
    Cursor getSteps(int RecipeId);

    @Query("SELECT * FROM step WHERE _id = :Id")
    Cursor getStepById(int Id);

    @Insert
    long insert(Step step);
}
