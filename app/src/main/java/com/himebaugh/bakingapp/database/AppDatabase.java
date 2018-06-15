package com.himebaugh.bakingapp.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.himebaugh.bakingapp.model.Ingredients;
import com.himebaugh.bakingapp.model.Recipe;
import com.himebaugh.bakingapp.model.Steps;
import com.himebaugh.bakingapp.utils.NetworkUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

@Database(entities = {RecipeEntry.class}, version = 1, exportSchema = false)
//@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "baked1";    // .db ??
    private static AppDatabase sInstance;                   // volatile ??

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)

                        // To Populate database
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);

                                // sInstance.recipeDao().insertRecipe();
                                // sInstance.preFillData(context);

                                // ContentValues cv = new ContentValues();
                                // cv.put("name", "saladillo");
                                // db.insert("business", 0, cv);

                                // https://github.com/lct8712/RoomWordSample/tree/756ec8dd8549b327256ad36283ccb5378485c23b
                                // new InitAsyncTask(sInstance).execute();
                                // Log.d(LOG_TAG, "db init");

                                new InitDatabaseTask(sInstance).execute();
                                Log.d(LOG_TAG, "db init");
                            }
                        })

                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();
    public abstract StepDao stepDao();

//
//    private static class InitAsyncTask extends AsyncTask<Recipe, Void, Void> {
//
//        private final RecipeDao recipeDao;
//
//        private InitAsyncTask(AppDatabase appDatabase) {
//            this.recipeDao = appDatabase.recipeDao();
//        }
//
//        @Override
//        protected Void doInBackground(Recipe... recipes) {
//            recipeDao.deleteAll();
//            recipeDao.insert(new Recipe("apple"));
//            recipeDao.insert(new Recipe("orange"));
//            recipeDao.insert(new Repo(1, "Cool Repo Name", "url"));
//            return null;
//        }
//    }

//
    private static class InitDatabaseTask extends AsyncTask<Context, Context, ArrayList<Recipe>> {

        private final RecipeDao recipeDao;
        private final StepDao stepDao;
        private final IngredientDao ingredientDao;

        private InitDatabaseTask(AppDatabase appDatabase) {
            recipeDao = appDatabase.recipeDao();
            stepDao = appDatabase.stepDao();
            ingredientDao = appDatabase.ingredientDao();
        }


        @Override
        protected ArrayList<Recipe> doInBackground(Context... contexts) {

            URL url = NetworkUtil.buildUrl(contexts[0], "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");

            ArrayList<Recipe> recipeList = null;

            try {
                recipeList = NetworkUtil.getRecipeList(contexts[0], url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return recipeList;
        }

    @Override
        protected void onPostExecute(ArrayList<Recipe> recipeList) {

//            RecipeDao recipeDao = this.recipeDao;
//            StepDao stepDao = AppDatabase.getInstance(getApplicationContext()).stepDao();
//            IngredientDao ingredientDao = AppDatabase.getInstance(getApplicationContext()).ingredientDao();


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
