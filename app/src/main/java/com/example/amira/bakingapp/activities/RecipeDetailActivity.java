package com.example.amira.bakingapp.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.models.Ingredient;
import com.example.amira.bakingapp.models.Recipe;
import com.example.amira.bakingapp.models.Step;

public class RecipeDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();

    private static final int DATA_LOADER_ID = 701;
    private Recipe mCurrentRecipe;
    private Ingredient[] mRecipeIngredients;
    private Step[] mRecipeSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent callingIntent = getIntent();
        String id;
        if(callingIntent.hasExtra(Intent.EXTRA_TEXT)){
            id = callingIntent.getStringExtra(Intent.EXTRA_TEXT);
        }

        getData();

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(DATA_LOADER_ID , null , this);
    }

    public void getData(){
        LoaderManager lm = getSupportLoaderManager();
        Loader loader = lm.getLoader(DATA_LOADER_ID);

        if(loader != null){
            lm.restartLoader(DATA_LOADER_ID , null ,  this);

        }else{
            lm.initLoader(DATA_LOADER_ID , null , this);
        }
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        if(id == DATA_LOADER_ID){
            final Recipe mRecipe = null;
            return new AsyncTaskLoader<String>(this) {

                @Nullable
                @Override
                public String loadInBackground() {

                    return null;
                }

                @Override
                public void deliverResult(@Nullable String data) {
                    super.deliverResult(data);
                }
            };
        }else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<String> loader, String data) {
        int id = loader.getId();

        if(id == DATA_LOADER_ID){

        }else{

        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<String> loader) {

    }
}
