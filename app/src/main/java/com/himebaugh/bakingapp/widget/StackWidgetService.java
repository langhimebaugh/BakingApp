package com.himebaugh.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.database.AppDatabase;
import com.himebaugh.bakingapp.database.IngredientEntry;
import com.himebaugh.bakingapp.database.RecipeEntry;
import com.himebaugh.bakingapp.model.Ingredients;

import java.util.ArrayList;
import java.util.List;

public class StackWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final static String TAG = StackRemoteViewsFactory.class.getName();

    private List<StackWidgetItem> mStackWidgetItems = new ArrayList<StackWidgetItem>();
    private Context mContext;
    private AppDatabase mDb;
    private int mAppWidgetId;


    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {

        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.

        mDb = AppDatabase.getInstance(mContext);
    }

    public void onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mStackWidgetItems.clear();
    }

    public int getCount() {
        return mStackWidgetItems.size();
    }

    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.stack_widget_item);
        remoteViews.setTextViewText(R.id.recipe_name, mStackWidgetItems.get(position).getRecipeName());
        remoteViews.setTextViewText(R.id.recipe_ingredients, getIngredientString(mStackWidgetItems.get(position).getIngredientList()));

        Bundle extras = new Bundle();
        extras.putInt(StackWidgetProvider.RECIPE_ID, mStackWidgetItems.get(position).getRecipeID());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.stack_widget_layout, fillInIntent);

        // Return the remote views object.
        return remoteViews;
    }

    public RemoteViews getLoadingView() {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {

        // Called on start and when notifyAppWidgetViewDataChanged is called

        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.

        Log.i(TAG, "onDataSetChanged: ");

        // mStackWidgetItems = mDb.getRecipeDao().loadRecipeList();

        List<RecipeEntry> recipeList = mDb.getRecipeDao().loadRecipeList();

        for (RecipeEntry recipe : recipeList) {

            List<IngredientEntry> ingredientList = mDb.getIngredientDao().loadIngredientListByRecipeId(recipe.getId());

            List<Ingredients> ingredients = new ArrayList<Ingredients>();

            for (IngredientEntry ingredient : ingredientList) {
                ingredients.add(new Ingredients(ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getIngredient()));
            }

            mStackWidgetItems.add(new StackWidgetItem(recipe.getId(), recipe.getName(), ingredients));
        }

    }

    private String getIngredientString(List<Ingredients> ingredientList) {

        StringBuilder stringBuilder = new StringBuilder();

        for (Ingredients ingredient : ingredientList) {
            stringBuilder.append(ingredient.getQuantity())
                    .append(", ")
                    .append(ingredient.getMeasure())
                    .append(" ")
                    .append(ingredient.getIngredient())
                    .append('\n');
        }

        return stringBuilder.toString();
    }

}











