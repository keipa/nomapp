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
    private SQLiteDatabase recipes;

    private static final String INGRIDIENTS_DB_NAME = "database.db";
    private static final String RECIPES_DB_NAME = "reciepes.db";

    private static final String INGRIDIENT_ID = "_id";
    private static final String INGRIDIENT_NAME = "name";
    private static final String IS_CHECKED = "checked";

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
//        StartActivity act = new StartActivity();
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(context, INGRIDIENTS_DB_NAME);
        ingridients = dbOpenHelper.openDataBase();

        ExternalDbOpenHelper reciepesOpenHelper = new ExternalDbOpenHelper(context, RECIPES_DB_NAME);
        recipes = reciepesOpenHelper.openDataBase();
    }

    public SQLiteDatabase getIngridients(){
        return ingridients;
    }
    public SQLiteDatabase getRecipes(){
        return recipes;
    }

    public static String getIngridientId(){
        return INGRIDIENT_ID;
    }
    public static String getIngridientName(){
        return INGRIDIENT_NAME;
    }
    public static String getIngridientIsChecked(){
        return IS_CHECKED;
    }


}
