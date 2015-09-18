package com.nomapp.nomapp_beta.Database;

import android.app.Application;

/**
 * Created by antonid on 13.07.2015.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Database.initDatabase(this);
    }
}
