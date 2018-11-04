package com.example.amira.bakingapp.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> , RecipesAdapter.ItemOnClickHandler{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int RECIPES_LOADER_ID = 107;

    private LoaderManager mLoaderManager;

    private RecipesAdapter mAdapter;

    private int Id_Col , Name_Col , Servings_Col , Image_Col;

    @BindView(R.id.rv_recipes) RecyclerView mRecipesRecyclerView;
    @BindView(R.id.pb_recipes) ProgressBar mLoadingProgressBar;
    @BindView(R.id.tv_recipes_loading_error) TextView mErrorLoadingMessage;

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

    private void showErrorMessage(){
        mErrorLoadingMessage.setVisibility(View.VISIBLE);
        mRecipesRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideErrorMessage(){
        mErrorLoadingMessage.setVisibility(View.INVISIBLE);
        mRecipesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoadingBar(){
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingBar(){
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if(id == RECIPES_LOADER_ID){
            return new AsyncTaskLoader<Cursor>(this) {

                Cursor mCursor;
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    hideErrorMessage();
                    showLoadingBar();
                    if(mCursor != null){
                        deliverResult(mCursor);
                    }else{
                        forceLoad();
                    }
                }

                @Nullable
                @Override
                public Cursor loadInBackground() {
                    mCursor = getContentResolver().query(DataContract.RecipeEntry.CONTENT_URI ,
                            null , null , null , null);

                    return mCursor;
                }

                @Override
                public void deliverResult(@Nullable Cursor data) {
                    super.deliverResult(data);
                    mCursor = data;
                }
            };
        }else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data == null || data.getCount() == 0){
            FetchData();
            Log.d(LOG_TAG , "Data Cursor Failed , Gop Fetch Data");
        }else{
            // populate data Cursor
            getColumnIndicies(data);
            convertCursorToArray(data);
            Log.d(LOG_TAG , "Data Arrived Bu Cursor Now Parsing and the count is " + data.getCount());
        }
        data.close();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


    private void FetchData(){
        new GetRecipesDataQuery().execute();
    }

    private void getColumnIndicies(Cursor cursor){
        if(cursor == null) return;
        Id_Col = cursor.getColumnIndex(DataContract.RecipeEntry.ID_COL);
        Name_Col = cursor.getColumnIndex(DataContract.RecipeEntry.NAME_COL);
        Servings_Col = cursor.getColumnIndex(DataContract.RecipeEntry.SERVINGS_COL);
        Image_Col = cursor.getColumnIndex(DataContract.RecipeEntry.IMAGE_COL);
    }

    private void convertCursorToArray(Cursor cursor){
        if(cursor == null) {
            hideLoadingBar();
            showErrorMessage();
            return;
        }
        Recipe[] recipeList = new Recipe[cursor.getCount()];
        Recipe recipe;
        while(cursor.moveToNext()){
            recipe = new Recipe();
            recipe.setId(cursor.getInt(Id_Col));
            recipe.setServings(cursor.getInt(Servings_Col));
            recipe.setImage(cursor.getString(Image_Col));
            recipe.setName(cursor.getString(Name_Col));
            recipeList[cursor.getPosition()] = recipe;
        }
        mRecipes = recipeList;
        mAdapter.setmRecipes(mRecipes);
        Log.d(LOG_TAG , "Data Parsed From Cursor and sent to the adaptor");
        hideLoadingBar();
    }

    public class GetRecipesDataQuery extends AsyncTask<Void , Void , String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideErrorMessage();
            showLoadingBar();
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL bakingUrl = NetworkUtils.buildBakingDataUrl();
            String result = null;
            try{
                result = NetworkUtils.getBakingData(bakingUrl);
            }catch(IOException e){
                Log.d(LOG_TAG , e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            // Get the Data And Save it and send it to the Adapetr

            if(data != null && !data.isEmpty()){
                mRecipes = JsonUtils.ParseRecipesData(data);
                mAdapter.setmRecipes(mRecipes);
                new SaveDataQuery().execute();
                hideLoadingBar();
                Log.d(LOG_TAG , "Data Arrived from the internet");
            }else{
                Log.d(LOG_TAG , "Couldn't fetch Json Data");
                hideLoadingBar();
                showErrorMessage();
            }

        }
    }

    public class SaveDataQuery extends AsyncTask<Void , Void ,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            saveData();
            return null;
        }
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
                //stepValues.put(DataContract.StepEntry.ID_COL , step.getId());
                stepValues.put(DataContract.StepEntry.NUMBER_COL , step.getNumber());
                stepValues.put(DataContract.StepEntry.S_DESCRIPTION_COL , step.getShortDescription());
                stepValues.put(DataContract.StepEntry.DESCRIPTION_COL , step.getDescription());
                stepValues.put(DataContract.StepEntry.VIDEO_COL , step.getVideo());
                stepValues.put(DataContract.StepEntry.THUMBNAIL_COL , step.getThumbnail());
                stepValues.put(DataContract.StepEntry.RECIPE_ID_COL , recipe.getId());
                contentResolver.insert(DataContract.StepEntry.CONTENT_URI , stepValues);
            }

            ContentValues ingredientsValues ;
            for(Ingredient ingredient : ingredients){
                ingredientsValues = new ContentValues();
                ingredientsValues.put(DataContract.IngredientEntry.MEASURE_COL , ingredient.getMeasure());
                ingredientsValues.put(DataContract.IngredientEntry.QUANTITY_COL , ingredient.getQuantity());
                ingredientsValues.put(DataContract.IngredientEntry.NAME_COL , ingredient.getName());
                ingredientsValues.put(DataContract.IngredientEntry.RECIPE_ID , recipe.getId());
                contentResolver.insert(DataContract.IngredientEntry.CONTENT_URI , ingredientsValues);
            }
        }
        Log.d(LOG_TAG , "Data Saved Successfully");
    }
    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(MainActivity.this , RecipeDetailActivity.class);
        //int id = mRecipes[position].getId();
        // put the extra here
        intent.putExtra(Intent.EXTRA_TEXT , mRecipes[position]);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
