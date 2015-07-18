package com.nomapp.nomapp_beta;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by antonid on 05.07.2015.
 */

public class AddIngridientsActivity extends ListActivity {
  //  private static final String DB_NAME = "database.db";
    //Хорошей практикой является задание имен полей БД константами
    private static final String TABLE_NAME = "Ingridients";
    private static final String INGRIDIENT_ID = "_id";
    private static final String INGRIDIENT_NAME = "name";
    private static final String IS_CHECKED = "checked";
  /*  private static final String UPDATE_CHECHED = "UPDATE" + TABLE_NAME + "SET " +
            IS_CHECKED +  +"WHERE _id=1;";*/


    //private SQLiteDatabase database;
    private ListView listView;
    private ArrayList<String> forIngridients;
    private  SQLiteDatabase database;
    private String newChecked;
    //private Cursor cursor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingridients);



        fillIngridients();
        setUpList();
    }

    private void setUpList() {
        //Испльзуем стандартный адаптер и layout элемента для краткости
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, forIngridients));
        listView = getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Cursor cursor =  Database.getDatabase().getIngridients().query(TABLE_NAME,
                        new String[]
                                {INGRIDIENT_ID, INGRIDIENT_NAME, IS_CHECKED},
                        null, null, null, null
                        , null);

                cursor.moveToFirst();
                cursor.moveToPosition(position);
                int isChecked = cursor.getInt(2);
                if (isChecked == 0){
                    isChecked = 1;
                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=1 WHERE _id=" + (position + 1) + ";");
                    view.setBackgroundColor(getResources().getColor(R.color.chosenElement));

                } else {
                    isChecked = 0;
                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=0 WHERE _id=" + (position + 1) + ";");
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }

            }
        });
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
