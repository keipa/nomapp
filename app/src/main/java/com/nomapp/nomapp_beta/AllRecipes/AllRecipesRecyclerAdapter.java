package com.nomapp.nomapp_beta.AllRecipes;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by antonid on 16.09.2015.
 */


public class AllRecipesRecyclerAdapter extends RecyclerView.Adapter<AllRecipesRecyclerAdapter.ViewHolder> {
    private Cursor cursor;

    private ArrayList<String> names;
    private ArrayList<String> cookingTimes;
    private ArrayList<Integer> numbersOfSteps;
    private ArrayList<Integer> numberOfIngs;

    private ArrayList<Integer> IDsOfRecipesInCategory;

    private OnItemTouchListener onItemTouchListener;

    public AllRecipesRecyclerAdapter(ArrayList<Integer> IDsOfRecipesInCategory, OnItemTouchListener onItemTouchListener) {
        this.IDsOfRecipesInCategory = IDsOfRecipesInCategory;
        this.onItemTouchListener = onItemTouchListener;


        names = new ArrayList<>();
        cookingTimes = new ArrayList<>();
        numbersOfSteps = new ArrayList<>();
        numberOfIngs = new ArrayList<>();
        cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(),
                                Database.getRecipesIsAvailable(), Database.getRecipesNumberOfSteps(),
                                Database.getRecipesTimeForCooking(), Database.getRecipesNumberOfIngredients(),
                                Database.getRecipesHowToCook()},
                null, null, null, null
                , null);

        fillRecipesInThisCategory();

    }

    private void fillRecipesInThisCategory() {
        if (cursor.isClosed())
        {
            cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                    new String[]
                            {Database.getRecipesId(), Database.getRecipesName(),
                                    Database.getRecipesIsAvailable(), Database.getRecipesNumberOfSteps(),
                                    Database.getRecipesTimeForCooking(), Database.getRecipesNumberOfIngredients(),
                                    Database.getRecipesHowToCook()},
                    null, null, null, null
                    , null);
        }

        cursor.moveToFirst();
        int size = IDsOfRecipesInCategory.size();
        for (int counter = 0; counter < size; counter++) {
            cursor.moveToPosition(IDsOfRecipesInCategory.get(counter) - 1);
            names.add(cursor.getString(1));
            cookingTimes.add(cursor.getString(4));
            numbersOfSteps.add(cursor.getInt(3));
            numberOfIngs.add(cursor.getInt(5));
        }
        cursor.close();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_available_reciepe, viewGroup, false);
        return new ViewHolder(v, i);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.name.setText(names.get(i));
        viewHolder.time.setText(cookingTimes.get(i));
        viewHolder.numberOfSteps.setText(numbersOfSteps.get(i) + " шагов");
        viewHolder.numberOfIngredients.setText(numberOfIngs.get(i) + " продуктов");

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
        private ImageView image;

        public ViewHolder(View itemView, int position) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.avlRcpNameTV);
            time = (TextView) itemView.findViewById(R.id.timeTV);
            numberOfSteps = (TextView) itemView.findViewById(R.id.numberOfStepsTV);
            numberOfIngredients = (TextView) itemView.findViewById(R.id.numOfIngs);

            image = (ImageView) itemView.findViewById(R.id.avlRcpImageView);

            name.setText(names.get(position));
            time.setText(cookingTimes.get(position) +" мин.");
            numberOfSteps.setText(numbersOfSteps.get(position) + " шагов");
            numberOfIngredients.setText(numberOfIngs.get(position) + " продуктов");


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