package com.example.amira.bakingapp.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class TestActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private static final int RECIPES_LOADER_ID = 107;

    private LoaderManager mLoaderManager;

    private RecipesAdapter mAdapter;

    private Recipe[] mRecipes;

    @BindView(R.id.test_text)
    TextView testText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

        LoaderManager lm = getSupportLoaderManager();
        lm.initLoader(RECIPES_LOADER_ID , null , this);
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
            testText.setText(data);
            mRecipes = JsonUtils.ParseRecipesData(data);

            StringBuilder sb = new StringBuilder();
            for (Recipe recipe : mRecipes)
            {
                   sb.append(recipe.toString() + "\n");
            }
            Log.d("Data" , sb.toString());
            //mAdapter.setmRecipes(mRecipes);
        }else{
            Log.d("Data" , "Ya 7aywan msh tbgeeb el data  ");
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
