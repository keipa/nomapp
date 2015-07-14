package com.nomapp.nomapp_beta;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by antonid on 12.07.2015.
 */
public class Database {
    private static Database database;

    private SQLiteDatabase ingridients;
    private static final String DB_NAME = "database.db";

    public static void initDatabase(Context context) {
        Log.d("MY_TAG", "MySingleton::InitInstance()");
        if (database == null) {
            database = new Database(context);
        }
    }

    public static Database getDatabase() {
        Log.w("MY_TAG", "Database::getDatabase");
        return database;
    }

    private Database(Context context) {
        Log.w("MY_TAG", "MySingleton::MySingleton()");
        StartActivity act = new StartActivity();
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(context, DB_NAME);
        ingridients = dbOpenHelper.openDataBase();
    }

    public SQLiteDatabase getIngridients() {
        return ingridients;
    }

    public void setIngridients(SQLiteDatabase newIngridients) {
        ingridients = newIngridients;
    }
}
