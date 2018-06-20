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

    @Query("SELECT * FROM recipe ORDER BY id")
    LiveData<List<RecipeEntry>> loadAllRecipes();

    @Insert
    void insertRecipe(RecipeEntry recipeEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipeEntry recipeEntry);

    @Delete
    void deleteRecipe(RecipeEntry recipeEntry);

    @Query("SELECT * FROM " + AppDatabaseContract.RecipeEntry.TABLE_NAME + " WHERE id = :id")
    LiveData<RecipeEntry> loadRecipeById(int id);

    @Query("SELECT * FROM " + AppDatabaseContract.RecipeEntry.TABLE_NAME + " ORDER BY " + AppDatabaseContract.RecipeEntry.COLUMN_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + AppDatabaseContract.RecipeEntry.TABLE_NAME + " WHERE "  + AppDatabaseContract.RecipeEntry.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("SELECT recipe.*, ingredients.*, steps.* FROM recipe INNER JOIN ingredients ON ingredients.id = recipe.id INNER JOIN steps ON steps.recipeId = recipe.id")
    Cursor selectAllWithChildElements();


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


    // https://github.com/heymonheymon3000/baking/blob/300acde4d3067e02eb30e3d5ff1275781169b1fd/app/src/main/java/baking/nanodegree/android/baking/persistence/dao/RecipeDao.java

    //    @Query("SELECT COUNT(*) FROM " + RecipeContract.RecipeEntry.TABLE_NAME)
//    int count();
//
//    @Insert(onConflict = REPLACE)
//    long insert(Recipe recipe);
//
//    @Insert(onConflict = REPLACE)
//    long[] insertAll(Recipe[] recipes);
//
//    @Query("SELECT * FROM " + RecipeContract.RecipeEntry.TABLE_NAME + " ORDER BY " + RecipeContract.RecipeEntry.COLUMN_NAME)
//    Cursor selectAll();
//
//    @Query("SELECT * FROM " + RecipeContract.RecipeEntry.TABLE_NAME + " ORDER BY " + RecipeContract.RecipeEntry.COLUMN_NAME)
//    LiveData<List<Recipe>> getAll();
//
//    @Query("SELECT " + RecipeContract.RecipeEntry.TABLE_NAME + ".*, " +
//            RecipeContract.IngredientEntry.TABLE_NAME + ".*, " +
//            RecipeContract.StepEntry.TABLE_NAME + ".* " +
//            "FROM " + RecipeContract.RecipeEntry.TABLE_NAME + " " +
//            "INNER JOIN " + RecipeContract.IngredientEntry.TABLE_NAME + " ON " +
//            RecipeContract.IngredientEntry.TABLE_NAME + "." +
//            RecipeContract.IngredientEntry.COLUMN_RECIPE_ID + " = " +
//            RecipeContract.RecipeEntry.TABLE_NAME + "." + RecipeContract.RecipeEntry.COLUMN_ID +
//            " INNER JOIN " + RecipeContract.StepEntry.TABLE_NAME + " ON " +
//            RecipeContract.StepEntry.TABLE_NAME + "." +
//            RecipeContract.StepEntry.COLUMN_RECIPE_ID + " = " +
//            RecipeContract.RecipeEntry.TABLE_NAME + "." + RecipeContract.RecipeEntry.COLUMN_ID)
//    Cursor selectAllWithChildElements();
//
//    @Query("SELECT * FROM " + RecipeContract.RecipeEntry.TABLE_NAME + " WHERE "
//            + RecipeContract.RecipeEntry.COLUMN_ID + " = :id")
//    Cursor selectById(long id);
//
//    @Query("SELECT " + RecipeContract.RecipeEntry.TABLE_NAME + ".*, " +
//            RecipeContract.IngredientEntry.TABLE_NAME + ".*, " +
//            RecipeContract.StepEntry.TABLE_NAME + ".* " +
//            "FROM " + RecipeContract.RecipeEntry.TABLE_NAME + " " +
//            "INNER JOIN " + RecipeContract.IngredientEntry.TABLE_NAME + " ON " +
//            RecipeContract.IngredientEntry.TABLE_NAME + "." +
//            RecipeContract.IngredientEntry.COLUMN_RECIPE_ID + " = " +
//            RecipeContract.RecipeEntry.TABLE_NAME + "." + RecipeContract.RecipeEntry.COLUMN_ID +
//            " INNER JOIN " + RecipeContract.StepEntry.TABLE_NAME + " ON " +
//            RecipeContract.StepEntry.TABLE_NAME + "." +
//            RecipeContract.StepEntry.COLUMN_RECIPE_ID + " = " +
//            RecipeContract.RecipeEntry.TABLE_NAME + "." + RecipeContract.RecipeEntry.COLUMN_ID +
//            " WHERE " + RecipeContract.RecipeEntry.TABLE_NAME + "." +
//            RecipeContract.RecipeEntry.COLUMN_ID + " = :id")
//    Cursor selectByIdWithChildElements(long id);
//
//    @Query("DELETE FROM " + RecipeContract.RecipeEntry.TABLE_NAME + " WHERE "
//            + RecipeContract.RecipeEntry.COLUMN_ID + " = :id")
//    int deleteById(long id);
//
//    @Update(onConflict = REPLACE)
//    int update(Recipe recipe);

}
