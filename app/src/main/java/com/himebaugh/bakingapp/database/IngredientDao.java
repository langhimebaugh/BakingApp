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

//    @Query("SELECT * FROM Book " +
//            "INNER JOIN Loan ON Loan.book_id = Book.id " +
//            "INNER JOIN User on User.id = Loan.user_id " +
//            "WHERE User.name LIKE :userName"
//    )
//    LiveData<List<Book>> findBooksBorrowedByName(String userName);

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

