package com.example.amira.bakingapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.amira.bakingapp.models.Ingredient;
import com.example.amira.bakingapp.models.IngredientDao;
import com.example.amira.bakingapp.models.Recipe;
import com.example.amira.bakingapp.models.RecipeDao;
import com.example.amira.bakingapp.models.Step;
import com.example.amira.bakingapp.models.StepDao;

@Database(entities = {Recipe.class , Ingredient.class , Step.class} , version = 1 , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();

    private static final String DATABASE_NAME = "bakingdb";
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext() ,
                        AppDatabase.class , AppDatabase.DATABASE_NAME )
                        .build();
            }
        }
        return sInstance;
    }

    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();
    public abstract StepDao stepDao();
}
