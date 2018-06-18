package com.himebaugh.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.himebaugh.bakingapp.adapter.RecipeCardAdapter;
import com.himebaugh.bakingapp.database.AppDatabase;
import com.himebaugh.bakingapp.database.IngredientEntry;
import com.himebaugh.bakingapp.database.RecipeEntry;
import com.himebaugh.bakingapp.database.StepEntry;

import java.util.List;


// This fragment displays all of the AndroidMe images in one large list
// The list appears as a grid of images
public class MasterListFragment extends Fragment {

    private final static String TAG = MasterListFragment.class.getName();

    private AppDatabase mDb;
    RecyclerView mRecyclerView;
    private RecipeCardAdapter mAdapter;

    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // May save position and reset upon orientation change???
        // layoutManager.scrollToPosition(0);

        mRecyclerView.setLayoutManager(layoutManager);
        // allows for optimizations if all items are of the same size:
        mRecyclerView.setHasFixedSize(true);

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new RecipeCardAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        // Return the root view
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = AppDatabase.getInstance(getContext());

        setupViewModel();
    }

    private void setupViewModel() {

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getRecipes().observe(this, new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                Log.d(TAG, "Updating list of recipes from LiveData in ViewModel");

                Log.i(TAG, "recipeEntries.size(): " + recipeEntries.size());

                // TODO: Create Adapter
                mAdapter.loadRecipes(recipeEntries);

                Log.i(TAG, "mAdapter.getItemCount(): " + mAdapter.getItemCount() );
            }
        });
        viewModel.getIngredients(2).observe(this, new Observer<List<IngredientEntry>>() {
            @Override
            public void onChanged(@Nullable List<IngredientEntry> ingredientEntries) {
                Log.d(TAG, "Updating list of ingredients from LiveData in ViewModel");

                Log.i(TAG, "ingredientEntries.size(): " + ingredientEntries.size());
            }
        });
        viewModel.getSteps(1).observe(this, new Observer<List<StepEntry>>() {
            @Override
            public void onChanged(@Nullable List<StepEntry> stepEntries) {
                Log.d(TAG, "Updating list of steps from LiveData in ViewModel");

                Log.i(TAG, "stepEntries.size(): " + stepEntries.size());
            }
        });
    }

}
