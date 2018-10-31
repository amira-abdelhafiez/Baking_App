package com.example.amira.bakingapp.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.models.Ingredient;
import com.example.amira.bakingapp.models.Recipe;
import com.example.amira.bakingapp.models.Step;

public class RecipeDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();

    private static final int INGREDIENTS_LOADER_ID = 701;
    private static final int STEPS_LOADER_ID = 702;

    private Recipe mCurrentRecipe;
    private Cursor mIngredientCursor , mStepsCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mCurrentRecipe = null;
        Intent callingIntent = getIntent();
        if(callingIntent.hasExtra(Intent.EXTRA_TEXT)){
            mCurrentRecipe = callingIntent.getParcelableExtra(Intent.EXTRA_TEXT);
        }

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(INGREDIENTS_LOADER_ID , null , this);
        loaderManager.initLoader(STEPS_LOADER_ID , null , this);

        getIngredientsData();
        getStepsData();
    }

    public void getIngredientsData(){
        LoaderManager lm = getSupportLoaderManager();
        Loader loader = lm.getLoader(INGREDIENTS_LOADER_ID);

        if(loader != null){
            lm.restartLoader(INGREDIENTS_LOADER_ID , null ,  this);

        }else{
            lm.initLoader(INGREDIENTS_LOADER_ID , null , this);
        }
    }

    public void getStepsData(){
        LoaderManager lm = getSupportLoaderManager();
        Loader loader = lm.getLoader(STEPS_LOADER_ID);

        if(loader != null){
            lm.restartLoader(STEPS_LOADER_ID , null ,  this);

        }else{
            lm.initLoader(STEPS_LOADER_ID , null , this);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if(id == INGREDIENTS_LOADER_ID){
            return new AsyncTaskLoader<Cursor>(this) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                }

                @Nullable
                @Override
                public Cursor loadInBackground() {
                    return null;
                }

                @Override
                public void deliverResult(@Nullable Cursor data) {
                    super.deliverResult(data);
                }
            };
        }else if(id == STEPS_LOADER_ID){
            return new AsyncTaskLoader<Cursor>(this) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                }

                @Nullable
                @Override
                public Cursor loadInBackground() {
                    return null;
                }

                @Override
                public void deliverResult(@Nullable Cursor data) {
                    super.deliverResult(data);
                }
            };
        }else{
            Log.d(LOG_TAG , "Invalid Loader Id in onCreateLoader");
            return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        int loaderId = loader.getId();
        if(loaderId == INGREDIENTS_LOADER_ID){

        }else if(loaderId == STEPS_LOADER_ID){

        }else{
            Log.d(LOG_TAG  , "Invalid Loader ID in onLoadFinished");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
