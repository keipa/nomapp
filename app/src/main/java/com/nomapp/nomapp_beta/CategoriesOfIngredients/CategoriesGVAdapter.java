package com.nomapp.nomapp_beta.CategoriesOfIngredients;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.R;

import java.lang.reflect.Type;

/**
 * Created by Антоненко Илья on 10.09.2015.
 */
public class CategoriesGVAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    GridView gridView;
   View view;
    int w;
    int h;
   // TypedArray imagesArray;


    public CategoriesGVAdapter(Context c, GridView gridView) {
        this.gridView = gridView;
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


    public View getView(int position, View convertView, ViewGroup parent) {

        view = mInflater.inflate(R.layout.card_material_category, parent, false);
         TextView name = (TextView)view.findViewById(R.id.name_of_category);
        name.setText(stringArrayCategory[position]);


       /* if (stringArrayCategory[position].length() > 9)
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20 );
       */ /*if (stringArrayCategory[position].length() > 5) {
                name.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView name = (TextView) view.findViewById(R.id.name_of_category);
                        while (name.getLineCount() >= 1) {
                            int lineCount = name.getLineCount();

                            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5);
                           // name.
                        }
                        // Perform any actions you want based on the line count here.
                    }
                });
        }*/

        return view;


    }
    private String[] stringArrayCategory = {
            "Фрукты и ягоды",
            "Птица",
            "Рыба",
            "Морепродукты",
            "Овощи",
            "Фрукты и ягоды",
            "Бакалея",
            "Крупы и бобовые",
            "Молочные продукты",
            "Грибы",
            "Зелень и травы",
            "Орехи",
            "Готовые продукты",
    };
//        ImageView imageView;
//        if (convertView == null) {
//            w = gridView.getColumnWidth();
//            h = w;
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//          //  imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageView.getLayoutParams().width));
//            imageView.setLayoutParams(new GridView.LayoutParams(w, h));
//            imageView.setPadding(8, 8, 8, 8);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//
//        } else {
//            imageView = (ImageView) convertView;
//        }
//
//        imageView.setImageResource(imagesArray[position]);
//        return imageView;


// references to our images
//    private Integer[] imagesArray = {
//            R.drawable.category_meat, R.drawable.category_bird,
//            R.drawable.category_fish, R.drawable.category_seaproducts,
//            R.drawable.category_vegetables, R.drawable.category_fruits,
//            R.drawable.category_bakalei, R.drawable.category_crups,
//            R.drawable.category_milk, R.drawable.category_mashrooms,
//            R.drawable.category_zelen, R.drawable.category_nuts,
//            R.drawable.category_readyproducts
//    };

}
