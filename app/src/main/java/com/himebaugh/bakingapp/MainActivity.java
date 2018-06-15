package com.himebaugh.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.himebaugh.bakingapp.database.AppDatabase;
import com.himebaugh.bakingapp.database.IngredientDao;
import com.himebaugh.bakingapp.database.IngredientEntry;
import com.himebaugh.bakingapp.database.RecipeDao;
import com.himebaugh.bakingapp.database.RecipeEntry;
import com.himebaugh.bakingapp.database.StepDao;
import com.himebaugh.bakingapp.database.StepEntry;
import com.himebaugh.bakingapp.model.Ingredients;
import com.himebaugh.bakingapp.model.Recipe;
import com.himebaugh.bakingapp.model.Steps;
import com.himebaugh.bakingapp.utils.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = MainActivity.class.getName();

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Load JSON from Internet...
        // URL queryUrl = NetworkUtil.buildUrl(this, "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        
        // new NetworkUtil.InitializeDatabaseTask().execute(queryUrl);

        mDb = AppDatabase.getInstance(getApplicationContext());

        setupViewModel();

//        To add things to the database we need to invoke:
//
//        RepoDatabase
//                .getInstance(context)
//                .getRepoDao()
//                .insert(new Repo(1, "Cool Repo Name", "url"));
//        Getting things is also pretty simple:
//
//        List<Repo> allRepos = RepoDatabase
//                .getInstance(MainActivity.this)
//                .getRepoDao()
//                .getAllRepos();

    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                Log.d(TAG, "Updating list of recipes from LiveData in ViewModel");

                Log.i(TAG, "recipeEntries.size(): " + recipeEntries.size());
                // TODO: Create Adapter
                // mAdapter.setTasks(recipeEntries);
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

//    public class RecipeQueryTask extends AsyncTask<URL, Void, ArrayList<Recipe>> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // mLoadingIndicatorProgressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected ArrayList<Recipe> doInBackground(URL... params) {
//            URL url = params[0];
//
//            ArrayList<Recipe> recipeList = null;
//
//            try {
//                recipeList = NetworkUtil.getRecipeList(getApplicationContext(), url);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return recipeList;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Recipe> recipeList) {
//
//            RecipeDao recipeDao = AppDatabase.getInstance(getApplicationContext()).recipeDao();
//            StepDao stepDao = AppDatabase.getInstance(getApplicationContext()).stepDao();
//            IngredientDao ingredientDao = AppDatabase.getInstance(getApplicationContext()).ingredientDao();
//
//
//            Log.i(TAG, "recipeList.toString(): " + recipeList.toString());
//
//            Log.i(TAG, "recipeList.size()" + recipeList.size());
//
//            for (Recipe recipe : recipeList) {
//
//                recipeDao.insertRecipe(new RecipeEntry(recipe.getName(), recipe.getServings(), recipe.getImage()));
//
//                Log.i(TAG, "listIterator: " + recipe.getName() + " " + recipe.getImage());
//
//                // ArrayList<Ingredients> ingredients, ArrayList<Steps> steps
//
//                for (Ingredients ingredients : recipe.getIngredients()) {
//
//                    ingredientDao.insertIngredient(new IngredientEntry(
//                            ingredients.getQuantity(),
//                            ingredients.getMeasure(),
//                            ingredients.getIngredient(),
//                            recipe.getId()));
//
//                    Log.i(TAG, "listIterator: " + ingredients.getIngredient() + " " + ingredients.getMeasure());
//                }
//
//                for (Steps steps : recipe.getSteps()) {
//
//                    stepDao.insertStep(new StepEntry(
//                            steps.getId(),
//                            steps.getShortDescription(),
//                            steps.getDescription(),
//                            steps.getVideoURL(),
//                            steps.getThumbnailURL(),
//                            recipe.getId()));
//
//                    Log.i(TAG, "listIterator: " + steps.getDescription() + " " + steps.getVideoURL());
//                }
//
//            }
//
//        }
//
//
//    }


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
}
