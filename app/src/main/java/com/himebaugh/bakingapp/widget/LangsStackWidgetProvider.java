package com.himebaugh.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.RecipeStepListActivity;
import com.himebaugh.bakingapp.database.AppDatabaseContract;
import com.himebaugh.bakingapp.database.AppDatabaseProvider;

public class LangsStackWidgetProvider extends AppWidgetProvider {

    private final static String TAG = LangsStackWidgetProvider.class.getName();

    //public static final String TOAST_ACTION = "com.himebaugh.bakingapp.widget.TOAST_ACTION";
    //public static final String EXTRA_ITEM = "com.himebaugh.bakingapp.widget.EXTRA_ITEM";
    public static final String CLICK_ACTION = "com.himebaugh.bakingapp.widget.CLICK_ACTION";
    public static final String RECIPE_ID = "com.himebaugh.bakingapp.widget.EMPLOYEE_ID";

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(CLICK_ACTION)) {

            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            int recipeID = intent.getIntExtra(RECIPE_ID, 0);

            //========================================

//            Uri details = Uri.withAppendedPath(AppDatabaseContract.RecipeEntry.CONTENT_URI, "" + recipeID);
//            Intent detailsIntent = new Intent(Intent.ACTION_VIEW, details);
//            detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(detailsIntent);

            // Uri details = Uri.withAppendedPath(AppDatabaseProvider.buildUriMatcher().addURI()   , "" + recipeID);

            Intent detailIntent = new Intent(context, RecipeStepListActivity.class);
            detailIntent.putExtra(RecipeStepListActivity.EXTRA_RECIPE_ID, recipeID);
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(detailIntent);

            //=============================

            Log.i(TAG, "recipeID: " + recipeID);

//            Intent detailIntent = new Intent(context, RecipeStepListActivity.class);
//            detailIntent.putExtra(RecipeStepListActivity.EXTRA_RECIPE_ID, recipeID);
//            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            //===========================

//            context.startActivity(detailIntent);
//
//            Intent intent = new Intent(context, RecipeStepListActivity.class);
//            intent.putExtra(DetailRecipeListActivity.RECIPE_EXTRA, recipe);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

//        if (intent.getAction().equals(TOAST_ACTION)) {
//            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
//            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
//            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
//        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {

            Intent intent = new Intent(context, LangsStackWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.langs_widget_layout);
            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);
            remoteViews.setEmptyView(R.id.stack_view, R.id.empty_view);

            // ===================================
//            Intent clickIntent = new Intent(context, RecipeStepListActivity.class);
//            clickIntent.setAction(LangsStackWidgetProvider.CLICK_ACTION);
//            clickIntent.putExtra(RecipeStepListActivity.EXTRA_RECIPE_ID, recipeID);
//
//            PendingIntent clickPendingIntent = PendingIntent.getActivity(context,0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // ===================================

            Intent clickIntent = new Intent(context, LangsStackWidgetProvider.class);
            clickIntent.setAction(LangsStackWidgetProvider.CLICK_ACTION);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setPendingIntentTemplate(R.id.stack_view, clickPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {

        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {

        super.onEnabled(context);
    }

}