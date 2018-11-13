package com.example.amira.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.amira.bakingapp.activities.StepDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityTest {
    @Rule
    ActivityTestRule<StepDetailActivity> stepDetailActivityRule =
            new ActivityTestRule<>(StepDetailActivity.class);

    @Test
    void fun(){

    }
}
