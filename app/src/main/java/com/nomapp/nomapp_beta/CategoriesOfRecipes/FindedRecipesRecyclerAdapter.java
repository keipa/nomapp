package com.nomapp.nomapp_beta.CategoriesOfRecipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by antonid on 17.09.2015.
 */

public class FindedRecipesRecyclerAdapter extends RecyclerView.Adapter<FindedRecipesRecyclerAdapter.ViewHolder> {
    private ArrayList<String> names;
    private ArrayList<Integer> cookingTimes;
    private ArrayList<Integer> numbersOfSteps;
    private ArrayList<Integer> numbersOfIngs;
    private ArrayList<String> measuresForTime;

    Context ctx;


    private OnItemTouchListener onItemTouchListener;

    public FindedRecipesRecyclerAdapter(Context ctx, ArrayList<String> names,
                                     ArrayList<Integer> cookingTimes, ArrayList<Integer> numbersOfSteps,
                                     ArrayList<Integer> numberOfIngs, ArrayList<String> measuresForTime,
                                        OnItemTouchListener onItemTouchListener) {
        this.names = names;
        this.cookingTimes = cookingTimes;
        this.numbersOfSteps = numbersOfSteps;
        this.onItemTouchListener = onItemTouchListener;
        this.numbersOfIngs = numberOfIngs;
        this.measuresForTime = measuresForTime;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_material_recipe, viewGroup, false);
        return new ViewHolder(v, i);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.name.setText(names.get(i));
        viewHolder.time.setText(cookingTimes.get(i) + "");
        viewHolder.numberOfSteps.setText(numbersOfSteps.get(i) + "");
        viewHolder.numberOfIngredients.setText(numbersOfIngs.get(i) + "");

        viewHolder.textSteps.setText(getStepsEnding(numbersOfSteps.get(i)));
        viewHolder.textNumberOfProducts.setText(getProductsEnding(numbersOfIngs.get(i)));
        viewHolder.textMeasureForTime.setText(measuresForTime.get(i));

    }

    @Override
    public int getItemCount() {
        return names == null ? 0 : names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView time;
        private TextView numberOfSteps;
        private TextView numberOfIngredients;
        private TextView textSteps;
        private TextView textNumberOfProducts;
        private TextView textMeasureForTime;

        private ImageView image;

        public ViewHolder(View itemView, int position) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.avlRcpNameTV);
            time = (TextView) itemView.findViewById(R.id.time_tv);
            numberOfSteps = (TextView) itemView.findViewById(R.id.steps_tv);
            numberOfIngredients = (TextView) itemView.findViewById(R.id.number_of_products_tv);

            textSteps = (TextView) itemView.findViewById(R.id.steps_tv);
            textNumberOfProducts = (TextView) itemView.findViewById(R.id.text_ings);
            textMeasureForTime = (TextView) itemView.findViewById(R.id.measure_of_time_tv);


            image = (ImageView) itemView.findViewById(R.id.avlRcpImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onCardViewTap(v, getPosition());
                }
            });
        }
    }



    private String getStepsEnding(int count)
    {
        String toReturn = "";
        int modNumberOAR =count  % 10;
        if (modNumberOAR >=2 && modNumberOAR <=4){
            toReturn = ctx.getResources().getString(R.string.two_or_four_steps);

        }
        if (modNumberOAR >=5 || modNumberOAR == 0){
            toReturn = ctx.getResources().getString(R.string.more_steps);
        }
        if (modNumberOAR == 1){
            toReturn = ctx.getResources().getString(R.string.one_step);
        }
        return toReturn;
    }

    private String getProductsEnding(int count)
    {
        String toReturn = "";
        int modNumberOAR =count  % 10;
        if (modNumberOAR >=2 && modNumberOAR <=4){
            toReturn = ctx.getResources().getString(R.string.two_or_four_products);

        }
        if (modNumberOAR >=5 || modNumberOAR == 0){
            toReturn = ctx.getResources().getString(R.string.more_products);
        }
        if (modNumberOAR == 1){
            toReturn = ctx.getResources().getString(R.string.one_product);
        }
        return toReturn.toUpperCase();
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