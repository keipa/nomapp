package com.nomapp.nomapp_beta.Steps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nomapp.nomapp_beta.R;

import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class CookingStepsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String currentStep;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    static final int ITEMS_COUNT = 1;

    public CookingStepsRecyclerViewAdapter(String currentStep) {
        this.currentStep= currentStep;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return ITEMS_COUNT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_desc, parent, false);

        return new StepInfoViewHolder(view, currentStep);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                break;
        }
    }

    static class StepInfoViewHolder extends RecyclerView.ViewHolder {
        TextView stepTextView;

        public StepInfoViewHolder(View view, String currentStep) {
            super(view);
            stepTextView = (TextView) view.findViewById(R.id.textView);

            stepTextView.setText(currentStep);
        }
    }

}