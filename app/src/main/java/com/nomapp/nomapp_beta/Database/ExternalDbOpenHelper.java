package com.nomapp.nomapp_beta.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by antonid on 10.07.2015.
 */
public class ExternalDbOpenHelper extends SQLiteOpenHelper {

    public String DB_PATH;
    public String DB_NAME;

    public SQLiteDatabase database;
    public final Context context;

    private static final int DB_VERSION = 2;

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public ExternalDbOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, DB_VERSION);

        this.context = context;

        String packageName = context.getPackageName();
        DB_PATH = String.format("//data//data//%s//databases//", packageName);
        DB_NAME = databaseName;

        initDatabase();
    }

    public void initDatabase() throws SQLException {
        String path = DB_PATH + DB_NAME;

         if (isDatabaseActual()){
            Log.i("database", "Database is actual. Didn't updated");
         } else {
              this.getReadableDatabase();

              try {
                  copyDataBase();
              } catch (IOException e) {
                  Log.e(this.getClass().toString(), "Copying error");
                  throw new Error("Error copying database!");
              }
        }

        database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);
        database.setVersion(DB_VERSION);

    }

    private boolean isDatabaseActual() {
        SQLiteDatabase checkDb = null;
        boolean isVersionCorrect = false;

        try {
            String path = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);
            int ver = checkDb.getVersion();

            isVersionCorrect = DB_VERSION == checkDb.getVersion();

        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }

        if (checkDb != null) {
            checkDb.close();
        }

        return checkDb != null && isVersionCorrect;
    }

    private void copyDataBase() throws IOException {
        InputStream externalDbStream = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;

        OutputStream localDbStream = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }

        localDbStream.close();
        externalDbStream.close();
    }


    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
