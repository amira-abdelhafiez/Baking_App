package com.example.amira.bakingapp.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.adapters.IngredientsAdapter;
import com.example.amira.bakingapp.adapters.StepsAdapter;

public class MasterFragment extends Fragment implements StepsAdapter.onItemClickHandler{

    private static final String LOG_TAG = MasterFragment.class.getSimpleName();

    private Cursor mStepCursor , mIngredientCursor;
    private StepsAdapter mStepsAdapter;
    private IngredientsAdapter mIngredientsAdapter;

    private RecyclerView mStepsRecyclerView , mIngredientsRecyclerView;
    private Context mContext;
    public MasterFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_detail_fragment , container , false);

        mStepsRecyclerView = rootView.findViewById(R.id.rv_recipe_steps);
        mIngredientsRecyclerView = rootView.findViewById(R.id.rv_recipe_ingredients);

        mIngredientsAdapter = new IngredientsAdapter();
        mIngredientsAdapter.setmIngredients(mIngredientCursor);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        RecyclerView.LayoutManager ingredientLayoutManager = new LinearLayoutManager(mContext , LinearLayoutManager.VERTICAL , false);
        mIngredientsRecyclerView.setLayoutManager(ingredientLayoutManager);

        mStepsAdapter = new StepsAdapter(this);
        mStepsAdapter.setmSteps(mStepCursor);
        RecyclerView.LayoutManager stepsLayoutManager = new LinearLayoutManager(mContext , LinearLayoutManager.VERTICAL , false);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsRecyclerView.setLayoutManager(stepsLayoutManager);

        return rootView;
    }
    public void setmContext(Context context){
        mContext  = context;
    }

    public void setmStepsCursor(Cursor data){
        this.mStepCursor = data;
    }

    public void setmIngredientCursor(Cursor data){
        this.mIngredientCursor = data;
    }

    @Override
    public void onClick(int position) {
        Toast toast = Toast.makeText(mContext , "You clicked on item number " + position , Toast.LENGTH_SHORT);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getFragmentManager() != null) {

            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }
}
