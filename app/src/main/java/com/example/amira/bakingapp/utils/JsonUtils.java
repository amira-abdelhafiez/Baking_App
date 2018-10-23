package com.example.amira.bakingapp.utils;

import android.util.Log;

import com.example.amira.bakingapp.models.Ingredient;
import com.example.amira.bakingapp.models.Recipe;
import com.example.amira.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    // Recipe Object
    private static final String R_ID = "id";
    private static final String R_NAME = "name";
    private static final String R_INGREDIENTS = "ingredients";
    private static final String R_STEPS = "steps";
    private static final String R_SERVINGS = "servings";
    private static final String R_IMAGE = "image";

    // Ingredient Object
    private static final String I_NAME = "ingredient";
    private static final String I_QUANTITY = "quantity";
    private static final String I_MEASURE = "measure";


    // Step Object

    private static final String S_ID = "id";
    private static final String S_DESCRIPTION = "description";
    private static final String S_SHORT_DESCRIPTION = "shortDescription";
    private static final String S_VIDEO = "videoURL";
    private static final String S_THUMBNAIL = "thumbnailURL";

    /*
    Parse The whole data inside the json file and return array if recipes
     */
    public static Recipe[] ParseRecipesData(String jsonData){
        Recipe[] parsedRecipes = null;

        if(jsonData != null){
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                int length = jsonArray.length();
                if(length > 0){
                    parsedRecipes = new Recipe[length];
                    for (int i = 0 ; i < length ; i++){
                        parsedRecipes[i] = ParseRecipe(jsonArray.optString(i));
                    }
                }
            } catch (JSONException e) {
                Log.d(LOG_TAG , e.getStackTrace().toString());
            }
        }else{
            Log.d(LOG_TAG , "Null Recipes Query Data");
        }
        return parsedRecipes;
    }

    /*
    Parse each recipe json object and return recipe java object
     */
    private static Recipe ParseRecipe(String jsonData){
        Recipe recipe = new Recipe();
        if(jsonData != null){
            try{
                JSONObject jsonRecipe = new JSONObject(jsonData);

                recipe.setId(jsonRecipe.optInt(R_ID));
                recipe.setName(jsonRecipe.optString(R_NAME));
                recipe.setImage(jsonRecipe.optString(R_IMAGE));
                recipe.setServings(jsonRecipe.optInt(R_SERVINGS));

                ArrayList<Step> steps = new ArrayList<>();
                ArrayList<Ingredient> ingredients = new ArrayList<>();

                JSONArray stepsJson = jsonRecipe.getJSONArray(R_STEPS);

                int stepsLength = stepsJson.length();
                if(stepsLength > 0){
                    for(int i = 0 ; i < stepsLength ; i++){
                        steps.add(parseStep(stepsJson.optString(i)));
                    }
                }

                JSONArray ingredientsJson = jsonRecipe.getJSONArray(R_INGREDIENTS);

                int ingredientLength = ingredientsJson.length();

                if(ingredientLength > 0){
                    for(int i = 0 ; i < ingredientLength ; i++){
                        ingredients.add(parseIngredient(ingredientsJson.optString(i)));
                    }
                }

                recipe.setIngredients(ingredients);
                recipe.setSteps(steps);
            }catch(JSONException e){
                Log.d(LOG_TAG , e.getStackTrace().toString());
            }
        }else{
            Log.d(LOG_TAG , "Recipe is null");
        }
        return recipe;
    }

    /*
    Parse ingredient json object
     */
    private static Ingredient parseIngredient(String jsonData){

        Ingredient ingredient = new Ingredient();
        if(jsonData != null){
            try{
                JSONObject ingredientJson = new JSONObject(jsonData);
                ingredient.setName(ingredientJson.optString(I_NAME));
                ingredient.setMeasure(ingredientJson.optString(I_MEASURE));
                ingredient.setQuantity(ingredientJson.optDouble(I_QUANTITY));
            }catch(JSONException e){
                Log.d(LOG_TAG , e.getStackTrace().toString());
            }
        }else{
            Log.d(LOG_TAG , "Null Ingredient");
        }
        return ingredient;
    }

    /*
    Parse step java object
     */
    private static Step parseStep(String jsonData){
        Step  step = new Step();
        if(jsonData != null){
            try{
                JSONObject stepJson = new JSONObject(jsonData);
                step.setId(stepJson.optInt(S_ID));
                step.setDescription(stepJson.optString(S_DESCRIPTION));
                step.setShortDescription(stepJson.optString(S_SHORT_DESCRIPTION));
                step.setThumbnail(stepJson.optString(S_THUMBNAIL));
                step.setVideo(stepJson.optString(S_VIDEO));
            }catch(JSONException e){
                Log.d(LOG_TAG , e.getStackTrace().toString());
            }
        }else{
            Log.d(LOG_TAG , "Null Step");
        }
        return step;
    }
}
