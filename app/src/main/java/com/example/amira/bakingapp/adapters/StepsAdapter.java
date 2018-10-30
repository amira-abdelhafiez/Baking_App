package com.example.amira.bakingapp.adapters;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.amira.bakingapp.models.Step;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder>{

    private static final String LOG_TAG = StepsAdapter.class.getSimpleName();

    private onItemClickHandler handler;

    private int numberOfItems;
    private Cursor mStepCursor;

    public StepsAdapter(onItemClickHandler handler){
        this.handler = handler;
    }

    public void setmSteps(Cursor cursor) {
        this.mStepCursor = cursor;
        if(cursor != null){
            this.numberOfItems = cursor.getCount();
        }else{
            this.numberOfItems = 0;
        }
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.numberOfItems;
    }

    public interface onItemClickHandler{
        void onClick(int position);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder{

        public StepViewHolder(View view){
            super(view);
        }

    }
}
