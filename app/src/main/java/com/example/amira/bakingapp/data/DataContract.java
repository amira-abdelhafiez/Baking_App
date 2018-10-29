package com.example.amira.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataContract {

    public static final String AUTHORITY = "com.example.amira.bakingapp";
    public static final String RECIPE_PATH = "recipe";
    public static final String STEP_PATH = "step";
    public static final String INGREDIENT_PATH = "ingredient";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class RecipeEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(RECIPE_PATH).build();

        public static final String ID_COL = "_id";
        public static final String NAME_COL = "name";
        public static final String SERVINGS_COL = "servings";
        public static final String IMAGE_COL = "image";
    }

    public static final class IngredientEntry implements  BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(INGREDIENT_PATH).build();

        public static final String ID_COL = "_id";
        public static final String NAME_COL = "name";
        public static final String QUANTITY_COL = "quantity";
        public static final String MEASURE_COL = "measure";
        public static final String RECIPE_ID = "recipeId";
    }

    public static final class StepEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(STEP_PATH).build();

        public static final String ID_COL = "_id";
        public static final String DESCRIPTION_COL = "description";
        public static final String S_DESCRIPTION_COL = "sDescription";
        public static final String VIDEO_COL = "video";
        public static final String THUMBNAIL_COL = "thumbnail";
        public static final String RECIPE_ID_COL = "recipeId";
    }
}
