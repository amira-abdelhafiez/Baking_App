package com.example.amira.bakingapp.widgets;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class BakingAppIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private static final String ACTION = "";

    public BakingAppIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
