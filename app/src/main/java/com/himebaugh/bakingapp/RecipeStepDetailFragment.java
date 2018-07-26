package com.himebaugh.bakingapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    // public static final String EXTRA_RECIPE_ID = "extra_recipe_id";
    // public static final String EXTRA_STEP_NUMBER = "extra_step_number";

    private static final int DEFAULT_RECIPE_ID = -1;
    private int mRecipeId = DEFAULT_RECIPE_ID;

    // private CollapsingToolbarLayout appBarLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;


    // =========================================================================
    public static final String STEP_KEY = "step_k";

    private static final String PLAYER_POSITION = "player_current_position";
    private static final String PLAYER_PLAY_WHEN_READY = "player_play_when_ready";

    private long mCurrentPosition = 0;
    //private boolean mPlayWhenReady = true;
    // =========================================================================

    // TextView recipeStepDetailTextView;

    private SimpleExoPlayer mExoPlayer;
    // private SimpleExoPlayerView mPlayerView;
    private PlayerView mPlayerView;
    private ImageView mThumbnailView;
    private Uri mVideoUri;
    private String mThumbnailUrl;
    private TextView mDetailTextView;
    private NestedScrollView mScrollView;

    private boolean mPlayWhenReady = true;

    private boolean mLandscapeMobileLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = this.getActivity();
        // appBarLayout = activity.findViewById(R.id.toolbar_layout);
        // appBarLayout = activity.findViewById(R.id.app_bar);
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

        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.recipe_step_detail)).setText(mItem.details);
//        }
        //recipeStepDetailTextView = rootView.findViewById(R.id.tv_recipe_step_detail);

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

        // Load the question mark as the background image until the user answers the question.
        // mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));


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

                // set page title
//                if (appBarLayout != null) {
//                    appBarLayout.setTitle(recipeEntry.getName());
//                }
                if (toolbar != null) {
                    toolbar.setTitle(recipeEntry.getName());
                }

                //temp.setText(recipeEntry.getName());

            }
        });

        viewModel.getStep(recipeID, stepNumber).observe(this, new Observer<StepEntry>() {
            @Override
            public void onChanged(@Nullable StepEntry stepEntry) {

                mDetailTextView.setText(stepEntry.getDescription());

                mVideoUri = Uri.parse(stepEntry.getVideoURL()).buildUpon().build();
                mThumbnailUrl = stepEntry.getThumbnailURL();

                //===================================
                // if video not available...
                // set...
                // player_view = invisible
                // iv_recipe_step_thumbnail = visible
                // tv_recipe_step_detail = visible
                //===================================

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
        if (Util.SDK_INT <= 23) {
            if (mExoPlayer != null) {
                mCurrentPosition = mExoPlayer.getCurrentPosition();
                releasePlayer();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (mExoPlayer != null) {
                mCurrentPosition = mExoPlayer.getCurrentPosition();
                releasePlayer();
            }
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
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

//        if (playbackState == Player.STATE_ENDED) {
//            // showControls();
//            debugRootView.setVisibility(View.VISIBLE);
//        }
//        updateButtonVisibilities();
//
//
//        if ((playbackState == Player.STATE_READY) && playWhenReady) {
//            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
//                    playerCurrentPosition, 1f);
//            videoThumbnailIv.setVisibility(View.INVISIBLE); //hide thumbnail to play
//        } else if ((playbackState == Player.STATE_READY)) {
//            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
//                    playerCurrentPosition, 1f);
//            videoThumbnailIv.setVisibility(View.VISIBLE); // show thumbnail when paused
//        }
//        videoSession.setPlaybackState(stateBuilder.build());
//
//
//        if ((playbackState == Player.STATE_READY) && playWhenReady) {
//            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
//        } else if ((playbackState == Player.STATE_READY)) {
//            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
//        }
//        mediaSession.setPlaybackState(stateBuilder.build());
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
        // outState.putLong(PLAYER_POSITION, mExoPlayer.getCurrentPosition());
        // outState.putBoolean(PLAYER_PLAY_WHEN_READY, mExoPlayer.getPlayWhenReady());

        super.onSaveInstanceState(outState);
    }
}
