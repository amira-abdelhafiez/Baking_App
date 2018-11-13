package com.example.amira.bakingapp.data;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class StepIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback mCallback;
    private AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.mCallback = callback;
    }

    public void setIdleState(boolean idleState){
        isIdleNow.set(idleState);
        if(idleState && mCallback != null){
            mCallback.onTransitionToIdle();
        }
    }
}
