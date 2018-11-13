package com.example.amira.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.example.amira.bakingapp.activities.MainActivity;
import com.example.amira.bakingapp.activities.RecipeDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.v4.content.ContextCompat.startActivity;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    private static final String CURRENT_ID = "currentPosition";
    private static final String CURRENT_RECIPE_ID = "currentRecipeId";

    @Rule
    public IntentsTestRule<RecipeDetailActivity> intentsTestRule =
            new IntentsTestRule<>(RecipeDetailActivity.class);

    @Before
    public void registerIdingResource(){

    }

    @After
    public void unregisterIdlingResource(){

    }
    @Test
    public void stepIntentTest(){
        Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), RecipeDetailActivity.class);

        intent.putExtra(Intent.EXTRA_TEXT , 100);
        intentsTestRule.launchActivity(intent);

        onView(ViewMatchers.withId(R.id.rv_recipe_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0 , click()));

        intended(allOf(
                toPackage("com.example.amira.bakingapp"),
                hasExtra(CURRENT_ID , 1),
                hasExtra(CURRENT_RECIPE_ID , 100)
        ));
    }
}
