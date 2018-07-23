package com.himebaugh.bakingapp.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.database.Cursor;
import android.util.Log;


import java.util.List;

public class DataViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = DataViewModel.class.getSimpleName();

    private AppDatabase mDatabase;
    private LiveData<List<RecipeEntry>> recipes;
    private LiveData<List<IngredientEntry>> ingredients;

    public DataViewModel(Application application) {
        super(application);
        mDatabase = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the recipes from the DataBase");


        // recipes = mDatabase.recipeDao().loadAllRecipes();
        // Log.i(TAG, "MainViewModel: recipes.getValue().size()=" + recipes.getValue().size());
        // Log.i(TAG, "MainViewModel: getRecipes().getValue().size()=" + getRecipes().getValue().size());
    }

    public LiveData<List<RecipeEntry>> getRecipes() {

        return mDatabase.getRecipeDao().loadAllRecipes();
    }

    public Cursor getCursorOfRecipes() {

        return mDatabase.getRecipeDao().selectAll();
    }

    public LiveData<RecipeEntry> getRecipe(int recipeId) {

        return mDatabase.getRecipeDao().loadRecipeById(recipeId);
    }

    public LiveData<List<IngredientEntry>> getIngredients(int recipeId) {

        return mDatabase.getIngredientDao().loadIngredientsByRecipeId(recipeId);
    }

    public LiveData<List<StepEntry>> getSteps(int recipeId) {
        
        return mDatabase.getStepDao().loadStepsByRecipeId(recipeId);
    }

    public LiveData<StepEntry> getStep(int recipeId, int stepNumber) {

        return mDatabase.getStepDao().loadStepsByStepNumber(recipeId,stepNumber);
    }

}
