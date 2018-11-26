package com.example.amira.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.data.DataContract;

public class IngredientWidgetGridViewService extends RemoteViewsService {

    private static final String LOG_TAG = IngredientWidgetGridViewFactory.class.getSimpleName();
    private static final String RECIPE_ID = "recipeId";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int recipeId = intent.getIntExtra(RECIPE_ID , -1);
        return new IngredientWidgetGridViewFactory(getApplicationContext() , recipeId);
    }

    class IngredientWidgetGridViewFactory implements RemoteViewsFactory {

        private final String LOG_TAG = IngredientWidgetGridViewFactory.class.getSimpleName();
        private Context mContext;
        private int mCurrentRecipeId;

        private Cursor mCursor;

        public IngredientWidgetGridViewFactory(Context context , int recipeId){
            this.mContext = context;
            this.mCurrentRecipeId = recipeId;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if(mCurrentRecipeId < 0) return;
            if(mCursor != null) mCursor.close();

            String[] args = {Integer.toString(mCurrentRecipeId)};
            mCursor = getContentResolver().query(DataContract.IngredientEntry.CONTENT_URI ,
                    null , null , args , null);
        }

        @Override
        public void onDestroy() {
            mCursor.close();
        }

        @Override
        public int getCount() {
            if(mCursor == null) return 0;
            else return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if(mCursor.moveToPosition(position)){
                RemoteViews rv = new RemoteViews(mContext.getPackageName() , R.layout.ingredient_rv_item);
                String ingredientStr = mCursor.getString(mCursor.getColumnIndex(DataContract.IngredientEntry.NAME_COL))
                        + " " + Double.toString(mCursor.getDouble(mCursor.getColumnIndex(DataContract.IngredientEntry.QUANTITY_COL)))
                        + " " + mCursor.getString(mCursor.getColumnIndex(DataContract.IngredientEntry.MEASURE_COL));
                rv.setTextViewText(R.id.tv_recipe_ingredient , ingredientStr);
                return rv;
            }
            return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            if(mCursor.moveToPosition(position)){
                return mCursor.getInt(mCursor.getColumnIndex(DataContract.IngredientEntry.ID_COL));
            }
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
