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
public interface StepDao {

    @Query("SELECT * FROM " + AppDatabaseContract.StepEntry.TABLE_NAME + " WHERE " + AppDatabaseContract.StepEntry.COLUMN_RECIPE_ID + " = :recipeId ORDER BY " + AppDatabaseContract.StepEntry._ID)
    LiveData<List<StepEntry>> loadStepsByRecipeId(int recipeId);

    @Insert
    void insertStep(StepEntry stepEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStep(StepEntry stepEntry);

    @Delete
    void deleteStep(StepEntry stepEntry);

    @Query("SELECT * FROM " + AppDatabaseContract.StepEntry.TABLE_NAME + " WHERE " + AppDatabaseContract.StepEntry._ID + " = :id")
    LiveData<StepEntry> loadStepById(int id);

    @Query("SELECT * FROM " + AppDatabaseContract.StepEntry.TABLE_NAME + " WHERE " + AppDatabaseContract.StepEntry.COLUMN_RECIPE_ID + " = :recipeId AND " + AppDatabaseContract.StepEntry.COLUMN_STEP_NUMBER + " = :stepNumber ")
    LiveData<StepEntry> loadStepsByStepNumber(int recipeId, int stepNumber);

}

