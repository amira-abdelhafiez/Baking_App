package com.example.amira.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.amira.bakingapp.models.Ingredient;
import com.example.amira.bakingapp.models.IngredientDao;
import com.example.amira.bakingapp.models.Recipe;
import com.example.amira.bakingapp.models.RecipeDao;
import com.example.amira.bakingapp.models.Step;
import com.example.amira.bakingapp.models.StepDao;

public class BakingAppProvider extends ContentProvider {
    private static final int RECIPE = 100;
    private static final int RECIPE_WITH_ID = 101;
    private static final int INGREDIENT = 200;
    private static final int INGREDIENT_WITH_ID = 201;
    private static final int STEP = 300;
    private static final int STEP_WITH_ID = 301;


    private static UriMatcher sUriMatcher = buildUriMatcher();

    private AppDatabase mDb;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DataContract.AUTHORITY , DataContract.RECIPE_PATH , RECIPE);
        uriMatcher.addURI(DataContract.AUTHORITY , DataContract.RECIPE_PATH + "/#" , RECIPE_WITH_ID);
        uriMatcher.addURI(DataContract.AUTHORITY , DataContract.INGREDIENT_PATH , INGREDIENT);
        uriMatcher.addURI(ContactsContract.AUTHORITY , DataContract.INGREDIENT_PATH + "/#" , INGREDIENT_WITH_ID);
        uriMatcher.addURI(DataContract.AUTHORITY , DataContract.STEP_PATH , STEP);
        uriMatcher.addURI(DataContract.AUTHORITY , DataContract.STEP_PATH + "/#" , STEP_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDb = AppDatabase.getsInstance(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor dataCursor = null;
        int resultId = sUriMatcher.match(uri);
        int id;
        switch (resultId){
            case RECIPE:
                dataCursor = mDb.recipeDao().getRecipes();
                break;
            case RECIPE_WITH_ID:
                id = uri.getPathSegments().indexOf(0);
                dataCursor = mDb.recipeDao().getRecipeById(id);
                break;
            case INGREDIENT:
                id = Integer.parseInt(selectionArgs[0]);
                dataCursor = mDb.ingredientDao().getIngredients(id);
                break;
            case INGREDIENT_WITH_ID:
                id = uri.getPathSegments().indexOf(0);
                dataCursor = mDb.ingredientDao().getIngredientById(id);
                break;
            case STEP:
                id = Integer.parseInt(selectionArgs[0]);
                dataCursor = mDb.stepDao().getSteps(id);
                break;
            case STEP_WITH_ID:
                id = uri.getPathSegments().indexOf(0);
                dataCursor = mDb.stepDao().getStepById(id);
                break;
            default:
                throw new UnsupportedOperationException("Invalid uri is " +  uri.toString());
        }

        dataCursor.setNotificationUri(getContext().getContentResolver() , uri);
        return dataCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int resultId = sUriMatcher.match(uri);

        Uri returnedUri;
        switch (resultId){
            case RECIPE:
                Recipe recipe = new Recipe();
                int id = values.getAsInteger(DataContract.RecipeEntry.ID_COL);
                recipe.setId(id);
                recipe.setImage(values.getAsString(DataContract.RecipeEntry.IMAGE_COL));
                recipe.setName(values.getAsString(DataContract.RecipeEntry.NAME_COL));
                recipe.setServings(values.getAsInteger(DataContract.RecipeEntry.SERVINGS_COL));

                long insertedRecipeId = mDb.recipeDao().insertRecipe(recipe);

                returnedUri = ContentUris.withAppendedId(DataContract.RecipeEntry.CONTENT_URI , insertedRecipeId);
                break;
            case INGREDIENT:
                Ingredient ingredient = new Ingredient();
                //ingredient.setId(values.getAsInteger(DataContract.IngredientEntry.ID_COL));
                ingredient.setName(values.getAsString(DataContract.IngredientEntry.NAME_COL));
                ingredient.setQuantity(values.getAsDouble(DataContract.IngredientEntry.QUANTITY_COL));
                ingredient.setMeasure(values.getAsString(DataContract.IngredientEntry.MEASURE_COL));
                ingredient.setRecipeId(values.getAsInteger(DataContract.IngredientEntry.RECIPE_ID));

                long insertedIngredientId = mDb.ingredientDao().insert(ingredient);

                returnedUri = ContentUris.withAppendedId(DataContract.IngredientEntry.CONTENT_URI , insertedIngredientId);
                break;
            case STEP:
                Step step = new Step();
                step.setVideo(values.getAsString(DataContract.StepEntry.VIDEO_COL));
                step.setThumbnail(values.getAsString(DataContract.StepEntry.THUMBNAIL_COL));
                step.setNumber(values.getAsInteger(DataContract.StepEntry.NUMBER_COL));
                step.setNumber(values.getAsInteger(DataContract.StepEntry.NUMBER_COL));
                step.setRecipeId(values.getAsInteger(DataContract.StepEntry.RECIPE_ID_COL));
                step.setDescription(values.getAsString(DataContract.StepEntry.DESCRIPTION_COL));
                step.setShortDescription(values.getAsString(DataContract.StepEntry.S_DESCRIPTION_COL));

                long insertedStepId = mDb.stepDao().insert(step);
                returnedUri = ContentUris.withAppendedId(DataContract.StepEntry.CONTENT_URI , insertedStepId);
                break;
            default:
                throw new UnsupportedOperationException("Uri " + uri);
        }
        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
