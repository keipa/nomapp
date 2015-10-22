package com.nomapp.nomapp_beta.AllRecipes;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.nomapp.nomapp_beta.R;

/**
 * Created by antonid on 26.07.2015.
 */
public class AllRecipesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recipes);


        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.notification));
    }

}
