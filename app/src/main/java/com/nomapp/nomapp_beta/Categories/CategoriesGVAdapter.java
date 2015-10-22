package com.nomapp.nomapp_beta.Categories;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
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
public class CategoriesGVAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
   // TypedArray imagesArray;


    public CategoriesGVAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
     //   imagesArray = mContext.getResources().obtainTypedArray(R.array.images_for_categories);
    }

    public int getCount() {
        return 13;
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
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(20, 8, 8, 8);
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
            R.drawable.category_milk, R.drawable.category_mashrooms,
            R.drawable.category_zelen, R.drawable.category_nuts,
            R.drawable.category_readyproducts
    };

}
