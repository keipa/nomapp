package com.nomapp.nomapp_beta.AddIngredients;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by antonid on 05.07.2015.
 */

public class AddIngridientsActivity extends Activity {
    private static final String TABLE_NAME = "Ingridients";

    private TextView nameOfCategotyView;

    private ListView listView;
    private ArrayList<String> forIngridients;
    private ArrayList<Integer> IDs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingridients);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorMainDark));

        nameOfCategotyView = (TextView) findViewById(R.id.nameOfCategory);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fillIngridients();
        setUpList();
    }

    private void setUpList() {
        MyArrayAdapter adapter = new MyArrayAdapter(this, forIngridients, IDs);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Cursor cursor = Database.getDatabase().getIngridients().query(TABLE_NAME,
                        new String[]
                                {Database.getIngridientId(), Database.getIngridientName(),
                                        Database.getIngridientIsChecked()},
                        null, null, null, null
                        , null);

                cursor.moveToFirst();
                cursor.moveToPosition(IDs.get(position) - 1);
                int isChecked = cursor.getInt(2);
                if (isChecked == 0) {
                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=1 WHERE _id=" + IDs.get(position) + ";");
                    view.setBackgroundColor(getResources().getColor(R.color.chosenElement));
                    //  ((TextView) view).setTextColor(getResources().getColor(R.color.chosenElement));
                    Log.d("MY_TAG", "Checked position " + IDs.get(position));

                } else {
                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                    Log.d("MY_TAG", "Unchecked position " + IDs.get(position));

                }
                cursor.close();
            }
        });
    }


    //Извлечение элментов из базы данных
    private void fillIngridients() { //
        forIngridients = new ArrayList<>();
        IDs = new ArrayList<>();

        Cursor cursor = Database.getDatabase().getIngridients().query(TABLE_NAME,
                new String[]
                        {Database.getIngridientId(), Database.getIngridientName(),
                                Database.getIngridientIsChecked()},
                null, null, null, null
                , null);

        Cursor categoryCursor = Database.getDatabase().getCategories().query(Database.getCategoriesTableName(),
                new String[]
                        {Database.getCategoriesId(), Database.getCategoryName(),
                                Database.getCategoryIngredients()},
                null, null, null, null
                , null);

        categoryCursor.moveToFirst();
        Intent intent = getIntent();
        categoryCursor.moveToPosition(intent.getIntExtra("numberOfCategory", 0) - 1);

        nameOfCategotyView.setText(categoryCursor.getString(1));

        IDs = parse(categoryCursor.getString(2));
        categoryCursor.close();

        cursor.moveToFirst();
//        if (!cursor.isAfterLast()) {
//            do {
//                String name = cursor.getString(1);
//                forIngridients.add(name);
//            } while (cursor.moveToNext());
//        }
        int size = IDs.size();
        for (int counter = 0; counter < size; counter++) {
            cursor.moveToPosition(IDs.get(counter) - 1);
            forIngridients.add(cursor.getString(1));
        }

        cursor.close();

    }

    private ArrayList<Integer> parse(String toConvert) {
        ArrayList<Integer> converted = new ArrayList<Integer>();

        int counter = 0;
        int factor = 1;
        int currentIngridient = 0;
        int size = toConvert.length();
        for (counter = 0; counter < size; counter++) {
            while (toConvert.charAt(counter) != ',' && toConvert.charAt(counter) != '.') {//TODO
                currentIngridient += (toConvert.charAt(counter) - '0') * factor;
                factor *= 10;
                counter++;
            }
            factor = 1;
            converted.add(currentIngridient);
            currentIngridient = 0;
        }
        return converted;
    }

}
