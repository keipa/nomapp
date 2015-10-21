package com.nomapp.nomapp_beta.Categories;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.R;

/**
 * Created by Антоненко Илья on 10.09.2015.
 */
public class CategoriesGVAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    TypedArray imagesArray;


    public CategoriesGVAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        imagesArray = mContext.getResources().obtainTypedArray(R.array.images_for_categories);
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
        View view;
        view = mInflater.inflate(R.layout.category_item, parent, false);

        ImageView image = (ImageView) view.findViewById(R.id.category_image);
        image.setImageResource(imagesArray.getResourceId(position, -1));

        return view;
    }

}
