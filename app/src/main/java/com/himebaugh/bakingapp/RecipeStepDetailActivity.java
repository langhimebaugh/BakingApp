package com.himebaugh.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.himebaugh.bakingapp.adapter.StepsPagerAdapter;
import com.himebaugh.bakingapp.database.DataViewModel;
import com.himebaugh.bakingapp.database.StepEntry;

import java.util.List;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepListActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private final static String TAG = RecipeStepDetailActivity.class.getName();

    private static final int DEFAULT_RECIPE_ID = -1;
    private int mRecipeId = DEFAULT_RECIPE_ID;
    private static final int DEFAULT_STEP_NUMBER = 0;
    private int mStepNumber = DEFAULT_STEP_NUMBER;

    public static final String EXTRA_CURRENT_STEP_NUMBER = "extra_current_step_number";

    private StepsPagerAdapter mStepsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt("recipeId2");
            mStepNumber = savedInstanceState.getInt("stepNumber");
        } else {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(RecipeStepListActivity.EXTRA_RECIPE_ID)) {
                mRecipeId = intent.getIntExtra(RecipeStepListActivity.EXTRA_RECIPE_ID, DEFAULT_RECIPE_ID);
                mStepNumber = intent.getIntExtra(RecipeStepListActivity.EXTRA_STEP_NUMBER, DEFAULT_STEP_NUMBER);
            }
        }

        // Set up the ViewPager with the steps adapter.
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(this);

        loadPagerAdapterFromViewModel(mRecipeId,mStepNumber);
    }

    private void loadPagerAdapterFromViewModel(int recipeID, final int stepNumber) {

        DataViewModel viewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        viewModel.getSteps(recipeID).observe(this, new Observer<List<StepEntry>>() {
            @Override
            public void onChanged(@Nullable List<StepEntry> stepEntries) {
                Log.d(TAG, "Updating list of steps from LiveData in ViewModel");
                Log.i(TAG, "stepEntries.size(): " + stepEntries.size());
                //============================================
                // Create the adapter that will return a fragment for each of the Steps
                // stepNumber gets passed through the StepsPagerAdapter to RecipeStepDetailFragment where it will
                // start playing the video only if stepNumber matches the current page.
                mStepsPagerAdapter = new StepsPagerAdapter(getBaseContext(),stepEntries,getSupportFragmentManager(), stepNumber);

                mViewPager.setAdapter(mStepsPagerAdapter);

                // display the selected step...
                mViewPager.setCurrentItem(stepNumber);

                mTabLayout = findViewById(R.id.tab_layout);
                mTabLayout.setupWithViewPager(mViewPager);

                //mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
                //mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
                //============================================
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, RecipeStepListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("recipeId2", mRecipeId);
        outState.putInt("stepNumber", mStepNumber);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipeId = savedInstanceState.getInt("recipeId2");
        mStepNumber = savedInstanceState.getInt("stepNumber");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        Log.i(TAG, "onPageScrolled: position="+position);
    }

    @Override
    public void onPageSelected(int position) {

        Log.i(TAG, "onPageSelected: position="+position);

        //setCurrentItem(position);
        mStepNumber = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

        Log.i(TAG, "onClick: v.getId()="+v.getId());
//        int position = indexes.get(v.getId());
//        if (mViewPager.getCurrentItem() != position) {
//            mViewPager.setCurrentItem(position, false);
//        }
    }
}
