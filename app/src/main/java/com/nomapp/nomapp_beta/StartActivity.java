package com.nomapp.nomapp_beta;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class StartActivity extends ActionBarActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";

    Button addIngridients;

    public String[] ingridients  = {"Svekla", "Morkov"};

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        dbHelper = new DBHelper(this);

        addIngridients = (Button) findViewById(R.id.addIngBtn);
        addIngridients.setOnClickListener(this);

        ContentValues cv = new ContentValues();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("name", ingridients[0]);
        //long rowID = db.insert("mytable", null, cv);
        //Log.d(LOG_TAG, "row inserted, ID = " + rowID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addIngBtn:
                Intent intent = new Intent(StartActivity.this, AddIngridientsActivity.class);
                startActivity(intent);
                break;

            default:

                break;
        }
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
