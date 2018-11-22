package com.example.amira.bakingapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.adapters.StepsAdapter;
import com.example.amira.bakingapp.data.DataContract;
import com.example.amira.bakingapp.fragments.FlowFragment;
import com.example.amira.bakingapp.fragments.MasterFragment;
import com.example.amira.bakingapp.models.Step;
import com.example.amira.bakingapp.utils.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> , StepsAdapter.onItemClickHandler{

    private static final String LOG_TAG = StepDetailActivity.class.getSimpleName();

    private static final String CURRENT_PLAYER_POSITION = "current_player_position";

    private int mCurrentPosition , mCurrentRecipeId;

    private Cursor mRecipeSteps;

    private static final int RECIPE_STEPS_LOADER_ID = 801;
    private static final int RECIPE_INGREDIENTS_LOADER_ID = 803;

    private static final String CURRENT_ID = "currentPosition";
    private static final String CURRENT_RECIPE_ID = "currentRecipeId";

    private static final String MASTER_FRAGMENT_TAG = "master_fragment";
    private static final String FLOW_FRAGMENT_TAG = "flow_fragment";

    private boolean TwoPaneUI;

    private MasterFragment mMasterFragment;
    private FlowFragment mFlowFragment;

    private SimpleExoPlayer mPlayer;

    private long mCurrentPlayerPosition = 0;

    @BindView(R.id.tv_step_long_description)
    TextView mStepDescription;

    @BindView(R.id.next_btn)
    Button mNextButton;

    @BindView(R.id.previous_btn)
    Button mPreviousButton;


    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mExoPlayerView ;

    @BindView(R.id.iv_no_video)
    ImageView mNoVideoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Intent callingIntent = getIntent();


        if(callingIntent.hasExtra(CURRENT_ID)){
            mCurrentPosition = callingIntent.getIntExtra(CURRENT_ID , 0);
            Log.d(LOG_TAG , "the intent value Id " + Integer.toString(mCurrentPosition));
        }
        if(callingIntent.hasExtra(CURRENT_RECIPE_ID)){
            mCurrentRecipeId = callingIntent.getIntExtra(CURRENT_RECIPE_ID , -1);
            Log.d(LOG_TAG , "Current Recipe Id is " + mCurrentRecipeId);
        }

        if(savedInstanceState != null){

            if(savedInstanceState.containsKey(CURRENT_PLAYER_POSITION)){
                mCurrentPlayerPosition = savedInstanceState.getLong(CURRENT_PLAYER_POSITION, 0);
            }
            if(savedInstanceState.containsKey(CURRENT_ID)){
                mCurrentPosition = savedInstanceState.getInt(CURRENT_ID);
            }
            if(savedInstanceState.containsKey(CURRENT_RECIPE_ID)){
                mCurrentRecipeId = savedInstanceState.getInt(CURRENT_RECIPE_ID);
            }
        }

        if(findViewById(R.id.master_fragment_container) != null){
            TwoPaneUI = true;


            FragmentManager fragmentManager = getSupportFragmentManager();

            Fragment savedFragment = fragmentManager.findFragmentByTag(MASTER_FRAGMENT_TAG);
            if(savedFragment == null){
                mMasterFragment = new MasterFragment();
                mMasterFragment.setmContext(this , this);
                fragmentManager.beginTransaction()
                        .add(R.id.master_fragment_container, mMasterFragment , MASTER_FRAGMENT_TAG)
                        .commit();
            }else{
                fragmentManager.beginTransaction()
                        .replace(R.id.master_fragment_container , savedFragment , MASTER_FRAGMENT_TAG);
                mMasterFragment = (MasterFragment) savedFragment;
                mMasterFragment.setmContext(this , this);
            }

            Fragment savedFragment2 = fragmentManager.findFragmentByTag(FLOW_FRAGMENT_TAG);
            if(savedFragment2 == null){
                mFlowFragment = new FlowFragment();
                mFlowFragment.setmContext(this);
                fragmentManager.beginTransaction()
                        .add(R.id.flow_fragment_container, mFlowFragment , FLOW_FRAGMENT_TAG)
                        .commit();
            }else{
                fragmentManager.beginTransaction()
                        .replace(R.id.flow_fragment_container , savedFragment2 , FLOW_FRAGMENT_TAG);
                mFlowFragment = (FlowFragment) savedFragment2;
                mFlowFragment.setmContext(this);
            }

        }else{
            TwoPaneUI = false;
            ButterKnife.bind(this);
        }

        getRecipeSteps();
        getSupportLoaderManager().initLoader(RECIPE_STEPS_LOADER_ID , null , this);
        if(TwoPaneUI){
            getSupportLoaderManager().initLoader(RECIPE_INGREDIENTS_LOADER_ID , null , this);

        }else{
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnToPrevious();
                }
            });

            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToNext();
                }
            });
        }
    }


    private void goToNext(){
        mCurrentPosition++;
        if(mRecipeSteps.moveToPosition(mCurrentPosition)){

        }else{
            mCurrentPosition--;
        }
        changeButtons();
        populateData();
        preparePlayerMediaSource();
    }

    private void returnToPrevious(){
        mCurrentPosition--;
        if(mRecipeSteps.moveToPrevious()){

        }else{
            mCurrentPosition++;
        }
        changeButtons();
        populateData();
        preparePlayerMediaSource();
    }

    private void changeButtons(){
        mRecipeSteps.moveToPosition(mCurrentPosition);
        if(mRecipeSteps.isLast()){
            mNextButton.setVisibility(View.INVISIBLE);
        }else{
            mNextButton.setVisibility(View.VISIBLE);
        }

        if(mRecipeSteps.isFirst())
        {
            mPreviousButton.setVisibility(View.INVISIBLE);
        }else{
            mPreviousButton.setVisibility(View.VISIBLE);
        }
    }

    private void initializeExoPlayer(){
        if(mPlayer == null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(this,
                    new DefaultTrackSelector(), new DefaultLoadControl());

            mPlayer.setPlayWhenReady(true);
            mExoPlayerView.setPlayer(mPlayer);
        }
    }

    private void preparePlayerMediaSource(){
        if(mPlayer == null) return;
        mPlayer.stop();
        Log.d(LOG_TAG , "This is the current Position " + mCurrentPosition);
        mRecipeSteps.moveToPosition(mCurrentPosition);

        Uri videoUri = NetworkUtils.buildVideoUri(mRecipeSteps.getString(mRecipeSteps.getColumnIndex(DataContract.StepEntry.VIDEO_COL)));
        MediaSource mediaSource = buildMediaSource(videoUri);
        mPlayer.prepare(mediaSource);
        mPlayer.seekTo(mCurrentPlayerPosition);
    }

    private MediaSource buildMediaSource(Uri uri){
        String userAgent = Util.getUserAgent(this , "Baking_App");
        return new ExtractorMediaSource(uri , new DefaultHttpDataSourceFactory(userAgent)
        ,new DefaultExtractorsFactory() , null , null);
    }

    private void populateData(){
        if(mRecipeSteps == null) return;
        mRecipeSteps.moveToPosition(mCurrentPosition);
        mStepDescription.setText(mRecipeSteps.getString(mRecipeSteps.getColumnIndex(DataContract.StepEntry.DESCRIPTION_COL)));
        String videoValue = mRecipeSteps.getString(mRecipeSteps.getColumnIndex(DataContract.StepEntry.VIDEO_COL));
        if(videoValue == null || videoValue.isEmpty()){
            mExoPlayerView.setVisibility(View.INVISIBLE);
            mNoVideoImage.setVisibility(View.VISIBLE);
        }else{
            mExoPlayerView.setVisibility(View.VISIBLE);
            mNoVideoImage.setVisibility(View.INVISIBLE);
        }
        changeButtons();
    }

    private void getRecipeSteps(){
        LoaderManager lm = getSupportLoaderManager();
        Loader<Cursor> stepsLoader = lm.getLoader(RECIPE_STEPS_LOADER_ID);
        if(stepsLoader != null){
            lm.restartLoader(RECIPE_STEPS_LOADER_ID , null , this);
        }else{
            lm.initLoader(RECIPE_STEPS_LOADER_ID , null , this);
        }
    }

    private void releasePlayer(){
        if(mPlayer == null) return;
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if(id == RECIPE_STEPS_LOADER_ID){
            return new android.support.v4.content.AsyncTaskLoader<Cursor>(this) {
                Cursor stepsCursor;
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if(stepsCursor  == null){
                        forceLoad();
                    }else{
                        deliverResult(stepsCursor);
                    }
                }

                @Nullable
                @Override
                public Cursor loadInBackground() {
                    stepsCursor = null;
                    try {
                        String[] selectionArgs = new String[]{Integer.toString(mCurrentRecipeId)};
                        stepsCursor = getContentResolver().query(DataContract.StepEntry.CONTENT_URI ,
                                null , null , selectionArgs , null);
                    }catch(Exception e){

                    }
                    return stepsCursor;
                }

                @Override
                public void deliverResult(@Nullable Cursor data) {
                    super.deliverResult(data);
                    stepsCursor = data;
                }
            };
        }else if(id == RECIPE_INGREDIENTS_LOADER_ID){

            return new android.support.v4.content.AsyncTaskLoader<Cursor>(this) {
                Cursor mIngredientCursor;
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if(mIngredientCursor != null){
                        deliverResult(mIngredientCursor);
                    }else{
                        forceLoad();
                    }
                }

                @Nullable
                @Override
                public Cursor loadInBackground() {
                    mIngredientCursor = null;
                    try{
                        String[] selectionArgs = new String[]{Integer.toString(mCurrentRecipeId)};
                        mIngredientCursor = getContentResolver().query(DataContract.IngredientEntry.CONTENT_URI,
                                null , null , selectionArgs , null);

                    }catch (Exception e){
                        Log.d(LOG_TAG , e.getMessage());
                    }
                    return mIngredientCursor;
                }

                @Override
                public void deliverResult(@Nullable Cursor data) {
                    super.deliverResult(data);
                    mIngredientCursor = data;
                }
            };
        }else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == RECIPE_STEPS_LOADER_ID){
            if(data != null){
                Log.d(LOG_TAG , "Got recipe steps Data");
                mRecipeSteps = data;

                if(TwoPaneUI){
                    mMasterFragment.setmStepsCursor(data);
                    mMasterFragment.setUserVisibleHint(true);

                    data.moveToFirst();
                    Step step = new Step();
                    step.setId(data.getInt(data.getColumnIndex(DataContract.StepEntry.ID_COL)));
                    step.setNumber(data.getInt(data.getColumnIndex(DataContract.StepEntry.NUMBER_COL)));
                    step.setDescription(data.getString(data.getColumnIndex(DataContract.StepEntry.DESCRIPTION_COL)));
                    step.setVideo(data.getString(data.getColumnIndex(DataContract.StepEntry.VIDEO_COL)));
                    step.setShortDescription(data.getString(data.getColumnIndex(DataContract.StepEntry.S_DESCRIPTION_COL)));
                    step.setRecipeId(data.getInt(data.getColumnIndex(DataContract.StepEntry.RECIPE_ID_COL)));
                    mFlowFragment.setmCurrentStep(step);
                    mFlowFragment.setUserVisibleHint(true);

                }else{
                    populateData();
                }
                preparePlayerMediaSource();
            }else{
                Log.d(LOG_TAG , "The whole Steps Cursor data is null");
            }

        }else if(loader.getId() == RECIPE_INGREDIENTS_LOADER_ID){
            if(data != null){
                if(TwoPaneUI){
                    mMasterFragment.setmIngredientCursor(data);
                    mMasterFragment.setUserVisibleHint(true);
                }
            }else{
                Log.d(LOG_TAG , "Null Ingredients Data");
            }
        }else{
            Log.d(LOG_TAG , "Invalid Loader Id");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(int position) {

        mRecipeSteps.moveToPosition(position);
        Step step = new Step();
        step.setId(mRecipeSteps.getInt(mRecipeSteps.getColumnIndex(DataContract.StepEntry.ID_COL)));
        step.setNumber(mRecipeSteps.getInt(mRecipeSteps.getColumnIndex(DataContract.StepEntry.NUMBER_COL)));
        step.setDescription(mRecipeSteps.getString(mRecipeSteps.getColumnIndex(DataContract.StepEntry.DESCRIPTION_COL)));
        step.setVideo(mRecipeSteps.getString(mRecipeSteps.getColumnIndex(DataContract.StepEntry.VIDEO_COL)));
        step.setShortDescription(mRecipeSteps.getString(mRecipeSteps.getColumnIndex(DataContract.StepEntry.S_DESCRIPTION_COL)));
        step.setRecipeId(mRecipeSteps.getInt(mRecipeSteps.getColumnIndex(DataContract.StepEntry.RECIPE_ID_COL)));
        mFlowFragment.setmCurrentStep(step);
        mFlowFragment.setUserVisibleHint(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(CURRENT_PLAYER_POSITION, mCurrentPlayerPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        if(mPlayer != null){
            mCurrentPlayerPosition = mPlayer.getCurrentPosition();
        }
        releasePlayer();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeExoPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeExoPlayer();
        getSupportLoaderManager().restartLoader(RECIPE_INGREDIENTS_LOADER_ID , null  , this);
        getSupportLoaderManager().restartLoader(RECIPE_STEPS_LOADER_ID , null , this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }
}
