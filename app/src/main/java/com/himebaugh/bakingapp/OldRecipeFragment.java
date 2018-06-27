package com.himebaugh.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.himebaugh.bakingapp.adapter.IngredientAdapter;
import com.himebaugh.bakingapp.database.AppDatabase;
import com.himebaugh.bakingapp.database.IngredientEntry;
import com.himebaugh.bakingapp.database.RecipeEntry;
import com.himebaugh.bakingapp.database.StepEntry;

import java.util.List;


// This fragment displays all of the AndroidMe images in one large list
// The list appears as a grid of images
public class OldRecipeFragment extends Fragment {

    private final static String TAG = OldRecipeFragment.class.getName();

    public static final String ARG_RECIPE_ID = "recipe_id";

    private AppDatabase mDb;
    RecyclerView mRecyclerView;
    private IngredientAdapter mAdapter;

    private int mRecipeID;

    // Mandatory empty constructor
    public OldRecipeFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView: 1");
        //final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        Log.i("LANG", "onCreateView: ");

        // Get a reference to the GridView in the fragment_master_list xml layout file
        //GridView gridView = (GridView) rootView.findViewById(R.id.images_grid_view);

        // final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        final View rootView = inflater.inflate(R.layout.recipe_step_list, container, false);

        Log.i(TAG, "onCreateView: 2");

        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager;

        layoutManager = new LinearLayoutManager(getContext());

        // May save position and reset upon orientation change???
        // layoutManager.scrollToPosition(0);

        mRecyclerView.setLayoutManager(layoutManager);
        // allows for optimizations if all items are of the same size:
        mRecyclerView.setHasFixedSize(true);

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new IngredientAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Return the root view
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate: ");

        mDb = AppDatabase.getInstance(getContext());

        if (getArguments() != null && getArguments().containsKey(ARG_RECIPE_ID)) {

            // Load the dummy content specified by the fragment
            loadAdaptersFromViewModel(getArguments().getInt(ARG_RECIPE_ID));
        } else {
            Log.i(TAG, "LANG WAS HERE!");
            loadAdaptersFromViewModel(1);
        }


    }

    private void loadAdaptersFromViewModel(int recipeID) {

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getRecipes().observe(this, new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                Log.d(TAG, "Updating list of recipes from LiveData in ViewModel");

                Log.i(TAG, "recipeEntries.size(): " + recipeEntries.size());

                // TODO: Create Adapter
                // mAdapter.loadRecipes(recipeEntries);

                Log.i(TAG, "mAdapter.getItemCount(): " + mAdapter.getItemCount());
            }
        });
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

    public void setRecipeId(int recipeId) {
        mRecipeID = recipeId;
    }

}
