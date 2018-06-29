package com.himebaugh.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.database.StepEntry;

import java.util.List;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ListItemViewHolder> {

    private static final String TAG = StepsAdapter.class.getSimpleName();
    private List<StepEntry> mStepList;

    // An on-click handler to make it easy for an Activity to interface with the RecyclerView
    private StepAdapterOnClickHandler mClickHandler;

    // The interface that receives onClick messages
    public interface StepAdapterOnClickHandler {
        void onClick(@NonNull StepEntry step);
    }

    public StepsAdapter(@NonNull StepAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public StepsAdapter.ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.step_item, viewGroup, false);

        return new StepsAdapter.ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.ListItemViewHolder holder, int position) {

        StepEntry step = mStepList.get(position);

        holder.stepNumber.setText(String.valueOf(step.getStepNumber()));
        holder.stepShortDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mStepList == null) {
            return 0;
        }
        return mStepList.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView stepNumber;
        TextView stepShortDescription;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            stepNumber = itemView.findViewById(R.id.tv_step_number);
            stepShortDescription = itemView.findViewById(R.id.tv_short_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();

            StepEntry step = mStepList.get(adapterPosition);

            mClickHandler.onClick(step);
        }
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public List<StepEntry> loadSteps(List<StepEntry> stepList) {

        Log.i(TAG, "loadIngredients: " + stepList.size());

        // check if this cursor is the same as the previous cursor (mCursor)
        if (mStepList == stepList) {
            return null; // bc nothing has changed
        }
        List<StepEntry> temp = mStepList;
        mStepList = stepList; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (stepList != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
