package com.himebaugh.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.database.IngredientEntry;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ListItemViewHolder> {

    private static final String TAG = IngredientsAdapter.class.getSimpleName();
    private List<IngredientEntry> mIngredientList;

    public IngredientsAdapter() {
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_item, viewGroup, false);

        ListItemViewHolder viewHolder = new ListItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        IngredientEntry ingredient = mIngredientList.get(position);
        holder.ingredientName.setText(ingredient.getIngredient());
        holder.ingredientQuantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.ingredientMeasure.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        if (mIngredientList == null) {
            return 0;
        }
        return mIngredientList.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientName;
        TextView ingredientQuantity;
        TextView ingredientMeasure;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            ingredientName = itemView.findViewById(R.id.tv_ingredient);
            ingredientQuantity = itemView.findViewById(R.id.tv_quantity);
            ingredientMeasure = itemView.findViewById(R.id.tv_measure);
        }

    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public List<IngredientEntry> loadIngredients(List<IngredientEntry> ingredientList) {

        Log.i(TAG, "loadIngredients: " + ingredientList.size());

        // check if this cursor is the same as the previous cursor (mCursor)
        if (mIngredientList == ingredientList) {
            return null; // bc nothing has changed
        }
        List<IngredientEntry> temp = mIngredientList;
        mIngredientList = ingredientList; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (ingredientList != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
