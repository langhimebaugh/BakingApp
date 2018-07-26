package com.himebaugh.bakingapp.adapter;


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
import com.squareup.picasso.Picasso;

import java.util.List;

// Custom adapter class that displays a list of Recipe Cards in a RecyclerView

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ListItemViewHolder> {

    private static final String TAG = RecipeCardAdapter.class.getSimpleName();
    private List<RecipeEntry> mRecipeList;

    // An on-click handler to make it easy for an Activity to interface with the RecyclerView
    private RecipeCardAdapterOnClickHandler mClickHandler;

    // The interface that receives onClick messages
    public interface RecipeCardAdapterOnClickHandler {
        void onClick(@NonNull RecipeEntry recipe);
    }

    public RecipeCardAdapter(@NonNull RecipeCardAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeCardAdapter.ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_card_item, viewGroup, false);

        ListItemViewHolder viewHolder = new ListItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCardAdapter.ListItemViewHolder holder, int position) {

        RecipeEntry recipe = mRecipeList.get(position);
        holder.recipeName.setText(recipe.getName());

        String imageURL = recipe.getImage();

        Log.i(TAG, "imageURL:=" + imageURL + "<==");

        if (!imageURL.isEmpty()) {
            // if not empty, then try to load and if error display the placeholder.
            Picasso.get().load(imageURL).placeholder(R.drawable.recipe_image_placeholder).into(holder.recipeImage);
        } else {
            // just display the placeholder.
            Picasso.get().load(R.drawable.recipe_image_placeholder).into(holder.recipeImage);
        }


    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null) {
            return 0;
        }
        return mRecipeList.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recipeName;
        ImageView recipeImage;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.tv_recipe_name);
            recipeImage = itemView.findViewById(R.id.iv_recipe_image);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();

            RecipeEntry recipe = mRecipeList.get(adapterPosition);

            Log.i(TAG, "onClick: ");

            mClickHandler.onClick(recipe);
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
