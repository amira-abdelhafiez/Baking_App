package com.example.amira.bakingapp.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.adapters.RecipesAdapter;
import com.example.amira.bakingapp.data.DataContract;
import com.example.amira.bakingapp.models.Ingredient;
import com.example.amira.bakingapp.models.Recipe;
import com.example.amira.bakingapp.models.Step;
import com.example.amira.bakingapp.utils.JsonUtils;
import com.example.amira.bakingapp.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> , RecipesAdapter.ItemOnClickHandler{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int RECIPES_LOADER_ID = 107;

    private LoaderManager mLoaderManager;

    private RecipesAdapter mAdapter;

    @BindView(R.id.rv_recipes) RecyclerView mRecipesRecyclerView;
    @BindView(R.id.pb_recipes) ProgressBar mLoadingProgressBar;

    private Recipe[] mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAdapter = new RecipesAdapter(this);
        mRecipesRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false );
        mRecipesRecyclerView.setLayoutManager(layoutManager);


        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(RECIPES_LOADER_ID , null , this);
        getRecipesData();

    }

    private void getRecipesData(){

        Loader<String> recipesLoader = mLoaderManager.getLoader(RECIPES_LOADER_ID);
        if(recipesLoader != null){
            mLoaderManager.restartLoader(RECIPES_LOADER_ID , null , this);
        }else{
            mLoaderManager.initLoader(RECIPES_LOADER_ID , null , this);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        if(id == RECIPES_LOADER_ID){

            return new AsyncTaskLoader<String>(this) {
                @Nullable String mRecipesData = null;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    if(mRecipes != null){
                        deliverResult(mRecipesData);
                    }else{
                        forceLoad();
                    }
                }

                @Nullable
                @Override
                public String loadInBackground() {
                    URL bakingUrl = NetworkUtils.buildBakingDataUrl();
                    String data = null;
                    try {
                        data = NetworkUtils.getBakingData(bakingUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return data;
                }

                @Override
                public void deliverResult(@Nullable String data) {
                    mRecipesData = data;
                    super.deliverResult(data);
                }
            };
        }else{
            return new AsyncTaskLoader<String>(this) {
                @Nullable
                @Override
                public String loadInBackground() {
                    return null;
                }
            };
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if(data != null && !data.isEmpty()) {
            mRecipes = JsonUtils.ParseRecipesData(data);
            Log.d("Data" , mRecipes.toString());
            saveData();
            mAdapter.setmRecipes(mRecipes);
        }else{
            Log.d("Data" , "Data null or empty");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    private void saveData(){
        ContentResolver contentResolver = getContentResolver();
        ContentValues recipeValues;
        for (Recipe recipe : mRecipes) {
            recipeValues = new ContentValues();
            recipeValues.put(DataContract.RecipeEntry.ID_COL , recipe.getId());
            recipeValues.put(DataContract.RecipeEntry.SERVINGS_COL , recipe.getServings());
            recipeValues.put(DataContract.RecipeEntry.NAME_COL , recipe.getName());
            recipeValues.put(DataContract.RecipeEntry.IMAGE_COL , recipe.getImage());
            contentResolver.insert(DataContract.RecipeEntry.CONTENT_URI ,recipeValues );

            ArrayList<Step> steps = recipe.getSteps();
            ArrayList<Ingredient> ingredients = recipe.getIngredients();

            ContentValues stepValues ;
            for(Step step : steps){
                stepValues = new ContentValues();
                stepValues.put(DataContract.StepEntry.ID_COL , step.getId());
                stepValues.put(DataContract.StepEntry.S_DESCRIPTION_COL , step.getShortDescription());
                stepValues.put(DataContract.StepEntry.DESCRIPTION_COL , step.getDescription());
                stepValues.put(DataContract.StepEntry.VIDEO_COL , step.getVideo());
                stepValues.put(DataContract.StepEntry.THUMBNAIL_COL , step.getThumbnail());
                stepValues.put(DataContract.StepEntry.RECIPE_ID_COL , step.getRecipeId());
                contentResolver.insert(DataContract.StepEntry.CONTENT_URI , stepValues);
            }

            ContentValues ingredientsValues ;
            for(Ingredient ingredient : ingredients){
                ingredientsValues = new ContentValues();
                ingredientsValues.put(DataContract.IngredientEntry.MEASURE_COL , ingredient.getMeasure());
                ingredientsValues.put(DataContract.IngredientEntry.QUANTITY_COL , ingredient.getQuantity());
                ingredientsValues.put(DataContract.IngredientEntry.NAME_COL , ingredient.getName());
                ingredientsValues.put(DataContract.IngredientEntry.RECIPE_ID , ingredient.getRecipeId());
                contentResolver.insert(DataContract.IngredientEntry.CONTENT_URI , ingredientsValues);
            }
        }
        Log.d(LOG_TAG , "Data Saved Successfully");
    }
    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(MainActivity.this , RecipeDetailActivity.class);
        int id = mRecipes[position].getId();
        // put the extra here
        intent.putExtra(Intent.EXTRA_TEXT , id);
        startActivity(intent);
    }
}
