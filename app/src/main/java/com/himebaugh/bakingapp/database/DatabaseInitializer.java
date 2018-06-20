package com.himebaugh.bakingapp.database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

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

    public static void populateSync(URL url, @NonNull final AppDatabase db) {
        populateWithTestData(url, db);
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
            populateWithTestData(mUrl, mDb);
            return null;
        }

    }

    private static void populateWithTestData(URL url, AppDatabase appDatabase) {

        RecipeDao recipeDao = appDatabase.getRecipeDao();
        StepDao stepDao = appDatabase.getStepDao();
        IngredientDao ingredientDao = appDatabase.getIngredientDao();

        //URL url = NetworkUtil.buildUrl(context, "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");

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


//        db.loanModel().deleteAll();
//        db.userModel().deleteAll();
//        db.bookModel().deleteAll();
//
//        User user1 = addUser(db, "1", "Jason", "Seaver", 40);
//        User user2 = addUser(db, "2", "Mike", "Seaver", 12);
//        addUser(db, "3", "Carol", "Seaver", 15);
//
//        Book book1 = addBook(db, "1", "Dune");
//        Book book2 = addBook(db, "2", "1984");
//        Book book3 = addBook(db, "3", "The War of the Worlds");
//        Book book4 = addBook(db, "4", "Brave New World");
//        addBook(db, "5", "Foundation");
//        try {
//            // Loans are added with a delay, to have time for the UI to react to changes.
//
//            Date today = getTodayPlusDays(0);
//            Date yesterday = getTodayPlusDays(-1);
//            Date twoDaysAgo = getTodayPlusDays(-2);
//            Date lastWeek = getTodayPlusDays(-7);
//            Date twoWeeksAgo = getTodayPlusDays(-14);
//
//            addLoan(db, "1", user1, book1, twoWeeksAgo, lastWeek);
//            Thread.sleep(DELAY_MILLIS);
//            addLoan(db, "2", user2, book1, lastWeek, yesterday);
//            Thread.sleep(DELAY_MILLIS);
//            addLoan(db, "3", user2, book2, lastWeek, today);
//            Thread.sleep(DELAY_MILLIS);
//            addLoan(db, "4", user2, book3, lastWeek, twoDaysAgo);
//            Thread.sleep(DELAY_MILLIS);
//            addLoan(db, "5", user2, book4, lastWeek, today);
//            Log.d("DB", "Added loans");
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

}
