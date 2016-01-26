package com.nomapp.nomapp_beta.CategoriesOfRecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.R;

/**
 * Created by Антоненко Илья on 10.09.2015.
 */
public class CategoriesOfRecipesGVAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    GridView gridView;

    int w;
    int h;
   // TypedArray imagesArray;

    private OnItemTouchListener onItemTouchListener;

    public CategoriesOfRecipesGVAdapter(Context c, GridView gridView) {
        this.gridView = gridView;
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        //   imagesArray = mContext.getResources().obtainTypedArray(R.array.images_for_categories);
    }

    public int getCount() {
        return 9;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = null;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.card_material_category, parent, false);
            TextView name = (TextView) convertView.findViewById(R.id.name_of_category);
            TextView numOfRecs = (TextView) convertView.findViewById(R.id.count_of_products);
            TextView example = (TextView) convertView.findViewById(R.id.category_example);
            ImageView icon = (ImageView)  convertView.findViewById(R.id.image_of_category);
            icon.setImageResource(imagesArray[position]);
            //name.setText(names.get(position));
            //numOfIngs.setText(numbersOfIngredients.get(position) + " " + setEnding(numbersOfIngredients.get(position))); //TODO
            //example.setText(examples.get(position));

//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemTouchListener.onCardViewTap(v, position);
//                }
//            });
        }
        return convertView;
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
        R.drawable.child,
        R.drawable.soup,
        R.drawable.meatrec,
        R.drawable.flower,
        R.drawable.fishrec,
        R.drawable.diff,
        R.drawable.doctor,
        R.drawable.pastry,
        R.drawable.vegemash
};


}
