package com.nomapp.nomapp_beta;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
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
    private static final String DB_NAME = "database.db";
    //Хорошей практикой является задание имен полей БД константами
    private static final String TABLE_NAME = "Ingridients";
    private static final String INGRIDIENT_ID = "_id";
    private static final String INGRIDIENT_NAME = "name";

    private SQLiteDatabase database;
    private ListView listView;
    private ArrayList<String> friends;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingridients);

        //Наш ключевой хелпер
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(this, DB_NAME);
        database = dbOpenHelper.openDataBase();
        //Все, база открыта!
        fillFreinds();
        setUpList();
    }

    private void setUpList() {
        //Испльзуем стандартный адаптер и layout элемента для краткости
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, friends));
        listView = getListView();

        //Подарим себе тост, для души
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                view.setBackgroundColor(getResources().getColor(R.color.chosenElement));
            }
        });
    }

    //Извлечение элментов из базы данных
    private void fillFreinds() {
        friends = new ArrayList<String>();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]
                        {INGRIDIENT_ID, INGRIDIENT_NAME},
                null, null, null, null
                , null);

        
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                String name = cursor.getString(1);
                friends.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    public class ExternalDbOpenHelper extends SQLiteOpenHelper {

        //Путь к папке с базами на устройстве
        public String DB_PATH;
        //Имя файла с базой
        public String DB_NAME;
        public SQLiteDatabase database;
        public final Context context;

        public SQLiteDatabase getDb() {
            return database;
        }

        public ExternalDbOpenHelper(Context context, String databaseName) {
            super(context, databaseName, null, 1);
            this.context = context;
            //Составим полный путь к базам для вашего приложения
            String packageName = context.getPackageName();
            DB_PATH = String.format("//data//data//%s//databases//", packageName);
            DB_NAME = databaseName;
            openDataBase();
        }

        //Создаст базу, если она не создана
        public void createDataBase() {
            boolean dbExist = /*checkDataBase()*/ false;
            if (!dbExist) {
                this.getReadableDatabase();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    Log.e(this.getClass().toString(), "Copying error");
                    throw new Error("Error copying database!");
                }
            } else {
                Log.i(this.getClass().toString(), "Database already exists");
            }
        }

        //Проверка существования базы данных
        private boolean checkDataBase() {
            SQLiteDatabase checkDb = null;
            try {
                String path = DB_PATH + DB_NAME;
                checkDb = SQLiteDatabase.openDatabase(path, null,
                        SQLiteDatabase.OPEN_READONLY);
            } catch (SQLException e) {
                Log.e(this.getClass().toString(), "Error while checking db");
            }
            //Ардроид не любит утечки ресурсов, все должно закрываться
            if (checkDb != null) {
                checkDb.close();
            }
            return checkDb != null;
        }

        //Метод копирования базы
        private void copyDataBase() throws IOException {
            // Открываем поток для чтения из уже созданной нами БД
            //источник в assets
            InputStream externalDbStream = context.getAssets().open(DB_NAME);

            // Путь к уже созданной пустой базе в андроиде
            String outFileName = DB_PATH + DB_NAME;

            // Теперь создадим поток для записи в эту БД побайтно
            OutputStream localDbStream = new FileOutputStream(outFileName);

            // Собственно копирование
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = externalDbStream.read(buffer)) > 0) {
                localDbStream.write(buffer, 0, bytesRead);
            }
            // Мы будем хорошими мальчиками(девочками) и закроем потоки
            localDbStream.close();
            externalDbStream.close();

        }

        public SQLiteDatabase openDataBase() throws SQLException {
            String path = DB_PATH + DB_NAME;
            if (database == null) {
                createDataBase();
                database = SQLiteDatabase.openDatabase(path, null,
                        SQLiteDatabase.OPEN_READWRITE);
            }
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

}
