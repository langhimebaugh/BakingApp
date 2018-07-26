package com.himebaugh.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM " + AppDatabaseContract.IngredientEntry.TABLE_NAME + " WHERE " + AppDatabaseContract.IngredientEntry.COLUMN_RECIPE_ID + " = :recipeId ORDER BY " + AppDatabaseContract.IngredientEntry._ID)
    LiveData<List<IngredientEntry>> loadIngredientsByRecipeId(int recipeId);

    @Query("SELECT * FROM " + AppDatabaseContract.IngredientEntry.TABLE_NAME + " WHERE " + AppDatabaseContract.IngredientEntry.COLUMN_RECIPE_ID + " = :recipeId ORDER BY " + AppDatabaseContract.IngredientEntry._ID)
    List<IngredientEntry> loadIngredientListByRecipeId(int recipeId);

    @Insert
    void insertIngredient(IngredientEntry ingredientEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateIngredient(IngredientEntry ingredientEntry);

    @Delete
    void deleteIngredient(IngredientEntry ingredientEntry);

    @Query("SELECT * FROM " + AppDatabaseContract.IngredientEntry.TABLE_NAME + " WHERE " + AppDatabaseContract.IngredientEntry._ID + " = :id")
    LiveData<IngredientEntry> loadIngredientById(int id);

}

