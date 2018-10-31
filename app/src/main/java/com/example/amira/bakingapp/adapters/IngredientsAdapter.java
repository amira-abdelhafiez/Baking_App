package com.example.amira.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.models.Ingredient;
import com.squareup.picasso.Picasso;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private static final String LOG_TAG = IngredientsAdapter.class.getSimpleName();

    private Context mContext;

    private Ingredient[] mIngredients;
    private int numberOfItems;

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
        String IngredientText = mIngredients[position].getName() + " ( " +
                mIngredients[position].getQuantity() + " " +
                mIngredients[position].getMeasure() + " ) ";
        holder.mIngredientText.setText(IngredientText);

        Picasso.with(mContext)
                .load(R.drawable.ic_play_circle_filled_orange_24dp)
                .into(holder.mPointImage);
    }

    @Override
    public int getItemCount() {
        return this.numberOfItems;
    }

    public void setmIngredients(Ingredient[] mIngredients) {
        this.mIngredients = mIngredients;
        if(mIngredients == null){
            numberOfItems = 0;
        }else{
            numberOfItems = mIngredients.length;
        }
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
