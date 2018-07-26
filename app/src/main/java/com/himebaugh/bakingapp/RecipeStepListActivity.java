package com.himebaugh.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.himebaugh.bakingapp.adapter.IngredientsAdapter;
import com.himebaugh.bakingapp.adapter.StepsAdapter;
import com.himebaugh.bakingapp.database.AppDatabase;
import com.himebaugh.bakingapp.database.DataViewModel;
import com.himebaugh.bakingapp.database.IngredientEntry;
import com.himebaugh.bakingapp.database.RecipeEntry;
import com.himebaugh.bakingapp.database.StepEntry;

import java.util.List;

/**
 * An activity representing a list of Items.
 * This activity has different presentations for handset and tablet-size devices.
 * On handsets, the activity presents a list of items, which when touched,
 * lead to a ItemDetailActivity representing item details.
 * On tablets, the activity presents the list of items and item details side-by-side
 * using two vertical panes.
 */
public class RecipeStepListActivity extends AppCompatActivity implements StepsAdapter.StepAdapterOnClickHandler {

    private final static String TAG = RecipeStepListActivity.class.getName();
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id"; // Extra for the recipe ID to be received in the intent
    public static final String EXTRA_STEP_NUMBER = "extra_step_number";
    public static final String EXTRA_STEP_LIST = "extra_step_list";

    /* PREFS */
    public static final String PREF_RECIPE_ID = "pref_recipe_id";

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_RECIPE_ID = -1;

    private int mRecipeId = DEFAULT_RECIPE_ID;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean mTwoPane;
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;
    private AppDatabase mDb;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_item_list);
        setContentView(R.layout.activity_recipe_step_list);

        // Show the Up button in the action bar.
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }


        // When returning from RecipeStepDetailActivity via the back button
        // no arguments are passed.  How do I know what the RecipeID was?
        // Anyway to pass back the ID with the back button???
        // See below...

        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt("recipeId");
        } else {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(EXTRA_RECIPE_ID)) {
                Log.i(TAG, "onCreate: ");
                mRecipeId = intent.getIntExtra(EXTRA_RECIPE_ID, DEFAULT_RECIPE_ID);
                // mRecipeId = (int) intent.getLongExtra(EXTRA_RECIPE_ID, DEFAULT_RECIPE_ID);
                Log.i(TAG, "onCreate: mRecipeId="+mRecipeId);
            } else {

                // Maybe a better way, but...
                // When returning from RecipeStepDetailActivity via the back button, no arguments are passed.
                // Use the RecipeID that was last stored in shared preferences in the onClick method.
                // For phones not tablets.
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                mRecipeId = sharedPreferences.getInt(PREF_RECIPE_ID, DEFAULT_RECIPE_ID);
            }
        }


        Log.i(TAG, "onCreate: mRecipeId=" + mRecipeId);

        if (findViewById(R.id.recipe_step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-sw600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        RecyclerView ingredientsRecyclerView = findViewById(R.id.ingredients_recyclerView);
        // if (ingredientsRecyclerView == null) throw new AssertionError();
        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(this);
        ingredientsRecyclerView.setLayoutManager(ingredientLayoutManager);
        ingredientsRecyclerView.setHasFixedSize(true);
        mIngredientsAdapter = new IngredientsAdapter();
        ingredientsRecyclerView.setAdapter(mIngredientsAdapter);

        RecyclerView stepsRecyclerView = findViewById(R.id.steps_recyclerView);
        // if (stepsRecyclerView == null) throw new AssertionError();
        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(this);
        stepsRecyclerView.setLayoutManager(stepLayoutManager);
        stepsRecyclerView.setHasFixedSize(true);
        mStepsAdapter = new StepsAdapter(this);
        stepsRecyclerView.setAdapter(mStepsAdapter);

        mDb = AppDatabase.getInstance(this);

        loadAdaptersFromViewModel(mRecipeId);
    }

    private void loadAdaptersFromViewModel(int recipeID) {

        DataViewModel viewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        viewModel.getRecipe(recipeID).observe(this, new Observer<RecipeEntry>() {
            @Override
            public void onChanged(@Nullable RecipeEntry recipeEntry) {

                // set page title
                if (mActionBar != null) {
                    mActionBar.setTitle(recipeEntry.getName());
                }
            }
        });


        //
        // Log.i(TAG, "viewModel.getCursorOfRecipes().getCount(): " + viewModel.getCursorOfRecipes().getCount());


        viewModel.getIngredients(recipeID).observe(this, new Observer<List<IngredientEntry>>() {
            @Override
            public void onChanged(@Nullable List<IngredientEntry> ingredientEntries) {
                Log.d(TAG, "Updating list of ingredients from LiveData in ViewModel");
                Log.i(TAG, "ingredientEntries.size(): " + ingredientEntries.size());
                mIngredientsAdapter.loadIngredients(ingredientEntries);
            }
        });
        viewModel.getSteps(recipeID).observe(this, new Observer<List<StepEntry>>() {
            @Override
            public void onChanged(@Nullable List<StepEntry> stepEntries) {
                Log.d(TAG, "Updating list of steps from LiveData in ViewModel");
                Log.i(TAG, "stepEntries.size(): " + stepEntries.size());
                mStepsAdapter.loadSteps(stepEntries);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("recipeId", mRecipeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipeId = savedInstanceState.getInt("recipeId");
    }

    @Override
    public void onClick(@NonNull StepEntry step) {

        // Save Recipe Id
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_RECIPE_ID, mRecipeId);
        editor.commit();


        Log.i(TAG, "onClick: step.getVideoURL()=" + step.getVideoURL());

        // play video

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(EXTRA_RECIPE_ID, mRecipeId);
            arguments.putInt(EXTRA_STEP_NUMBER, step.getStepNumber());
            //arguments.putString(RecipeStepDetailFragment.ARG_ITEM_ID, "1");
            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(EXTRA_RECIPE_ID, mRecipeId);
            intent.putExtra(EXTRA_STEP_NUMBER, step.getStepNumber());
            startActivity(intent);
        }
    }

}
