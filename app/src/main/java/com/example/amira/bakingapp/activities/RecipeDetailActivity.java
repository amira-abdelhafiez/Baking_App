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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.adapters.IngredientsAdapter;
import com.example.amira.bakingapp.adapters.StepsAdapter;
import com.example.amira.bakingapp.data.AppDatabase;
import com.example.amira.bakingapp.data.DataContract;
import com.example.amira.bakingapp.models.Ingredient;
import com.example.amira.bakingapp.models.Recipe;
import com.example.amira.bakingapp.models.Step;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> , StepsAdapter.onItemClickHandler{

    private static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();

    private static final int INGREDIENTS_LOADER_ID = 701;
    private static final int STEPS_LOADER_ID = 702;

    private static final String CURRENT_ID = "currentPosition";
    private static final String CURRENT_RECIPE_ID = "currentRecipeId";

    private int mCurrentRecipeId;
    private Cursor mIngredientCursor , mStepsCursor;

    private StepsAdapter mStepsAdapter;
    private IngredientsAdapter mIngredientsAdapter;


    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView mIngredientsRecyclerView;

    @BindView(R.id.rv_recipe_steps)
    RecyclerView mStepsRecyclerView;

    @BindView(R.id.recipe_image)
    ImageView mRecipeImage;

    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mCurrentRecipeId = 0;
        Intent callingIntent = getIntent();
        if(callingIntent.hasExtra(Intent.EXTRA_TEXT)){
            mCurrentRecipeId = callingIntent.getIntExtra(Intent.EXTRA_TEXT , -1);
        }

        ButterKnife.bind(this);

        db = AppDatabase.getsInstance(this);

        Log.d(LOG_TAG , "The steps is  " + db.stepDao().count());
        Log.d(LOG_TAG , "The in is " + db.ingredientDao().count());

        int imageIndex = mCurrentRecipeId - 1;
        if(imageIndex >= 0 && imageIndex < 4){
            Picasso.with(this)
                    .load(Recipe.getRecipeImages()[imageIndex])
                    .placeholder(R.drawable.default_meal_image)
                    .into(mRecipeImage);
        }


        mStepsAdapter = new StepsAdapter(this);
        mIngredientsAdapter = new IngredientsAdapter();

        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        mStepsRecyclerView.setAdapter(mStepsAdapter);

        LinearLayoutManager mIngredientsLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);

        LinearLayoutManager mStepsLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);
        mStepsRecyclerView.setLayoutManager(mStepsLayoutManager);

        getIngredientsData();
        getStepsData();

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(INGREDIENTS_LOADER_ID , null , this);
        loaderManager.initLoader(STEPS_LOADER_ID , null , this);
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
                Cursor mIngredientsData;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if(mIngredientsData != null){
                        deliverResult(mIngredientsData);
                    }else{
                        forceLoad();
                    }
                }

                @Nullable
                @Override
                public Cursor loadInBackground() {
                    if(mCurrentRecipeId == -1) return null;
                    Log.d(LOG_TAG , "LoadInBack for ingredients");
                    String selection = "recipeId =: %d";
                    String id = Integer.toString(mCurrentRecipeId);
                    String[] selectionArgs = new String[] {id};
                    mIngredientsData = getContentResolver().query(DataContract.IngredientEntry.CONTENT_URI ,
                            null, null ,selectionArgs, null);
                    return mIngredientsData;
                }

                @Override
                public void deliverResult(@Nullable Cursor data) {
                    super.deliverResult(data);
                    mIngredientCursor = data;
                }
            };
        }else if(id == STEPS_LOADER_ID){
            return new AsyncTaskLoader<Cursor>(this) {

                Cursor mStepData;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    if(mStepData != null){
                        deliverResult(mStepData);
                    }else{
                        forceLoad();
                    }
                }

                @Nullable
                @Override
                public Cursor loadInBackground() {
                    if(mCurrentRecipeId == -1) return null;
                    Log.d(LOG_TAG , "Load in back steps");
                    String selection = "recipeId =: %d";
                    String id = Integer.toString(mCurrentRecipeId);
                    String[] selectionArgs = new String[] {id};
                    mStepData = getContentResolver().query(DataContract.StepEntry.CONTENT_URI ,
                            null , null , selectionArgs , DataContract.StepEntry.NUMBER_COL);

                    if(mStepData == null){
                        Log.d(LOG_TAG  , "HERE STEPS NULL");
                    }else{
                        Log.d(LOG_TAG , "GOt Steps Data");
                    }
                    return mStepData;
                }

                @Override
                public void deliverResult(@Nullable Cursor data) {
                    super.deliverResult(data);
                    mStepsCursor = data;
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
            if(data != null){

                Log.d(LOG_TAG , "The in count is " + data.getCount());
                mIngredientsAdapter.setmIngredients(data);
            }else{
                Log.d(LOG_TAG , "Ingredients Null");
            }
        }else if(loaderId == STEPS_LOADER_ID){

            if(data != null){
                Log.d(LOG_TAG , "The st count is " + data.getCount());
                mStepsAdapter.setmSteps(data);
            }else{
                Log.d(LOG_TAG , "Steps Null");
            }
        }else{
            Log.d(LOG_TAG  , "Invalid Loader ID in onLoadFinished");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(RecipeDetailActivity.this , StepDetailActivity.class);
        intent.putExtra(CURRENT_ID , position);
        intent.putExtra(CURRENT_RECIPE_ID , mStepsCursor.getInt(mStepsCursor.getColumnIndex(DataContract.StepEntry.RECIPE_ID_COL)));
        startActivity(intent);
    }
}
