package com.example.amira.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.data.DataContract;
import com.example.amira.bakingapp.models.Recipe;

public class BakingWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingWidgetItemFactory(getApplicationContext());
    }

    class BakingWidgetItemFactory implements RemoteViewsFactory{

        private final String LOG_TAG = BakingWidgetItemFactory.class.getSimpleName();
        private Context mContext;

        private Cursor mCursor;

        BakingWidgetItemFactory(Context context){
            this.mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if(mCursor != null) mCursor.close();
            mCursor = getContentResolver().query(DataContract.RecipeEntry.CONTENT_URI,
                    null , null , null ,null);

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
        public RemoteViews getViewAt(int position)
        {
            if(mCursor != null) {
                mCursor.moveToPosition(position);
                RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_stack_view_item);
                int imageIndex = mCursor.getInt(mCursor.getColumnIndex(DataContract.RecipeEntry.ID_COL));
                views.setImageViewResource(R.id.widget_stack_view_item_iv, Recipe.getRecipeImages()[imageIndex - 1]);
                views.setTextViewText(R.id.widget_stack_view_item_tv, mCursor.getString(mCursor.getColumnIndex(DataContract.RecipeEntry.NAME_COL)));

                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_TEXT , mCursor.getInt(mCursor.getColumnIndex(DataContract.RecipeEntry.ID_COL)));
                views.setOnClickFillInIntent(R.id.widget_stack_view_item_iv , intent);
                return views;
            }else{
                return null;
            }
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
                return mCursor.getInt(mCursor.getColumnIndex(DataContract.RecipeEntry.ID_COL));
            }else{
                return -1;
            }
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

//        class RecipesQuery extends AsyncTask<Void , Void , Cursor>{
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected Cursor doInBackground(Void... voids) {
//                Cursor cursor = getContentResolver().query(DataContract.RecipeEntry.CONTENT_URI ,
//                        null , null , null , null);
//                return cursor;
//            }
//
//            @Override
//            protected void onPostExecute(Cursor cursor) {
//                super.onPostExecute(cursor);
//                if(mCursor != null) mCursor.close();
//                Log.d(LOG_TAG , "Data Arrived");
//
//                mCursor = cursor;
//            }
//        }
    }
}
