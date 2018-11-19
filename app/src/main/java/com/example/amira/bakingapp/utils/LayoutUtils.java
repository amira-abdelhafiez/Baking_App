package com.example.amira.bakingapp.utils;

import android.content.Context;

public class LayoutUtils {
    public static float convertPxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }
}
