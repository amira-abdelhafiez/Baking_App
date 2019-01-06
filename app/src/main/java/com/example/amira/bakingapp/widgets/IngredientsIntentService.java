package com.example.amira.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.activities.MainActivity;
import com.example.amira.bakingapp.data.DataContract;
import com.example.amira.bakingapp.models.Ingredient;

public class IngredientsIntentService extends IntentService {

    private static final String NAME = "IngredientServiceIntentService";

    public static final String ACTION_UPDATE_WIDGET = "com.example.amira.bakingapp.action.update_widget";
    public static final String RECIPE_ID = "recipeId";

    public IngredientsIntentService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            if(intent.getAction() == ACTION_UPDATE_WIDGET){
                int recipeId = intent.getIntExtra(RECIPE_ID , -1);
                String recipeName = intent.getStringExtra(MainActivity.RECIPE_NAME_EXTRA);
                Log.d("WidgetTrace" , "On Handle intent with RecipeID " + Integer.toString(recipeId));
                handleUpdateWidgetAction(recipeId , recipeName);
            }
        }
    }

    private void handleUpdateWidgetAction(int recipeId , String recipeName){
        if(recipeId > 0){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetsIds = appWidgetManager.getAppWidgetIds(new ComponentName(this , IngredientsWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetsIds , R.id.gv_ingredients_widget);
            IngredientsWidget.setCurrentRecipe(recipeId , recipeName);
            IngredientsWidget.updateIngredientWidget(this , appWidgetManager  ,appWidgetsIds);
        }
    }
}
