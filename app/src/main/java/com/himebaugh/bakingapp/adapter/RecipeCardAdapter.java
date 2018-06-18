package com.himebaugh.bakingapp.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.database.RecipeEntry;
import com.himebaugh.bakingapp.model.Recipe;

import java.util.List;

// Custom adapter class that displays a list of Recipe Cards in a RecyclerView

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ListItemViewHolder> {

    private static final String TAG = RecipeCardAdapter.class.getSimpleName();
    private List<RecipeEntry> mRecipeList;
    private Context mContext;

    /**
     * Constructor for the RecipeCardAdapter that initializes the Context.
     *
     * @param context the current Context
     */
    public RecipeCardAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecipeCardAdapter.ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_card_item, viewGroup, false);

        ListItemViewHolder viewHolder = new ListItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCardAdapter.ListItemViewHolder holder, int position) {

        RecipeEntry recipe = mRecipeList.get(position);
        holder.listItemTest.setText(recipe.getId() + recipe.getName() +  recipe.getServings() );
    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null) {
            return 0;
        }
        return mRecipeList.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        TextView listItemTest;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            listItemTest = (TextView) itemView.findViewById(R.id.tv_recipe_name);

        }

    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public List<RecipeEntry> loadRecipes(List<RecipeEntry> recipeList) {

        Log.i(TAG, "loadRecipes: " + recipeList.size());
        
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mRecipeList == recipeList) {
            return null; // bc nothing has changed
        }
        List<RecipeEntry> temp = mRecipeList;
        mRecipeList = recipeList; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (recipeList != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
