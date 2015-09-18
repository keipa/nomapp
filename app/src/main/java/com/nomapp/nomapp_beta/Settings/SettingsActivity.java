package com.nomapp.nomapp_beta.Settings;

/**
 * Created by antonid on 04.08.2015.
 */
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.nomapp.nomapp_beta.R;

public class SettingsActivity extends PreferenceActivity {

    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        // Get the custom preference
    }
}