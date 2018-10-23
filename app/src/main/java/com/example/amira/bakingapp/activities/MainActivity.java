package com.example.amira.bakingapp.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.adapters.RecipesAdapter;
import com.example.amira.bakingapp.models.Recipe;
import com.example.amira.bakingapp.utils.JsonUtils;
import com.example.amira.bakingapp.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int RECIPES_LOADER_ID = 107;

    private LoaderManager mLoaderManager;

    private RecipesAdapter mAdapter;
    @BindView(R.id.test_text) TextView mTestTextView;

    @BindView(R.id.rv_recipes) RecyclerView mRecipesRecyclerView;

    private Recipe[] mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

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
        mRecipes = JsonUtils.ParseRecipesData(data);
        mAdapter.setmRecipes(mRecipes);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
