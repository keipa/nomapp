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

    //���� � ����� � ������ �� ����������
    public String DB_PATH;
    //��� ����� � �����
    public String DB_NAME;
    public SQLiteDatabase database;
    public final Context context;

    public SQLiteDatabase getDb() {
        return database;
    }

    public ExternalDbOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
        this.context = context;
        //�������� ������ ���� � ����� ��� ������ ����������
        String packageName = context.getPackageName();
        DB_PATH = String.format("//data//data//%s//databases//", packageName);
        DB_NAME = databaseName;
        openDataBase();
    }

    //������� ����, ���� ��� �� �������
    public void createDataBase() {
        boolean dbExist = checkDataBase();

        //  if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }

      /*  } else {
            Log.i(this.getClass().toString(), "Database already exists");
        }*/
    }

    //�������� ������������� ���� ������
    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        try {
            String path = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }
        //������� �� ����� ������ ��������, ��� ������ �����������
        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }

    //����� ����������� ����
    private void copyDataBase() throws IOException {
        // ��������� ����� ��� ������ �� ��� ��������� ���� ��
        //�������� � assets
        InputStream externalDbStream = context.getAssets().open(DB_NAME);

        // ���� � ��� ��������� ������ ���� � ��������
        String outFileName = DB_PATH + DB_NAME;

        // ������ �������� ����� ��� ������ � ��� �� ��������
        OutputStream localDbStream = new FileOutputStream(outFileName);

        // ���������� �����������
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        // �� ����� �������� ����������(���������) � ������� ������
        localDbStream.close();
        externalDbStream.close();

    }

    public SQLiteDatabase openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;

    //    if (!checkDataBase())
   //         createDataBase();

        createDataBase();

        database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);

        return database;
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
