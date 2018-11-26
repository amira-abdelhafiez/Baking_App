package com.example.amira.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.data.DataContract;
import com.example.amira.bakingapp.models.Ingredient;

public class IngredientsIntentService extends IntentService {

    private static final String NAME = "IngredientServiceIntentService";

    public static final String ACTION_UPDATE_WIDGET = "com.example.amira.bakingapp.action.update_widget";
    public static final String RECIPE_ID = "recipeId";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public IngredientsIntentService(String name) {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            if(intent.getAction() == ACTION_UPDATE_WIDGET){
                int recipeId = intent.getIntExtra(RECIPE_ID , -1);
                handleUpdateWidgetAction(recipeId);
            }
        }
    }

    private void handleUpdateWidgetAction(int recipeId){
        if(recipeId > 0){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetsIds = appWidgetManager.getAppWidgetIds(new ComponentName(this , IngredientsWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetsIds , R.id.gv_ingredients_widget);
            IngredientsWidget.updateIngredientWidget(this , appWidgetManager , null ,appWidgetsIds);
            //PlantWidgetProvider.updatePlantWidget(this , appWidgetManager , plantId , imgRes , canWater , appWidgetsIds);
        }
    }
}
