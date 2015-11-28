package com.nomapp.nomapp_beta.CategoriesOfRecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
        ImageView imageView;
        if (convertView == null) {
            w = gridView.getColumnWidth();
            h = w;
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
          //  imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageView.getLayoutParams().width));
            imageView.setLayoutParams(new GridView.LayoutParams(w, h));
            imageView.setPadding(8, 8, 8, 8);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(imagesArray[position]);
        return imageView;
    }

// references to our images
    private Integer[] imagesArray = {
            R.drawable.category_meat, R.drawable.category_bird,
            R.drawable.category_fish, R.drawable.category_seaproducts,
            R.drawable.category_vegetables, R.drawable.category_fruits,
            R.drawable.category_bakalei, R.drawable.category_crups,
            R.drawable.category_milk
    };

}
