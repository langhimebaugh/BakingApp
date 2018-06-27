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

    // ========================
    // https://android.jlelse.eu/android-architecture-components-room-introduction-4774dd72a1ae
    // ========================
    // @Insert
    // void insert(Repo... repos);

    // @Insert
    // void insert(Repo repo);

    // @Insert
    // void insert(List<Repo> repoList);

    // @Query("SELECT * FROM repo")
    // List<Repo> getAllRepos();

    // @Query("SELECT * FROM repo WHERE id=:id")
    // Repo getRepo(int id);

    // @Query("SELECT * FROM repo")
    // Cursor getRepoCursor();

    // @Query("SELECT * FROM repo WHERE name=:name")
    // List<Repo> getReposByName(String name);

    // @Query("SELECT * FROM repo WHERE name=:name LIMIT :max")
    // List<Repo> getReposByName(int max, String... name);

}

