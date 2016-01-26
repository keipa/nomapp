package com.nomapp.nomapp_beta.NavigationDrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.R;

/**
 * Created by antonid on 29.10.2015.
 */
public class NavDrawerListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
   // ArrayList<Product> objects;
    //Data for out NavDrawer
    String[] items = {"Холодильник", "Все рецепты", "Настройки", "Оставить отзыв",
    "Справка"};

    int currentPosition;

    int[] images = {R.drawable.ic_fridge_dr, R.drawable.ic_recepies_dr};

    public NavDrawerListAdapter(Context context, int currentPosition) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentPosition = currentPosition;
    }

    public NavDrawerListAdapter(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currentPosition = 100; //any incredible number
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return items.length;
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return items[position];
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
      //  TextView text = (TextView)convertView.findViewById(R.id.navdrawer_name_of_item);
        if (view == null) {
            if (position < 2) { //Main items
                if (position == 1){
                    view = lInflater.inflate(R.layout.navdrawer_item_with_separator, parent, false);
                    ((TextView) view.findViewById(R.id.navdrawer_name_of_item)).setText(items[position]);
                    ((ImageView) view.findViewById(R.id.navdrawer_item_icon)).
                            setImageResource(images[position]);

                } else {
                    view = lInflater.inflate(R.layout.navdrawer_item, parent, false);
                    ((TextView) view.findViewById(R.id.navdrawer_name_of_item)).setText(items[position]);
                    ((ImageView) view.findViewById(R.id.navdrawer_item_icon)).
                            setImageResource(images[position]);
                }
            } else {
                view = lInflater.inflate(R.layout.navdrawer_additional_item, parent, false);
                ((TextView) view.findViewById(R.id.navdrawer_name_of_additional_item)).setText(items[position]);
            }

            if (position == currentPosition) {
                ((TextView) view.findViewById(R.id.navdrawer_name_of_item)).setTextColor(ctx.getResources().getColor(R.color.chosenElement));
                view.setBackgroundColor(ctx.getResources().getColor(R.color.grey));
            }

        }

        return view;
    }

}
