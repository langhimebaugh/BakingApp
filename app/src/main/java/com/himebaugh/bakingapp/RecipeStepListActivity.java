package com.himebaugh.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.himebaugh.bakingapp.adapter.IngredientAdapter;
import com.himebaugh.bakingapp.database.AppDatabase;
import com.himebaugh.bakingapp.database.IngredientEntry;
import com.himebaugh.bakingapp.database.RecipeEntry;
import com.himebaugh.bakingapp.database.StepEntry;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a ItemDetailActivity representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepListActivity extends AppCompatActivity {

    private final static String TAG = RecipeStepListActivity.class.getName();
    public static final String EXTRA_RECIPE_ID = "extraRecipeId"; // Extra for the recipe ID to be received in the intent

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_RECIPE_ID = -1;

    private int mRecipeId = DEFAULT_RECIPE_ID;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private IngredientAdapter mAdapter;
    private AppDatabase mDb;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_item_list);
        setContentView(R.layout.recipe_step_list);

        // Show the Up button in the action bar.
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());

        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt("recipeId");
        } else {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(EXTRA_RECIPE_ID)) {
                mRecipeId = intent.getIntExtra(EXTRA_RECIPE_ID, DEFAULT_RECIPE_ID);
            }
        }

        if (findViewById(R.id.recipe_step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // View recyclerView = findViewById(R.id.item_list);

        RecyclerView recyclerView = findViewById(R.id.recipe_step_list);

        // assert recyclerView != null;
        if (recyclerView == null) throw new AssertionError();

        LinearLayoutManager layoutManager;

        layoutManager = new LinearLayoutManager(this);

        // May save position and reset upon orientation change???
        // layoutManager.scrollToPosition(0);

        recyclerView.setLayoutManager(layoutManager);
        // allows for optimizations if all items are of the same size:
        recyclerView.setHasFixedSize(true);

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new IngredientAdapter();
        recyclerView.setAdapter(mAdapter);

        mDb = AppDatabase.getInstance(this);

        loadAdaptersFromViewModel(mRecipeId);
    }

    private void loadAdaptersFromViewModel(int recipeID) {

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getRecipes(recipeID).observe(this, new Observer<RecipeEntry>() {
            @Override
            public void onChanged(@Nullable RecipeEntry recipeEntry) {

                // set page title
                if (mActionBar != null) {
                    mActionBar.setTitle(recipeEntry.getName());
                }
            }
        });


        Log.i(TAG, "viewModel.getCursorOfRecipes().getCount(): " + viewModel.getCursorOfRecipes().getCount());


        viewModel.getIngredients(recipeID).observe(this, new Observer<List<IngredientEntry>>() {
            @Override
            public void onChanged(@Nullable List<IngredientEntry> ingredientEntries) {
                Log.d(TAG, "Updating list of ingredients from LiveData in ViewModel");

                Log.i(TAG, "ingredientEntries.size(): " + ingredientEntries.size());

                // TODO: Create Adapter
                mAdapter.loadIngredients(ingredientEntries);
            }
        });
        viewModel.getSteps(recipeID).observe(this, new Observer<List<StepEntry>>() {
            @Override
            public void onChanged(@Nullable List<StepEntry> stepEntries) {
                Log.d(TAG, "Updating list of steps from LiveData in ViewModel");

                Log.i(TAG, "stepEntries.size(): " + stepEntries.size());
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

}
