package com.himebaugh.bakingapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;


import com.himebaugh.bakingapp.database.AppDatabase;
import com.himebaugh.bakingapp.database.RecipeEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<RecipeEntry>> recipes;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the recipes from the DataBase");
        recipes = database.recipeDao().loadAllRecipes();
    }

    public LiveData<List<RecipeEntry>> getTasks() {
        return recipes;
    }
}
