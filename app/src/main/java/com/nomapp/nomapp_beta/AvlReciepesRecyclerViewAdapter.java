package com.nomapp.nomapp_beta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonid on 16.09.2015.
 */


public class AvlReciepesRecyclerViewAdapter extends RecyclerView.Adapter<AvlReciepesRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> names;
    private ArrayList<Integer> cookingTimes;

    private OnItemTouchListener onItemTouchListener;

    public AvlReciepesRecyclerViewAdapter(ArrayList<String> names, ArrayList<Integer> cookingTimes, OnItemTouchListener onItemTouchListener) {
        this.names = names;
        this.cookingTimes = cookingTimes;
        this.onItemTouchListener = onItemTouchListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_available_reciepe, viewGroup, false);
        return new ViewHolder(v, i);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
    //    viewHolder.title.setText(names.get(i));
    }

    @Override
    public int getItemCount() {
        return names == null ? 0 : names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public ViewHolder(View itemView, int position) {
            super(itemView);

            TextView textView = (TextView) itemView.findViewById(R.id.avlRcpNameTW);
            TextView time = (TextView) itemView.findViewById(R.id.timeTW);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.avlRcpImageView);

            textView.setText(names.get(position));
            time.setText(cookingTimes.get(position) +"");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onCardViewTap(v, getPosition());
                }
            });
        }
    }
    /**
     * Interface for the touch events in each item
     */
    public interface OnItemTouchListener {
        /**
         * Callback invoked when the user Taps one of the RecyclerView items
         *
         * @param view     the CardView touched
         * @param position the index of the item touched in the RecyclerView
         */
        public void onCardViewTap(View view, int position);

    }


}