package com.himebaugh.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.himebaugh.bakingapp.R;
import com.himebaugh.bakingapp.database.StepEntry;

import java.util.List;


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ListItemViewHolder> {

    private static final String TAG = StepAdapter.class.getSimpleName();
    private List<StepEntry> mStepList;

    // An on-click handler to make it easy for an Activity to interface with the RecyclerView
    private StepAdapterOnClickHandler mClickHandler;

    // The interface that receives onClick messages
    public interface StepAdapterOnClickHandler {
        void onClick(@NonNull StepEntry step);
    }

    public StepAdapter(@NonNull StepAdapterOnClickHandler clickHandler, @NonNull List<StepEntry> stepList) {
        mClickHandler = clickHandler;
        mStepList = stepList;
    }

    @NonNull
    @Override
    public StepAdapter.ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.step_item, viewGroup, false);

        return new StepAdapter.ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.ListItemViewHolder holder, int position) {
        StepEntry step = mStepList.get(position);
        holder.stepShortDescription.setText(step.getShortDescription());
        holder.stepDescription.setText(String.valueOf(step.getDescription()));
        holder.stepVideoURL.setText(step.getVideoURL());
        holder.stepThumbnailURL.setText(step.getThumbnailURL());
    }

    @Override
    public int getItemCount() {
        if (mStepList == null) {
            return 0;
        }
        return mStepList.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView stepShortDescription;
        TextView stepDescription;
        TextView stepVideoURL;
        TextView stepThumbnailURL;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            stepShortDescription = itemView.findViewById(R.id.tv_short_description);
            stepDescription = itemView.findViewById(R.id.tv_description);
            stepVideoURL = itemView.findViewById(R.id.tv_video_url);
            stepThumbnailURL = itemView.findViewById(R.id.tv_thumbnail_url);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();

            StepEntry step = mStepList.get(adapterPosition);

            mClickHandler.onClick(step);
        }
    }


//
//
//    private List<Step> steps;
//    private Recipe recipe;
//    private ListItemClickedListener listItemClickedListener;
//
//    public interface ListItemClickedListener {
//        void onListItemClicked(@NonNull Step step, @NonNull Recipe recipe);
//    }
//
//    StepAdapter(@NonNull ListItemClickedListener listItemClickedListener, @NonNull List<Step> steps, @NonNull Recipe recipe) {
//        this.listItemClickedListener = listItemClickedListener;
//        this.steps = steps;
//        this.recipe = recipe;
//    }
//
//    @Override
//    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_list_item, parent, false);
//        return new StepsViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(StepsViewHolder holder, int position) {
//        holder.bind(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return steps != null && steps.size() > 0 ? steps.size() : 0;
//    }
//
//    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        @BindView(R.id.stepsTextView) TextView stepTextView;
//
//        StepsViewHolder(View itemView) {
//            super(itemView);
//
//            ButterKnife.bind(this, itemView);
//
//            itemView.setOnClickListener(this);
//        }
//
//        void bind(int listItem) {
//            stepTextView.setText(steps.get(listItem).getShortDescription());
//        }
//
//        @Override
//        public void onClick(View view) {
//            listItemClickedListener.onListItemClicked(steps.get(getAdapterPosition()), recipe);
//        }
//    }
//
//    public class ListItemViewHolder extends RecyclerView.ViewHolder {
//
//        TextView stepShortDescription;
//        TextView stepDescription;
//        TextView stepVideoURL;
//        TextView stepThumbnailURL;
//
//        public ListItemViewHolder(View itemView) {
//            super(itemView);
//
//            ingredientName = itemView.findViewById(R.id.tv_ingredient);
//            ingredientQuantity = itemView.findViewById(R.id.tv_quantity);
//            ingredientMeasure = itemView.findViewById(R.id.tv_measure);
//
//        }
//
//    }


}
