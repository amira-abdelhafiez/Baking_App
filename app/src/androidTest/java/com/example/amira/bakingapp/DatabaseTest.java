package com.example.amira.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.amira.bakingapp.adapters.IngredientsAdapter;
import com.example.amira.bakingapp.data.AppDatabase;
import com.example.amira.bakingapp.data.DataContract;
import com.example.amira.bakingapp.models.Ingredient;
import com.example.amira.bakingapp.models.IngredientDao;
import com.example.amira.bakingapp.models.Recipe;
import com.example.amira.bakingapp.models.RecipeDao;
import com.example.amira.bakingapp.models.Step;
import com.example.amira.bakingapp.models.StepDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private AppDatabase mDb;
    private Context mContext;

    private IngredientDao ingredientDao;
    private RecipeDao recipeDao;
    private StepDao stepDao;

    @Before
    public void setUpmDb(){
        mContext = InstrumentationRegistry.getTargetContext();
        mDb = AppDatabase.getsInstance(mContext);
        ingredientDao = mDb.ingredientDao();
        recipeDao = mDb.recipeDao();
        stepDao = mDb.stepDao();
    }

    @After
    public void closemDb(){
        mDb.close();
    }

    @Test
    public void mDbCreatedTest(){

    }


    @Test
    public void recipeDaoTest(){
        Recipe testRecipe = new Recipe();
        testRecipe.setId(100);
        testRecipe.setImage("");
        testRecipe.setServings(3);
        testRecipe.setName("Cheese Cake");
        recipeDao.insertRecipe(testRecipe);

        int cnt = recipeDao.count();
        int expectedCount = 1;

        assertEquals(cnt , expectedCount);

        Cursor result = recipeDao.getRecipeById(100);
        assertNotNull(result);
        assertEquals(result.getCount() , expectedCount);

        result.moveToFirst();

        String nameValue = result.getString(result.getColumnIndex(DataContract.RecipeEntry.NAME_COL));
        String expectedName = "Cheese Cake";

        assertEquals(nameValue , expectedName);
    }

    @Test
    public void ingredientDaoTest(){
        Ingredient testIngredient = new Ingredient();
        testIngredient.setRecipeId(100);
        testIngredient.setMeasure("ts");
        testIngredient.setQuantity(1.4);
        testIngredient.setName("Salt");

        ingredientDao.insert(testIngredient);

        int cnt = ingredientDao.count();

        assertEquals(cnt , 1);

        Cursor result = ingredientDao.getIngredients(100);

        assertNotNull(result);
        assertEquals(result.getCount() , 1);
        result.moveToFirst();
        assertEquals(result.getString(result.getColumnIndex(DataContract.IngredientEntry.NAME_COL)) , "Salt");
    }

    @Test
    public void stepDaoTest(){
        Step testStep = new Step();
        testStep.setRecipeId(100);
        testStep.setShortDescription("Test Step ");
        testStep.setDescription("This is a test Step");
        testStep.setVideo("");
        testStep.setNumber(12);
        testStep.setThumbnail("");

        stepDao.insert(testStep);

        int cnt = stepDao.count();
        assertEquals(cnt ,1);

        Cursor result = stepDao.getSteps(100);
        assertNotNull(result);
        assertEquals(result.getCount() , 1);
        result.moveToFirst();
        assertEquals(result.getInt(result.getColumnIndex(DataContract.StepEntry.NUMBER_COL)) , 12);
    }
}
