package com.himebaugh.bakingapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

// https://developer.android.com/guide/topics/providers/content-provider-creating
// The following two snippets demonstrate the interaction between ContentProvider.onCreate() and Room.databaseBuilder().
// This snippet shows the implementation of ContentProvider.onCreate() where the database object is built and handles to the data access objects are created:

public class AppDatabaseProvider extends ContentProvider {

    private static final String TAG = AppDatabaseProvider.class.getSimpleName();

    public static final int RECIPES = 100;
    public static final int RECIPE_WITH_ID = 101;
    public static final int INGREDIENTS = 200;
    public static final int INGREDIENT_WITH_ID = 201;
    public static final int STEPS = 300;
    public static final int STEP_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Initialize a new matcher object without any matches,
     * then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the movie directory and a single movie by ID.
         */
        uriMatcher.addURI(AppDatabaseContract.CONTENT_AUTHORITY, AppDatabaseContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(AppDatabaseContract.CONTENT_AUTHORITY, AppDatabaseContract.PATH_RECIPES + "/#", RECIPE_WITH_ID);
        uriMatcher.addURI(AppDatabaseContract.CONTENT_AUTHORITY, AppDatabaseContract.PATH_INGREDIENTS, INGREDIENTS);
        uriMatcher.addURI(AppDatabaseContract.CONTENT_AUTHORITY, AppDatabaseContract.PATH_INGREDIENTS + "/#", INGREDIENT_WITH_ID);
        uriMatcher.addURI(AppDatabaseContract.CONTENT_AUTHORITY, AppDatabaseContract.PATH_STEPS, STEPS);
        uriMatcher.addURI(AppDatabaseContract.CONTENT_AUTHORITY, AppDatabaseContract.PATH_STEPS + "/#", STEP_WITH_ID);

        return uriMatcher;
    }

    // Defines a handle to the Room database
    private AppDatabase mAppDatabase;

    // Defines a Data Access Object to perform the database operations
    private RecipeDao mRecipeDao;

    @Override
    public boolean onCreate() {

        // Creates a new database object.
        mAppDatabase = AppDatabase.getInstance(getContext());

        // Gets a Data Access Object to perform the database operations
        mRecipeDao = mAppDatabase.getRecipeDao();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int match = sUriMatcher.match(uri);
        Cursor returnCursor = null;
        String id;

        switch (match) {

            // Query for the recipes directory
            case RECIPES:

                returnCursor = mRecipeDao.selectAll();
                break;

            // Query for a single recipe
            case RECIPE_WITH_ID:

                // Get the recipe ID from the URI path
                id = uri.getPathSegments().get(1);

                // Use selections/selectionArgs to filter for this ID
                returnCursor = mRecipeDao.selectById( (int) ContentUris.parseId(uri));

                break;
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
