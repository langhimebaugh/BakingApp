package com.himebaugh.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM " + AppDatabaseContract.RecipeEntry.TABLE_NAME + " ORDER BY "  + AppDatabaseContract.RecipeEntry._ID )
    LiveData<List<RecipeEntry>> loadAllRecipes();

    @Query("SELECT * FROM " + AppDatabaseContract.RecipeEntry.TABLE_NAME + " ORDER BY "  + AppDatabaseContract.RecipeEntry._ID )
    List<RecipeEntry> loadRecipeList();

    @Insert
    void insertRecipe(RecipeEntry recipeEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipeEntry recipeEntry);

    @Delete
    void deleteRecipe(RecipeEntry recipeEntry);

    @Query("SELECT * FROM " + AppDatabaseContract.RecipeEntry.TABLE_NAME + " WHERE "  + AppDatabaseContract.RecipeEntry._ID + " = :id")
    LiveData<RecipeEntry> loadRecipeById(int id);

    @Query("SELECT * FROM " + AppDatabaseContract.RecipeEntry.TABLE_NAME + " ORDER BY " + AppDatabaseContract.RecipeEntry.COLUMN_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + AppDatabaseContract.RecipeEntry.TABLE_NAME + " WHERE "  + AppDatabaseContract.RecipeEntry._ID + " = :id")
    Cursor selectById(int id);

}
