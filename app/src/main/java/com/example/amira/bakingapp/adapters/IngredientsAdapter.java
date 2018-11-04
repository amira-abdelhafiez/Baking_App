package com.example.amira.bakingapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.icu.util.Measure;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.data.DataContract;
import com.example.amira.bakingapp.models.Ingredient;
import com.squareup.picasso.Picasso;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private static final String LOG_TAG = IngredientsAdapter.class.getSimpleName();

    private Context mContext;

    private Cursor mIngredients;
    private int numberOfItems;

    private int Id_Col , Name_Col , Quantity_Col , Measure_Col , Recipe_Id_Col;

    public IngredientsAdapter(){
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ingredient_rv_item , parent , false);
        IngredientViewHolder vh = new IngredientViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        if(mIngredients.moveToPosition(position)) {
            Name_Col = mIngredients.getColumnIndex(DataContract.IngredientEntry.NAME_COL);
            Quantity_Col = mIngredients.getColumnIndex(DataContract.IngredientEntry.QUANTITY_COL);
            Measure_Col = mIngredients.getColumnIndex(DataContract.IngredientEntry.MEASURE_COL);
            String IngredientText = mIngredients.getString(Name_Col) + " ( " +
                    mIngredients.getDouble(Quantity_Col) + " " +
                    mIngredients.getString(Measure_Col) + " ) ";
            holder.mIngredientText.setText(IngredientText);

            Picasso.with(mContext)
                    .load(R.drawable.ic_play_circle_filled_orange_24dp)
                    .into(holder.mPointImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.numberOfItems;
    }

    public void setmIngredients(Cursor mIngredients) {
        this.mIngredients = mIngredients;
        if(mIngredients == null){
            this.numberOfItems = 0;
        }else{
            this.numberOfItems = mIngredients.getCount();
        }
        Log.d(StepsAdapter.class.getSimpleName() , "In Count is " + this.numberOfItems);
        notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView mIngredientText;
        ImageView mPointImage;

        public IngredientViewHolder(View itemView){
            super(itemView);
            mIngredientText = itemView.findViewById(R.id.tv_recipe_ingredient);
            mPointImage = itemView.findViewById(R.id.iv_recipe_ingredient_icon);
        }
    }

}
