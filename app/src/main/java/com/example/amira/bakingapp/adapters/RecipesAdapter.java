package com.example.amira.bakingapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private static final String LOG_TAG = RecipesAdapter.class.getSimpleName();

    private Recipe[] mRecipes;

    private Context mContext;

    private ItemOnClickHandler handler;

    public RecipesAdapter(ItemOnClickHandler handler){
        this.handler = handler;
    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.recipes_rv_item , parent , false);
        RecipeViewHolder vh = new RecipeViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        String Name = (mRecipes[position].getName() == null) ? "No Name" : mRecipes[position].getName();
        holder.mRecipeNameTextView.setText(Name);

        int Servings = (mRecipes[position].getServings() <= 0) ? 0 : mRecipes[position].getServings();

        String ServingText = "For " + Integer.toString(Servings) + " people";
        holder.mRecipeServingsTextView.setText(Integer.toString(Servings));

        String imageUrl = mRecipes[position].getImage();

        if(imageUrl == null || imageUrl.equals("") || imageUrl.equals(" ")){
            Picasso.with(mContext)
                    .load(R.drawable.default_meal_image)
                    .into(holder.mRecipeImageView);
        }else{
            Picasso.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_meal_image)
                    .error(R.drawable.default_meal_image)
                    .into(holder.mRecipeImageView);
        }
    }

    @Override
    public int getItemCount() {
        if(mRecipes == null){
            return 0;
        }else{
            return mRecipes.length;
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mRecipeImageView;
        TextView mRecipeNameTextView;
        TextView mRecipeServingsTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeImageView = itemView.findViewById(R.id.iv_recipe_image);
            mRecipeNameTextView = itemView.findViewById(R.id.tv_recipe_name);
            mRecipeServingsTextView = itemView.findViewById(R.id.tv_recipe_servings);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            handler.onClickItem(position);
        }
    }

    public void setmRecipes(Recipe[] recipes){
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public interface ItemOnClickHandler{
        void onClickItem(int position);
    }
}
