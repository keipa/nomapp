package com.nomapp.nomapp_beta.FindIngredients;

/**
 * Created by antonid on 20.07.2015.
 */import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;


public class FindedIngredientsListAdapter extends ArrayAdapter<String> {

    private static final String TABLE_NAME = "Ingridients";

    private LayoutInflater mInflater;
    private final Context context;
    private final ArrayList<String> forIngridients;
    private final ArrayList<Integer> IDs;

    public FindedIngredientsListAdapter(Context context, ArrayList<String> forIngridients,ArrayList<Integer> IDs) {
        super(context, R.layout.adding_ingridients_listview, forIngridients);
        this.context = context;
        this.forIngridients = forIngridients;
        this.IDs = IDs;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.card_ingredient, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.nameOfIngTextView);
        textView.setText(forIngridients.get(position));

        Cursor cursor =  Database.getDatabase().getIngridients().query(TABLE_NAME,
                new String[]
                        {Database.getIngridientId(), Database.getIngridientName(),
                                Database.getIngridientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        cursor.moveToPosition(IDs.get(position) - 1);
        int isChecked = cursor.getInt(2);
        if (isChecked == 1) {
            rowView.setBackgroundColor(context.getResources().getColor(R.color.chosenElement)); // второй вариант
            //  textView.setTextColor(context.getResources().getColor(R.color.white));
            Log.d("MY_TAG", "in arrayAdapter");
        }
        return rowView;
    }


}
