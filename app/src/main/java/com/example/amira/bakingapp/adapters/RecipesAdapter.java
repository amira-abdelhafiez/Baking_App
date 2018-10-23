package com.example.amira.bakingapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
        holder.mRecipeNameTextView.setText(mRecipes[position].getName());
        holder.mRecipeServingsTextView.setText(mRecipes[position].getServings());

        String imageUrl = mRecipes[position].getImage();
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.default_meal_image)
                .error(R.drawable.default_meal_image)
                .into(holder.mRecipeImageView);
    }

    @Override
    public int getItemCount() {
        if(mRecipes == null){
            return 0;
        }else{
            return mRecipes.length;
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_recipe_image)
        ImageView mRecipeImageView;
        @BindView(R.id.tv_recipe_name)
        TextView mRecipeNameTextView;
        @BindView(R.id.tv_recipe_servings)
        TextView mRecipeServingsTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }

    public void setmRecipes(Recipe[] recipes){
        mRecipes = recipes;
        notifyDataSetChanged();
    }
}
