package com.nomapp.nomapp_beta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Антоненко Илья on 10.09.2015.
 */
public class CategoriesGVAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    String[] data = {"Мясо", "Птица", "Рыба", "Морепродукты", "Овощи", "Фрукты и ягоды", "Бакалея",
            "Крупы", "Молочные продукты", "Грибы", "Зелень", "Орехи", "Готовые продукты"};

    public CategoriesGVAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
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
        TextView text = (TextView) view.findViewById(R.id.textView5);
        text.setText(data[position]);
        return view;
    }

}
