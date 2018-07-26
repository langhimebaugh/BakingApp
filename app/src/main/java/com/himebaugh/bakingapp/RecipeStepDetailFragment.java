package com.himebaugh.bakingapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.himebaugh.bakingapp.database.DataViewModel;
import com.himebaugh.bakingapp.database.RecipeEntry;
import com.himebaugh.bakingapp.database.StepEntry;
import com.himebaugh.bakingapp.utils.NetworkUtil;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment implements Player.EventListener {


    private final static String TAG = RecipeStepDetailFragment.class.getName();

    private static final int DEFAULT_RECIPE_ID = -1;
    private int mRecipeId = DEFAULT_RECIPE_ID;

    private Toolbar toolbar;

    private static final String PLAYER_POSITION = "player_current_position";
    private static final String PLAYER_PLAY_WHEN_READY = "player_play_when_ready";

    private long mCurrentPosition = 0;

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private ImageView mThumbnailView;
    private Uri mVideoUri;
    private String mThumbnailUrl;
    private TextView mDetailTextView;
    private NestedScrollView mScrollView;

    private boolean mPlayWhenReady = true;

    private boolean mLandscapeMobileLayout;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = this.getActivity();

        toolbar = activity.findViewById(R.id.detail_toolbar);

        if (getArguments().containsKey(RecipeStepListActivity.EXTRA_RECIPE_ID)) {
            mRecipeId = getArguments().getInt(RecipeStepListActivity.EXTRA_RECIPE_ID);
            int stepNumber = getArguments().getInt(RecipeStepListActivity.EXTRA_STEP_NUMBER);

            // This argument will only exist for phones, not tablets!
            if (getArguments().containsKey(RecipeStepDetailActivity.EXTRA_CURRENT_STEP_NUMBER)) {

                // if the current fragment step equals the step from RecipeStepDetailActivity set to true...
                if (stepNumber == getArguments().getInt(RecipeStepDetailActivity.EXTRA_CURRENT_STEP_NUMBER)) {
                    mPlayWhenReady = true;
                } else {
                    mPlayWhenReady = false;
                }

            }

            loadStepFromViewModel(mRecipeId, stepNumber);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        // Initialize the player view.
        mPlayerView = rootView.findViewById(R.id.player_view);

        mScrollView = rootView.findViewById(R.id.scroll_view);
        mThumbnailView = rootView.findViewById(R.id.iv_recipe_step_thumbnail);
        mDetailTextView = rootView.findViewById(R.id.tv_recipe_step_detail);

        if (rootView.findViewById(R.id.landscape_mobile_layout) != null) {
            mLandscapeMobileLayout = true;
        } else {
            mLandscapeMobileLayout = false;
        }

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(PLAYER_POSITION)) {
                mCurrentPosition = savedInstanceState.getLong(PLAYER_POSITION);
            }
            if (savedInstanceState.containsKey(PLAYER_PLAY_WHEN_READY)) {
                mPlayWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY);
            }

        }

        return rootView;
    }

    private void loadStepFromViewModel(int recipeID, int stepNumber) {

        DataViewModel viewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        viewModel.getRecipe(recipeID).observe(this, new Observer<RecipeEntry>() {
            @Override
            public void onChanged(@Nullable RecipeEntry recipeEntry) {

                Log.i(TAG, "LANG: >>>>>" + recipeEntry.getName());

                if (toolbar != null) {
                    toolbar.setTitle(recipeEntry.getName());
                }

            }
        });

        viewModel.getStep(recipeID, stepNumber).observe(this, new Observer<StepEntry>() {
            @Override
            public void onChanged(@Nullable StepEntry stepEntry) {

                mDetailTextView.setText(stepEntry.getDescription());

                mVideoUri = Uri.parse(stepEntry.getVideoURL()).buildUpon().build();
                mThumbnailUrl = stepEntry.getThumbnailURL();

                // if internet available...
                if (NetworkUtil.isNetworkAvailable((getContext()))) {

                    // if VideoURL is available, show the video
                    if (!mVideoUri.toString().isEmpty()) {
                        mPlayerView.setVisibility(View.VISIBLE);
                        mThumbnailView.setVisibility(View.INVISIBLE);
                        if (mLandscapeMobileLayout) {
                            mScrollView.setVisibility(View.GONE);
                            mDetailTextView.setVisibility(View.INVISIBLE);
                        } else {
                            mDetailTextView.setVisibility(View.VISIBLE);
                        }
                        initializePlayer(mVideoUri);

                    } else {
                        // otherwise show the thumbnail image
                        mScrollView.setVisibility(View.VISIBLE);
                        mThumbnailView.setVisibility(View.VISIBLE);
                        mDetailTextView.setVisibility(View.VISIBLE);
                        mPlayerView.setVisibility(View.GONE);
                        displayImage(mThumbnailUrl);
                    }

                } else {
                    // otherwise show the thumbnail image
                    // will default to local resource file
                    mScrollView.setVisibility(View.VISIBLE);
                    mThumbnailView.setVisibility(View.VISIBLE);
                    mDetailTextView.setVisibility(View.VISIBLE);
                    mPlayerView.setVisibility(View.GONE);
                    displayImage(mThumbnailUrl);
                }

            }
        });

    }

    /**
     * Initialize ExoPlayer.
     *
     * @param videoUri The URI of the sample to play.
     */
    private void initializePlayer(Uri videoUri) {

        Log.i(TAG, "initializePlayer: mediaUri=" + videoUri.toString());

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            // LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mPlayerView.setPlayer(mExoPlayer);
            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);

            mExoPlayer.prepare(mediaSource);
            // onRestore
            if (mCurrentPosition != 0)
                mExoPlayer.seekTo(mCurrentPosition);

            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            //mExoPlayer.setPlayWhenReady(true);
            mPlayerView.setVisibility(View.VISIBLE);
        }
    }

    // if a recipe step has no video, or can't load videos due to no network connection
    private void displayImage(String thumbnailUrl) {

        if (thumbnailUrl.isEmpty()) {
            Picasso.get()
                    .load(R.drawable.recipe_image_placeholder)
                    .placeholder(R.drawable.recipe_image_placeholder)
                    .error(R.drawable.recipe_image_placeholder)
                    .into(mThumbnailView);
        } else {
            Picasso.get()
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.recipe_image_placeholder)
                    .error(R.drawable.recipe_image_placeholder)
                    .into(mThumbnailView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        if (Util.SDK_INT > 23) {
            //
            if (mVideoUri != null) {
                initializePlayer(mVideoUri);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            //
            if (mVideoUri != null) {
                initializePlayer(mVideoUri);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");

//        if (mExoPlayer != null) {
//            mCurrentPosition = mExoPlayer.getCurrentPosition();
//            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
//            releasePlayer();
//        }

//        if (mExoPlayer != null) {
//            mCurrentPosition = mExoPlayer.getCurrentPosition();
//            // mPlayWhenReady = mExoPlayer.getPlayWhenReady();
//            releasePlayer();
//        }
//        if (Util.SDK_INT <= 23) {
//            if (mExoPlayer != null) {
//                mCurrentPosition = mExoPlayer.getCurrentPosition();
//                mPlayWhenReady = mExoPlayer.getPlayWhenReady();
//                releasePlayer();
//            }
//        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(TAG, "onStop: ");

        //===============================================================================================
        // This works for API 23 & 26 On Rotation, On Home (circle) button, On Recents (square) button
        
        // To save persistent data, such as user preferences or data for a database, you should take appropriate 
        // opportunities when your activity is in the foreground. 
        // If no such opportunity arises, you should save such data during the onStop() method.
        //===============================================================================================
        if (mExoPlayer != null) {
            mCurrentPosition = mExoPlayer.getCurrentPosition();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            releasePlayer();
        }
//        if (Util.SDK_INT > 23) {
//            if (mExoPlayer != null) {
//                mCurrentPosition = mExoPlayer.getCurrentPosition();
//                mPlayWhenReady = mExoPlayer.getPlayWhenReady();
//                releasePlayer();
//            }
//        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
//        if (mExoPlayer != null) {
//            releasePlayer();
//        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        Log.i(TAG, "onPlayerStateChanged: playWhenReady" + playWhenReady);
        Log.i(TAG, "onPlayerStateChanged: playbackState" + playbackState);

        if (playbackState == Player.STATE_READY) {

            Log.i(TAG, "onPlayerStateChanged: mExoPlayer.getCurrentPosition()" + mExoPlayer.getCurrentPosition());

        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        if (mExoPlayer != null) {
            outState.putLong(PLAYER_POSITION, mExoPlayer.getCurrentPosition());
            outState.putBoolean(PLAYER_PLAY_WHEN_READY, mExoPlayer.getPlayWhenReady());
        }

        super.onSaveInstanceState(outState);
    }
}