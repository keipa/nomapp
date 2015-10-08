package com.nomapp.nomapp_beta.AddIngredients;

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
 * Created by antonid on 17.09.2015.
 */

public class AddIngredientsRecyclerAdapter extends RecyclerView.Adapter<AddIngredientsRecyclerAdapter.ViewHolder> {
    private ArrayList<String> names;
    private ArrayList<Integer> IDs;

    private OnItemTouchListener onItemTouchListener;

    public AddIngredientsRecyclerAdapter(ArrayList<String> names, ArrayList<Integer> IDs, OnItemTouchListener onItemTouchListener) {
        this.names = names;
        this.onItemTouchListener = onItemTouchListener;
        this.IDs = IDs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_ingregient_color, viewGroup, false);
        return new ViewHolder(v, i);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.name.setText(names.get(i));
        Cursor cursor =  Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                new String[]
                        {Database.getIngredientId(), Database.getIngredientName(),
                                Database.getIngredientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        cursor.moveToPosition(IDs.get(i) - 1);
        int isChecked = cursor.getInt(2);
        if (isChecked == 1) {
            viewHolder.name.setBackgroundColor(viewHolder.itemView.getResources()
                    .getColor(R.color.chosenElement)); // второй вариант
            //    Log.w("LOG", IDs.get(position - 1).toString()
            //          + " " + String.valueOf(position) + " " + String.valueOf(isChecked));
        } else{
            viewHolder.name.setBackgroundColor(viewHolder.itemView.getResources()
                    .getColor(R.color.white)); // второй вариант
        }

//TODO
    }

    @Override
    public int getItemCount() {
        return names == null ? 0 : names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView image;


        public ViewHolder(View itemView, int position) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name_of_ingredient_tv);

            name.setText(names.get(position));

            Cursor cursor =  Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                    new String[]
                            {Database.getIngredientId(), Database.getIngredientName(),
                                    Database.getIngredientIsChecked()},
                    null, null, null, null
                    , null);

            cursor.moveToFirst();
            cursor.moveToPosition(IDs.get(position) - 1);
            int isChecked = cursor.getInt(2);
            if (isChecked == 1) {
                name.setBackgroundColor(itemView.getResources().getColor(R.color.chosenElement)); // второй вариант
            //    Log.w("LOG", IDs.get(position - 1).toString()
              //          + " " + String.valueOf(position) + " " + String.valueOf(isChecked));
            } else{
                name.setBackgroundColor(itemView.getResources().getColor(R.color.white)); // второй вариант
            }


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