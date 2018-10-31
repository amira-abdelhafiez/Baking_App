package com.example.amira.bakingapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.models.Step;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder>{

    private static final String LOG_TAG = StepsAdapter.class.getSimpleName();

    private onItemClickHandler handler;

    private Context mContext;

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
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.step_rv_item , parent ,false);
        StepViewHolder vh = new StepViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.mStepNumber =
    }

    @Override
    public int getItemCount() {
        return this.numberOfItems;
    }

    public interface onItemClickHandler{
        void onClick(int position);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mStepNumber , mStepDescription;
        public StepViewHolder(View itemView){
            super(itemView);
            mStepNumber = itemView.findViewById(R.id.tv_step_number);
            mStepDescription = itemView.findViewById(R.id.tv_step_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            handler.onClick(position);
        }
    }
}
