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

    // NOTE TO SELF:
    // USE ContentResolver  NOT ContentProvider
    // Don't close the database ... db.close();

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
        //appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, AppDatabase.DATABASE_NAME).build();
        mAppDatabase = AppDatabase.getInstance(getContext());

        // Gets a Data Access Object to perform the database operations
        mRecipeDao = mAppDatabase.getRecipeDao();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Get access to underlying database (read-only for query)
        // final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor returnCursor = null;
        String id;

        switch (match) {

            // Query for the recipes directory
            case RECIPES:
                // returnCursor = mRecipeDao.selectAll(AppDatabaseContract.RecipeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                returnCursor = mRecipeDao.selectAll();
                break;

            // Query for a single recipe
            case RECIPE_WITH_ID:

                // Get the recipe ID from the URI path
                id = uri.getPathSegments().get(1);

                // Use selections/selectionArgs to filter for this ID
                returnCursor = mRecipeDao.selectById(ContentUris.parseId(uri));

                break;
        }
//        Cursor result = null;
//
//        int match = uriMatcher.match(uri);
//        switch (match) {
//            case LISTS:
//
//                result = mAppDatabase.getRecipeDao().selectAllWithChildElements()
//
//
//                db.query(
//                        RecipeContract.RecipeEntry.TABLE_RECIPES_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//
//                result.setNotificationUri(getContext().getContentResolver(), uri);
//                break;
//
//            case LIST_WITH_ID:
//                String id = uri.getPathSegments().get(1);
//                String mSelection = RecipeContract.RecipeEntry._ID + "=?";
//                String[] mSelectionArgs = new String[]{id};
//
//                result = db.query(
//                        RecipeContract.RecipeEntry.TABLE_RECIPES_NAME,
//                        projection,
//                        mSelection,
//                        mSelectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//
//                break;
//            default:
//                Log.w(TAG, "Unknown URI: " + uri);
//        }
//
//        final int code = URI_MATCHER.match(uri);
//
//        switch( code ) {
//            case HOMEWORK:
//            case HOMEWORK_ID:
//                final Context context = getContext();
//                if (context == null) {
//                    return null;
//                }
//
//                HomeworkDao homeworkDao = AppDatabase.getInstance(context).homeworkDao();
//                final Cursor cursor;
//
//                if (code == HOMEWORK) {
//                    cursor = homeworkDao.selectAll();
//                } else {
//                    cursor = homeworkDao.selectByUid(ContentUris.parseId(uri));
//                }
//                cursor.setNotificationUri(context.getContentResolver(), uri);
//                return cursor;
//
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//
//        }
//
//        final int code = MATCHER.match(uri);
//
//        context = HomepageFragment.context;
//        if (code == CODE_DIR || code == CODE_ITEM) {
//            if (context == null) {
//                return null;
//            }
//            ExpenseDao expenseDao = AppDatabase.getInstance(context).expenseDao();
//            final Cursor cursor;
//            if (code == CODE_DIR) {
//                cursor = expenseDao.selectAll();
//            } else {
//                cursor = expenseDao.selectById(ContentUris.parseId(uri));
//            }
//            cursor.setNotificationUri(context.getContentResolver(), uri);
//            return cursor;
//        } else {
//            throw new IllegalArgumentException("Unknown URI: " + uri);
//        }

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

//    // Implements the provider's insert method
//    public Cursor insert(Uri uri, ContentValues values) {
//        // Insert code here to determine which DAO to use when inserting data, handle error conditions, etc.
//    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
