package com.nomapp.nomapp_beta.AvailableRecipes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by antonid on 16.09.2015.
 */


public class AvlReciepesRecyclerViewAdapter extends RecyclerView.Adapter<AvlReciepesRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> names;
    private ArrayList<Integer> cookingTimes;
    private ArrayList<Integer> numbersOfSteps;

    private OnItemTouchListener onItemTouchListener;

    public AvlReciepesRecyclerViewAdapter(ArrayList<String> names, ArrayList<Integer> cookingTimes, ArrayList<Integer> numbersOfSteps, OnItemTouchListener onItemTouchListener) {
        this.names = names;
        this.cookingTimes = cookingTimes;
        this.numbersOfSteps = numbersOfSteps;
        this.onItemTouchListener = onItemTouchListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_available_reciepe, viewGroup, false);
        return new ViewHolder(v, i);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.name.setText(names.get(i));
        viewHolder.time.setText(cookingTimes.get(i) + "");
        viewHolder.numberOfSteps.setText(numbersOfSteps.get(i) + "");
    }

    @Override
    public int getItemCount() {
        return names == null ? 0 : names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView time;
        private TextView numberOfSteps;
        private ImageView image;

        public ViewHolder(View itemView, int position) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.avlRcpNameTV);
            time = (TextView) itemView.findViewById(R.id.timeTV);
            numberOfSteps = (TextView) itemView.findViewById(R.id.numberOfStepsTV);

            image = (ImageView) itemView.findViewById(R.id.avlRcpImageView);

            name.setText(names.get(position));
            time.setText(cookingTimes.get(position) +"");
            numberOfSteps.setText(numbersOfSteps.get(position) + "");


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