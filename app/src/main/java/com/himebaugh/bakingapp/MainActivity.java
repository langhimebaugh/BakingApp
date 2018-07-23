package com.himebaugh.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.himebaugh.bakingapp.adapter.RecipeCardAdapter;
import com.himebaugh.bakingapp.database.AppDatabase;
import com.himebaugh.bakingapp.database.DataViewModel;
import com.himebaugh.bakingapp.database.IngredientEntry;
import com.himebaugh.bakingapp.database.RecipeEntry;
import com.himebaugh.bakingapp.database.StepEntry;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecipeCardAdapter.RecipeCardAdapterOnClickHandler {

    private final static String TAG = MainActivity.class.getName();
    private static final String RECIPE_KEY = "recipe";
    private static final String BUNDLE = "bundle";

    private AppDatabase mDb;
    RecyclerView mRecyclerView;
    private RecipeCardAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // CHANGE GRID SpanCount ON ROTATION
        GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 1);
        } else {
            layoutManager = new GridLayoutManager(this, 3);
        }

        // May save position and reset upon orientation change???
        // layoutManager.scrollToPosition(0);

        mRecyclerView.setLayoutManager(layoutManager);
        // allows for optimizations if all items are of the same size:
        mRecyclerView.setHasFixedSize(true);

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new RecipeCardAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        //

        mDb = AppDatabase.getInstance(getApplicationContext());

        setupViewModel();

    }

    private void setupViewModel() {

        DataViewModel viewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        viewModel.getRecipes().observe(this, new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                Log.d(TAG, "Updating list of recipes from LiveData in ViewModel");

                Log.i(TAG, "recipeEntries.size(): " + recipeEntries.size());
                // TODO: Create Adapter
                mAdapter.loadRecipes(recipeEntries);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(@NonNull RecipeEntry recipe) {

        // Launch RecipeStepListActivity adding the recipeId as an extra in the intent
        Class destinationActivity = RecipeStepListActivity.class;  // RecipeActivity.class; RecipeStepListActivity.class;
        Intent intent = new Intent(this, destinationActivity);
        intent.putExtra(RecipeStepListActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
