package com.nomapp.nomapp_beta;

import android.app.Application;
import android.provider.ContactsContract;

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
