package com.nomapp.nomapp_beta;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * Created by antonid on 05.07.2015.
 */

public class AddIngridientsActivity extends ListActivity {
    private static final String TABLE_NAME = "Ingridients";
    private static final String INGRIDIENT_ID = "_id";
    private static final String INGRIDIENT_NAME = "name";
    private static final String IS_CHECKED = "checked";


    private ListView listView;
    private ArrayList<String> forIngridients;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingridients);

        fillIngridients();
        setUpList();
    }

    private void setUpList() {
        MyArrayAdapter adapter = new MyArrayAdapter(this, forIngridients);
        setListAdapter(adapter);
        listView = getListView();
    }

    @Override
    protected void onListItemClick(ListView l, View view, int position, long id) {
        Cursor cursor =  Database.getDatabase().getIngridients().query(TABLE_NAME,
                new String[]
                        {INGRIDIENT_ID, INGRIDIENT_NAME, IS_CHECKED},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        cursor.moveToPosition(position);
        int isChecked = cursor.getInt(2);
        if (isChecked == 0){
            Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=1 WHERE _id=" + (position + 1) + ";");
             view.setBackgroundColor(getResources().getColor(R.color.chosenElement));
          //  ((TextView) view).setTextColor(getResources().getColor(R.color.chosenElement));
            Log.d("MY_TAG", "in activity");

        } else {
            Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=0 WHERE _id=" + (position + 1) + ";");
           view.setBackgroundColor(getResources().getColor(R.color.white));
        }
        cursor.close();
    }

    //Извлечение элментов из базы данных
    private void fillIngridients() {
        forIngridients = new ArrayList<String>();
        Cursor cursor =  Database.getDatabase().getIngridients().query(TABLE_NAME,
                new String[]
                        {INGRIDIENT_ID, INGRIDIENT_NAME, IS_CHECKED},
                null, null, null, null
                , null);
        
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                String name = cursor.getString(1);
                forIngridients.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }



}
