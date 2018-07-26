package com.himebaugh.bakingapp.database;

import android.content.Context;
import android.os.AsyncTask;

import com.himebaugh.bakingapp.model.Ingredients;
import com.himebaugh.bakingapp.model.Recipe;
import com.himebaugh.bakingapp.model.Steps;
import com.himebaugh.bakingapp.utils.NetworkUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DatabaseInitializer {

    // See... Android Persistence codelab
    // https://codelabs.developers.google.com/codelabs/android-persistence/#0
    // https://github.com/googlecodelabs/android-persistence

    public static void populateAsync(URL url, final AppDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(url, db);
        task.execute();
    }

    private static class PopulateDbAsync extends AsyncTask<Context, Void, Void> {

        private final URL mUrl;
        private final AppDatabase mDb;

        PopulateDbAsync(URL url, AppDatabase db) {
            mUrl = url;
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Context... params) {
            populateWithData(mUrl, mDb);
            return null;
        }

    }

    private static void populateWithData(URL url, AppDatabase appDatabase) {

        RecipeDao recipeDao = appDatabase.getRecipeDao();
        StepDao stepDao = appDatabase.getStepDao();
        IngredientDao ingredientDao = appDatabase.getIngredientDao();

        ArrayList<Recipe> recipeList = null;

        try {
            recipeList = NetworkUtil.getRecipeList(url);

            for (Recipe recipe : recipeList) {

                recipeDao.insertRecipe(new RecipeEntry(recipe.getName(), recipe.getServings(), recipe.getImage()));

                for (Ingredients ingredients : recipe.getIngredients()) {

                    ingredientDao.insertIngredient(new IngredientEntry(
                            ingredients.getQuantity(),
                            ingredients.getMeasure(),
                            ingredients.getIngredient(),
                            recipe.getId()));

                }

                for (Steps steps : recipe.getSteps()) {

                    stepDao.insertStep(new StepEntry(
                            steps.getId(),
                            steps.getShortDescription(),
                            steps.getDescription(),
                            steps.getVideoURL(),
                            steps.getThumbnailURL(),
                            recipe.getId()));

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
