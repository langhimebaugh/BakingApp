package com.himebaugh.bakingapp.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class AppDatabaseContract {

    // Name for the entire content provider
    public static final String CONTENT_AUTHORITY = "com.himebaugh.bakingapp";

    // Base URI to contact the content provider for PopularMovies
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Paths for accessing data in this contract
    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_INGREDIENTS = "ingredients";
    public static final String PATH_STEPS = "steps";

    public static final long INVALID_RECIPE_ID = -1;

    // Inner class that defines the table contents of the recipe table
    public static final class RecipeEntry implements BaseColumns {

        // The base CONTENT_URI used to query the Recipe table from the content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        // Table & column names
        public static final String TABLE_NAME = "recipe";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";
        // public static final String COLUMN_FAVORITE = "favorite";            // Added field to save favorite

        public static Uri recipeContentUriWithId(int id){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
        }

    }

    // Inner class that defines the table contents of the ingredients table
    public static final class IngredientEntry implements BaseColumns {

        // The base CONTENT_URI used to query the Ingredients table from the content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        // Table & column names
        public static final String TABLE_NAME = "ingredients";

        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_RECIPE_ID = "recipeId";

        public static Uri ingredientContentUriWithId(int id){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
            // ??
            // return CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).appendPath(PATH_INGREDIENTS).build();
        }

    }

    // Inner class that defines the table contents of the Steps table
    public static final class StepEntry implements BaseColumns {

        // The base CONTENT_URI used to query the Steps table from the content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        // Table & column names
        public static final String TABLE_NAME = "steps";

        public static final String COLUMN_STEP_NUMBER = "stepNumber";
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "videoURL";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnailURL";
        public static final String COLUMN_RECIPE_ID = "recipeId";

        public static Uri stepContentUriWithId(int id){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
            // ?????
            // return CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).appendPath(PATH_STEPS).build();
        }

    }

}
