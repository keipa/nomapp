package com.nomapp.nomapp_beta.SlidingTabs;

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

    List<Object> contents;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public CookingStepsRecyclerViewAdapter(List<Object> contents) {
        this.contents = contents;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_desc, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_note, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
        }
        return null;
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

   /* static class StepInfoViewHolder extends RecyclerView.ViewHolder {
        TextView stepTextView;

        public StepInfoViewHolder(View view ,String path, Context ctx) {
            super(view);
            stepTextView = (TextView) view.findViewById(R.id.textView);
            int id = ctx.getResources().getIdentifier(nameOfSteps + (position + 1), "string", getPackageName());
            txt.setText(getResources().getText(id));

            stepTextView.setText(ctx.getResources().getText(id));
        }
    }*/

}