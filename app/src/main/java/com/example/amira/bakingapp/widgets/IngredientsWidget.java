package com.example.amira.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.activities.RecipeDetailActivity;
import com.example.amira.bakingapp.models.Ingredient;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    private static int mCurrentRecipeId = -1;
    private static String mCurrentRecipeName = "N/A";

    public static void setCurrentRecipe(int recipeId , String recipeName){
        mCurrentRecipeId = recipeId;
        mCurrentRecipeName = recipeName;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager ,int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        Intent intent = new Intent(context , RecipeDetailActivity.class);
        intent.putExtra("RecipeId" , mCurrentRecipeId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT );

        views.setPendingIntentTemplate(R.id.gv_ingredients_widget , pendingIntent);

        views.setTextViewText(R.id.tv_recipe_name , mCurrentRecipeName);
        IngredientWidgetGridViewService.setRecipeId(mCurrentRecipeId);
        Intent serviceIntent = new Intent(context , IngredientWidgetGridViewService.class);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.gv_ingredients_widget , serviceIntent);
        views.setEmptyView(R.id.gv_ingredients_widget , R.id.empty_view);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateIngredientWidget(Context context, AppWidgetManager appWidgetManager,
                                         int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager ,  appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager , int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

