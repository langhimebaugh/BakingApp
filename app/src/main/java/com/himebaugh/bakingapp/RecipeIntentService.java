package com.himebaugh.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.himebaugh.bakingapp.database.AppDatabaseContract;

import static com.himebaugh.bakingapp.database.AppDatabaseContract.BASE_CONTENT_URI;
import static com.himebaugh.bakingapp.database.AppDatabaseContract.INVALID_RECIPE_ID;
import static com.himebaugh.bakingapp.database.AppDatabaseContract.PATH_RECIPES;

// An {@link IntentService} subclass for handling asynchronous task requests in a service on a separate handler thread.

public class RecipeIntentService extends IntentService {


    private static final String ACTION_UPDATE_RECIPE_WIDGET = "com.himebaugh.bakingapp.action.UPDATE_RECIPE_WIDGET";

    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGET.equals(action)) {
                handleActionUpdateRecipeWidgets();
            }
        }
    }

    private void handleActionUpdateRecipeWidgets() {

        // TODO: Handle action Update Recipe Widgets

        // ==============================================================
        // URI to get all the recipes (ordered by name)
        // >> AppDatabaseContract.RecipeEntry.CONTENT_URI

        // >> AppDatabaseContract.RecipeEntry.recipeContentUriWithId(id)  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // ==============================================================

        // Uri details = Uri.withAppendedPath(EmployeeProvider.CONTENT_URI, "" + empID);

        // No need to pass args as I'm not currently doing anything with them in the AppDatabaseProvider
        Cursor cursor = getContentResolver().query(AppDatabaseContract.RecipeEntry.CONTENT_URI,null,null,null,null);

        // Extract the recipe details
        String recipeName = "Fish Soup"; // Default in case our recipe is empty
        long recipeId = INVALID_RECIPE_ID;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int idIndex = cursor.getColumnIndex(AppDatabaseContract.RecipeEntry._ID);
            int recipeNameIndex = cursor.getColumnIndex(AppDatabaseContract.RecipeEntry.COLUMN_NAME);
            recipeId = cursor.getLong(idIndex);
            recipeName = cursor.getString(recipeNameIndex);
            cursor.close();
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        //Trigger data update to handle the widgets and force a data refresh
        // recipe_widget  ??
        // appwidget_text ??
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget);

        //Now update all widgets
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipeId, recipeName, appWidgetIds);
        //PlantWidgetProvider.updatePlantWidgets(this, appWidgetManager, imgRes,plantId ,canWater,appWidgetIds);
    }

}
