package com.himebaugh.bakingapp.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtil {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static boolean isWifiConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Builds the URL used to query Videos Endpoint.
     *
     * @param stringUrl String to convert to URL.
     * @return The URL to return.
     */
    public static URL buildUrl(Context context, String stringUrl) {

        Uri builtUri = Uri.parse(stringUrl).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     */
    public static String getJsonFromHttpUrl(URL url) {

        String jsonString = null;

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                jsonString = scanner.next();
            }

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static ArrayList<Recipe> getRecipeList(URL url) throws IOException {

        ArrayList<Recipe> returnRecipeList;

        // **********************************************
        // filter is passed in when clicking options menu in MainActivity
        //
        // filter may contain either of:
        //  1) RecipeUtils.FILTER_POPULAR
        //
        //      Build a URL using RecipeUtils.POPULAR_MOVIES
        //      Attempt to get RecipeList through internet via API through getJsonFromHttpUrl(url)
        //      IF SUCCESSFUL, process & saveRecipeListToCursor() [REMOVE OLD MOVIES & REFRESH WITH NEW ONES]
        //      IF NOT SUCCESSFUL assume the internet is down and get backup data from the RecipeProvider using getRecipeListFromCursor()
        //
        //  2) RecipeUtils.FILTER_TOPRATED
        //
        //      Build a URL using RecipeUtils.TOPRATED_MOVIES
        //      Attempt to get RecipeList through internet via API through getJsonFromOkHttpClient(url)
        //      IF SUCCESSFUL, process & saveRecipeListToCursor() [REMOVE OLD MOVIES & REFRESH WITH NEW ONES]
        //      IF NOT SUCCESSFUL assume the internet is down and get backup data from the RecipeProvider using getRecipeListFromCursor()
        //
        //  3) RecipeUtils.FILTER_FAVORITE
        //
        //      Get RecipeList from database via the RecipeProvider using getRecipeListFromCursor()
        //
        // **********************************************

        // Can use either one... getJsonFromHttpUrl() -OR- getJsonFromOkHttpClient()
        String recipeJsonResults = getJsonFromHttpUrl(url);
        // String popularRecipesJsonResults = getJsonFromOkHttpClient(queryUrl);  // This Works Fine

        if (recipeJsonResults == null) {

            // ***********************************************************
            // INTERNET IS DOWN, so get RecipeList from database
            // ***********************************************************
            // returnRecipeList = getRecipeListFromCursor(context, filter);
            returnRecipeList = null;

        } else {

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Recipe>>() {
            }.getType();
            returnRecipeList = gson.fromJson(recipeJsonResults, listType);

            // ***********************************************************
            // The latest RecipeList results are saved to the database
            // Also, RecipeUtils.FILTER_POPULAR is saved to COLUMN_CATEGORY
            // ***********************************************************
            // saveRecipeListToCursor(context, returnRecipeList, RecipeUtils.FILTER_POPULAR);
        }

        return returnRecipeList;

    }

    public static class InitializeDatabaseTask extends AsyncTask<URL, Void, ArrayList<Recipe>> {

        private final RecipeDao recipeDao;
        private Context context;

        public InitializeDatabaseTask(Context context, AppDatabase appDatabase) {
            this.context = context;
            this.recipeDao = appDatabase.recipeDao();
        }

        @Override
        protected ArrayList<Recipe> doInBackground(URL... params) {
            URL url = params[0];

            ArrayList<Recipe> recipeList = null;

            try {
                recipeList = NetworkUtil.getRecipeList(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return recipeList;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipeList) {

            RecipeDao recipeDao = AppDatabase.getInstance(context).recipeDao();
            StepDao stepDao = AppDatabase.getInstance(context).stepDao();
            IngredientDao ingredientDao = AppDatabase.getInstance(context).ingredientDao();


            Log.i(LOG_TAG, "recipeList.toString(): " + recipeList.toString());

            Log.i(LOG_TAG, "recipeList.size()" + recipeList.size());

            for (Recipe recipe : recipeList) {

                recipeDao.insertRecipe(new RecipeEntry(recipe.getName(), recipe.getServings(), recipe.getImage()));

                Log.i(LOG_TAG, "listIterator: " + recipe.getName() + " " + recipe.getImage());

                // ArrayList<Ingredients> ingredients, ArrayList<Steps> steps

                for (Ingredients ingredients : recipe.getIngredients()) {

                    ingredientDao.insertIngredient(new IngredientEntry(
                            ingredients.getQuantity(),
                            ingredients.getMeasure(),
                            ingredients.getIngredient(),
                            recipe.getId()));

                    Log.i(LOG_TAG, "listIterator: " + ingredients.getIngredient() + " " + ingredients.getMeasure());
                }

                for (Steps steps : recipe.getSteps()) {

                    stepDao.insertStep(new StepEntry(
                            steps.getId(),
                            steps.getShortDescription(),
                            steps.getDescription(),
                            steps.getVideoURL(),
                            steps.getThumbnailURL(),
                            recipe.getId()));

                    Log.i(LOG_TAG, "listIterator: " + steps.getDescription() + " " + steps.getVideoURL());
                }

            }

        }


    }


}