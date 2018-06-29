package com.himebaugh.bakingapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.himebaugh.bakingapp.database.RecipeEntry;
import com.himebaugh.bakingapp.database.StepEntry;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    // public static final String EXTRA_RECIPE_ID = "extra_recipe_id";
    // public static final String EXTRA_STEP_NUMBER = "extra_step_number";

    private static final int DEFAULT_RECIPE_ID = -1;
    private int mRecipeId = DEFAULT_RECIPE_ID;
    private CollapsingToolbarLayout appBarLayout;

    private final static String TAG = RecipeStepDetailFragment.class.getName();

    TextView temp;

    /**
     * The dummy content this fragment is presenting.
     */
    // private DummyContent.DummyItem mItem;

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
        appBarLayout = activity.findViewById(R.id.toolbar_layout);

        if (getArguments().containsKey(RecipeStepListActivity.EXTRA_RECIPE_ID)) {
            mRecipeId = getArguments().getInt(RecipeStepListActivity.EXTRA_RECIPE_ID);
            int stepNumber = getArguments().getInt(RecipeStepListActivity.EXTRA_STEP_NUMBER);
            loadAdaptersFromViewModel(mRecipeId, stepNumber);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.recipe_step_detail)).setText(mItem.details);
//        }
        temp = rootView.findViewById(R.id.tv_recipe_step_detail);

        return rootView;
    }

    private void loadAdaptersFromViewModel(int recipeID, int stepNumber) {

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getRecipe(recipeID).observe(this, new Observer<RecipeEntry>() {
            @Override
            public void onChanged(@Nullable RecipeEntry recipeEntry) {

                Log.i(TAG, "LANG: >>>>>" + recipeEntry.getName());

                // set page title
                if (appBarLayout != null) {
                    appBarLayout.setTitle("LANG" + recipeEntry.getName());
                }

                temp.setText(recipeEntry.getName());

            }
        });

        viewModel.getStep(recipeID,stepNumber).observe(this, new Observer<StepEntry>() {
            @Override
            public void onChanged(@Nullable StepEntry stepEntry) {

                Log.i(TAG, "LANG: >>>>>" + stepEntry.getVideoURL());

                temp.setText(stepEntry.getDescription());
            }
        });

//
//        Log.i(TAG, "viewModel.getCursorOfRecipes().getCount(): " + viewModel.getCursorOfRecipes().getCount());
//
//
//        viewModel.getIngredients(recipeID).observe(this, new Observer<List<IngredientEntry>>() {
//            @Override
//            public void onChanged(@Nullable List<IngredientEntry> ingredientEntries) {
//                Log.d(TAG, "Updating list of ingredients from LiveData in ViewModel");
//                Log.i(TAG, "ingredientEntries.size(): " + ingredientEntries.size());
//                mIngredientsAdapter.loadIngredients(ingredientEntries);
//            }
//        });
//        viewModel.getSteps(recipeID).observe(this, new Observer<List<StepEntry>>() {
//            @Override
//            public void onChanged(@Nullable List<StepEntry> stepEntries) {
//                Log.d(TAG, "Updating list of steps from LiveData in ViewModel");
//                Log.i(TAG, "stepEntries.size(): " + stepEntries.size());
//                mStepsAdapter.loadSteps(stepEntries);
//            }
//        });

    }
}
