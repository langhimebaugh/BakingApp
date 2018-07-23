package com.himebaugh.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.database.AppDatabaseContract;
import com.himebaugh.bakingapp.database.DataViewModel;
import com.himebaugh.bakingapp.database.RecipeEntry;

import java.util.List;

import static com.himebaugh.bakingapp.database.AppDatabaseContract.INVALID_RECIPE_ID;

// An {@link IntentService} subclass for handling asynchronous task requests in a service on a separate handler thread.

public class RecipeWidgetIntentService extends IntentService {

    private final static String TAG = RecipeWidgetIntentService.class.getName();
    private static final String ACTION_UPDATE_RECIPE_WIDGET = "com.himebaugh.bakingapp.action.UPDATE_RECIPE_WIDGET";

    public RecipeWidgetIntentService() {
        super("RecipeIntentService");
    }

    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
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

        // Can't figure out how to use ViewModel here...

//        DataViewModel viewModel = ViewModelProviders.of(this).get(DataViewModel.class);
//
//        viewModel.getCursorOfRecipes();
//
//        viewModel.getRecipes().observe(this, new Observer<List<RecipeEntry>>() {
//            @Override
//            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
//
//            }
//        });

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
        // long recipeId = INVALID_RECIPE_ID;
        int recipeId = INVALID_RECIPE_ID;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int idIndex = cursor.getColumnIndex(AppDatabaseContract.RecipeEntry._ID);
            int recipeNameIndex = cursor.getColumnIndex(AppDatabaseContract.RecipeEntry.COLUMN_NAME);
            // recipeId = cursor.getLong(idIndex);
            recipeId = cursor.getInt(idIndex);
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
