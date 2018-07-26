package com.himebaugh.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.RecipeStepListActivity;

public class WidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int recipeId, String recipeName, int appWidgetId) {

        CharSequence widgetText = recipeName;

        // Create an Intent to launch MainActivity when clicked
        Intent intent = new Intent(context, RecipeStepListActivity.class);
        intent.putExtra(RecipeStepListActivity.EXTRA_RECIPE_ID, recipeId);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        // Widgets allow click handlers to only launch pending intents
        // views.setOnClickPendingIntent(R.id.widget_plant_image, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.recipe_name, pendingIntent);

        remoteViews.setTextViewText(R.id.recipe_name, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Start the intent service update widget action, the service takes care of updating the widgets UI
        // Service will call method below...
        WidgetIntentService.startActionUpdateRecipeWidgets(context);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int recipeId, String recipeName, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeId, recipeName, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        // Perform any action when the last AppWidget instance for this provider is deleted
    }
}

