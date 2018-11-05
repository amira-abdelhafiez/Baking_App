package com.example.amira.bakingapp.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amira.bakingapp.R;
import com.example.amira.bakingapp.data.DataContract;
import com.example.amira.bakingapp.models.Step;
import com.example.amira.bakingapp.utils.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class FlowFragment extends Fragment {

    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView mStepDescription;

    private ImageView mNoVideoImageView;

    private Step mCurrentStep;

    private Context mContext;

    public FlowFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail_fragment , container , false);
        mStepDescription = rootView.findViewById(R.id.tv_step_long_description);

        mNoVideoImageView = rootView.findViewById(R.id.iv_no_video);
        mExoPlayerView = rootView.findViewById(R.id.exo_player_view);


        populateData();
        initializeExoPlayer();
        return rootView;
    }

    private void initializeExoPlayer(){
        if(mCurrentStep == null) return;
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext ,
                new DefaultTrackSelector() , new DefaultLoadControl());

        mExoPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(true);

        preparePlayerMediaSource();
    }
    private void preparePlayerMediaSource(){
        if(mCurrentStep == null) return;
        mExoPlayer.stop();
        Uri videoUri = NetworkUtils.buildVideoUri(mCurrentStep.getVideo());
        MediaSource mediaSource = buildMediaSource(videoUri);
        mExoPlayer.prepare(mediaSource);
    }
    private MediaSource buildMediaSource(Uri uri){
        String userAgent = Util.getUserAgent(mContext , "Baking_App");
        return new ExtractorMediaSource(uri , new DefaultHttpDataSourceFactory(userAgent)
                ,new DefaultExtractorsFactory() , null , null);
    }

    private void populateData(){
        if(mCurrentStep == null) return;
        mStepDescription.setText(mCurrentStep.getDescription());
        String videoValue = mCurrentStep.getVideo();
        if(videoValue == null || videoValue.isEmpty()){
            mExoPlayerView.setVisibility(View.INVISIBLE);
            mNoVideoImageView.setVisibility(View.VISIBLE);
        }else{
            mExoPlayerView.setVisibility(View.VISIBLE);
            mNoVideoImageView.setVisibility(View.INVISIBLE);
        }
    }


    private void releaseExoPlayer(){
        if(mExoPlayer == null) return;
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        releaseExoPlayer();
        super.onDestroy();
    }

    public void setmContext(Context context){
        this.mContext = context;
    }

    public void setmCurrentStep(Step currentStep){
        this.mCurrentStep = currentStep;
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
