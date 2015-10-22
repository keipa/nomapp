package com.nomapp.nomapp_beta.AvailableRecipes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.RecipePreview.RecipePreviewActivity;

import java.util.ArrayList;

/**
 * Created by antonid on 26.07.2015.
 */
public class ListOfAvaliableRecipesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_available_recipes);


        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.notification));
    }

}
