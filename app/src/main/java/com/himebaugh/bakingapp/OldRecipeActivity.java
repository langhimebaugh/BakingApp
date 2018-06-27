package com.himebaugh.bakingapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class OldRecipeActivity extends AppCompatActivity {

    private final static String TAG = OldRecipeActivity.class.getName();
    public static final String EXTRA_RECIPE_ID = "extraRecipeId"; // Extra for the recipe ID to be received in the intent

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_RECIPE_ID = -1;

    private int mRecipeId = DEFAULT_RECIPE_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState != null) {

            mRecipeId = savedInstanceState.getInt("recipeId");

        } else {

            mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, DEFAULT_RECIPE_ID);
        }

        Bundle arguments = new Bundle();
        arguments.putInt(OldRecipeFragment.ARG_RECIPE_ID, mRecipeId);
        OldRecipeFragment fragment = new OldRecipeFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                //.replace(R.id.master_list_fragment, fragment)
                .replace(R.id.recipe_step_list, fragment)
                .commit();

//        if (savedInstanceState == null) {
//
//            Intent intent = getIntent();
//            if (intent != null && intent.hasExtra(EXTRA_RECIPE_ID)) {
//
//                Log.i(TAG, "intent.hasExtra(EXTRA_RECIPE_ID)");
//
//                if (mRecipeId == DEFAULT_RECIPE_ID) {
//
//                    mRecipeId = intent.getIntExtra(EXTRA_RECIPE_ID, DEFAULT_RECIPE_ID);
//
//                    Log.i(TAG, "mRecipeId: " + mRecipeId);
//
//
//                    Log.i(TAG, "BEFORE CRASH????");
//                    Bundle arguments = new Bundle();
//                    arguments.putInt(RecipeFragment.ARG_RECIPE_ID, mRecipeId);
//                    RecipeFragment fragment = new RecipeFragment();
//                    fragment.setArguments(arguments);
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.master_list_fragment, fragment)
//                            .commit();
//                }
//            } else {
//                Log.i(TAG, "intent == null!!!!!!");
//            }
//
//        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("recipeId", mRecipeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipeId =  savedInstanceState.getInt("recipeId");
    }

}
