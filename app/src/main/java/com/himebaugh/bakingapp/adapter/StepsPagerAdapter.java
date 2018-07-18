package com.himebaugh.bakingapp.adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.RecipeStepDetailActivity;
import com.himebaugh.bakingapp.RecipeStepDetailFragment;
import com.himebaugh.bakingapp.RecipeStepListActivity;
import com.himebaugh.bakingapp.database.StepEntry;

import java.util.List;

public class StepsPagerAdapter extends FragmentPagerAdapter {

    private final static String TAG = StepsPagerAdapter.class.getName();

    private Context mContext;
    private List<StepEntry> mStepList;
    private int mCurrentStepNumber;

    public StepsPagerAdapter(Context context, List<StepEntry> stepList, FragmentManager fragmentManager, int stepNumber) {
        super(fragmentManager);
        this.mContext = context;
        this.mStepList = stepList;
        this.mCurrentStepNumber = stepNumber;
    }

    @Override
    public Fragment getItem(int position) {

        Log.i(TAG, "getItem: position="+position);

        Bundle arguments = new Bundle();
        arguments.putInt(RecipeStepListActivity.EXTRA_RECIPE_ID, mStepList.get(position).getRecipeId() );
        arguments.putInt(RecipeStepListActivity.EXTRA_STEP_NUMBER, mStepList.get(position).getStepNumber() );
        arguments.putInt(RecipeStepDetailActivity.EXTRA_CURRENT_STEP_NUMBER, mCurrentStepNumber );
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.format(mContext.getString(R.string.step), position);
    }

    @Override
    public int getCount() {
        return mStepList.size();
    }


}
