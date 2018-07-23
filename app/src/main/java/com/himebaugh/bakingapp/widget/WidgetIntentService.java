package com.himebaugh.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;

import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.database.AppDatabaseContract;

import static com.himebaugh.bakingapp.database.AppDatabaseContract.INVALID_RECIPE_ID;

// An {@link IntentService} subclass for handling asynchronous task requests in a service on a separate handler thread.

public class WidgetIntentService extends IntentService {

    private final static String TAG = WidgetIntentService.class.getName();
    private static final String ACTION_UPDATE_RECIPE_WIDGET = "com.himebaugh.bakingapp.action.UPDATE_RECIPE_WIDGET";

    public WidgetIntentService() {
        super("RecipeIntentService");
    }

    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, WidgetIntentService.class);
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
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));

        //Trigger data update to handle the widgets and force a data refresh
        // recipe_widget  ??
        // appwidget_text ??
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget);

        //Now update all widgets
        WidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipeId, recipeName, appWidgetIds);
        //PlantWidgetProvider.updatePlantWidgets(this, appWidgetManager, imgRes,plantId ,canWater,appWidgetIds);
    }



    //     --------- beginning of crash
    //07-22 22:03:04.525 8705-8705/com.himebaugh.bakingapp E/AndroidRuntime: FATAL EXCEPTION: main
    //    Process: com.himebaugh.bakingapp, PID: 8705
    //    java.lang.RuntimeException: Unable to start receiver com.himebaugh.bakingapp.widget.WidgetProvider: java.lang.IllegalStateException: Not allowed to start service Intent { act=com.himebaugh.bakingapp.action.UPDATE_RECIPE_WIDGET cmp=com.himebaugh.bakingapp/.widget.WidgetIntentService }: app is in background uid UidRecord{b96a783 u0a78 RCVR idle procs:1 seq(0,0,0)}
    //        at android.app.ActivityThread.handleReceiver(ActivityThread.java:3259)
    //        at android.app.ActivityThread.-wrap17(Unknown Source:0)
    //        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1677)
    //        at android.os.Handler.dispatchMessage(Handler.java:105)
    //        at android.os.Looper.loop(Looper.java:164)
    //        at android.app.ActivityThread.main(ActivityThread.java:6541)
    //        at java.lang.reflect.Method.invoke(Native Method)
    //        at com.android.internal.os.Zygote$MethodAndArgsCaller.run(Zygote.java:240)
    //        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:767)
    //     Caused by: java.lang.IllegalStateException: Not allowed to start service Intent { act=com.himebaugh.bakingapp.action.UPDATE_RECIPE_WIDGET cmp=com.himebaugh.bakingapp/.widget.WidgetIntentService }: app is in background uid UidRecord{b96a783 u0a78 RCVR idle procs:1 seq(0,0,0)}
    //        at android.app.ContextImpl.startServiceCommon(ContextImpl.java:1505)
    //        at android.app.ContextImpl.startService(ContextImpl.java:1461)
    //        at android.content.ContextWrapper.startService(ContextWrapper.java:644)
    //        at android.content.ContextWrapper.startService(ContextWrapper.java:644)
    //        at com.himebaugh.bakingapp.widget.WidgetIntentService.startActionUpdateRecipeWidgets(WidgetIntentService.java:29)
    //        at com.himebaugh.bakingapp.widget.WidgetProvider.onUpdate(WidgetProvider.java:42)


}
