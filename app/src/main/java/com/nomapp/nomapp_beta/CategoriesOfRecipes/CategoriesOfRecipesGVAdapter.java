package com.nomapp.nomapp_beta.CategoriesOfRecipes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by Антоненко Илья on 10.09.2015.
 */
public class CategoriesOfRecipesGVAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    ArrayList<String> names;
    ArrayList<String> descriptions;
    ArrayList<Integer> numbersOfIngredients;

    private OnItemTouchListener onItemTouchListener;

    public CategoriesOfRecipesGVAdapter(Context c, OnItemTouchListener onItemTouchListener) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);

        this.onItemTouchListener = onItemTouchListener;

        names = new ArrayList<>();
        descriptions = new ArrayList<>();
        numbersOfIngredients = new ArrayList<>();
        getData();
    }

    public int getCount() {
        return 8;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = null;

        if (convertView == null)
        {
                convertView = mInflater.inflate(R.layout.card_material_category, parent, false);
                TextView name = (TextView) convertView.findViewById(R.id.name_of_category);
                TextView numOfIngs = (TextView) convertView.findViewById(R.id.count_of_products);
                TextView example = (TextView) convertView.findViewById(R.id.category_example);
                ImageView icon = (ImageView)  convertView.findViewById(R.id.image_of_category);

                icon.setImageResource(imagesArray[position]);
                name.setText(names.get(position));
                numOfIngs.setText(numbersOfIngredients.get(position) + " " + getEnding(numbersOfIngredients.get(position)));
                example.setText(descriptions.get(position));

                convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onItemTouchListener.onCardViewTap(v, position);
                            }
                });
        }
        return convertView;
    }

    private void getData()
    {
        Cursor categoryCursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesCategoriesTableName(),   //connection to the base
                new String[]
                        {Database.getRecipesCategoriesName(), Database.getRecipesCategoriesNumberOfRecipes(),
                        Database.getRecipesCategoriesDescription()},
                null, null, null, null
                , null);

        categoryCursor.moveToFirst();

        if (!categoryCursor.isAfterLast()) {            // loop is going throw the all ingridients and shows marked ones (marked has "1" isChecked option)
            do {
                names.add(categoryCursor.getString(0));
                numbersOfIngredients.add(categoryCursor.getInt(1));
                descriptions.add(categoryCursor.getString(2));
            } while (categoryCursor.moveToNext());
        }

        categoryCursor.close();
    }

    private String getEnding(int number)
    {
        int modNumberOAR = number % 10;
        if (modNumberOAR >= 2 && modNumberOAR <= 4){
            return mContext.getResources().getString(R.string.twofourrec);
        }
        if (modNumberOAR >=5 || modNumberOAR == 0){
           return mContext.getResources().getString(R.string.morerec);
        }
        if (modNumberOAR == 1){
            if (number % 100 == 11){
                return mContext.getResources().getString(R.string.morerec);
            }
            return mContext.getResources().getString(R.string.onerec);
        }
        return null;
    }

    public interface OnItemTouchListener {
        /**
         * Callback invoked when the user Taps one of the RecyclerView items
         *
         * @param view     the CardView touched
         * @param position the index of the item touched in the RecyclerView
         */
       public void onCardViewTap(View view, int position);

    }
// references to our images
private Integer[] imagesArray = {
        R.drawable.colddish,
        R.drawable.soup,
        R.drawable.fishrec,
        R.drawable.meatrec,
        R.drawable.vegemash,
//        R.drawable.flower,
        R.drawable.pastry,
        R.drawable.sugar,
        R.drawable.diff,
        R.drawable.vegemash,
        R.drawable.sugar,
        R.drawable.diff
};



}
