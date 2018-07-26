package com.himebaugh.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.RecipeStepListActivity;

public class StackWidgetProvider extends AppWidgetProvider {

    private final static String TAG = StackWidgetProvider.class.getName();

    public static final String CLICK_ACTION = "com.himebaugh.bakingapp.widget.CLICK_ACTION";
    public static final String RECIPE_ID = "com.himebaugh.bakingapp.widget.EMPLOYEE_ID";

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(CLICK_ACTION)) {

            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            int recipeID = intent.getIntExtra(RECIPE_ID, 0);

            // Create an Intent to launch RecipeStepListActivity
            Intent detailIntent = new Intent(context, RecipeStepListActivity.class);
            detailIntent.putExtra(RecipeStepListActivity.EXTRA_RECIPE_ID, recipeID);
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(detailIntent);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Perform this loop procedure for each App Widget that belongs to this provider
        // update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {

            Intent intent = new Intent(context, StackWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stack_widget_layout);
            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);
            remoteViews.setEmptyView(R.id.stack_view, R.id.empty_view);

            // Intent to provide data to the onReceive() above.
            Intent clickIntent = new Intent(context, StackWidgetProvider.class);
            clickIntent.setAction(StackWidgetProvider.CLICK_ACTION);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setPendingIntentTemplate(R.id.stack_view, clickPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);

//            Intent wateringIntent = new Intent(context, PlantWateringService.class);
//            wateringIntent.setAction(PlantWateringService.ACTION_WATER_PLANT);
//            wateringIntent.putExtra(PlantWateringService.EXTRA_PLANT_ID, plantId);
//            PendingIntent wateringPendingIntent = PendingIntent.getService(context, 0, wateringIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            views.setOnClickPendingIntent(R.id.widget_water_button, wateringPendingIntent);
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