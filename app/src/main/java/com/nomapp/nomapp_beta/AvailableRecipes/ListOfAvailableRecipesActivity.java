package com.nomapp.nomapp_beta.AvailableRecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nomapp.nomapp_beta.AllRecipes.AllRecipesActivity;import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.NavigationDrawer.NavDrawerListAdapter;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.Start.StartActivity;



/**
 * Created by antonid on 26.07.2015.
 */
public class ListOfAvailableRecipesActivity extends AppCompatActivity {
    ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_available_recipes);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.notification));
        setUpNavigationDraver();
    }

    void setUpNavigationDraver() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.notification));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        mDrawerList = (ListView) findViewById(R.id.nav_drawer_list_view);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new NavDrawerListAdapter(this));
        // Click events for Navigation Drawer (now available only on start screen)
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            navDrawerSelectItem(i);
        }
    }
    //
    private void navDrawerSelectItem(int position){
        switch (position){
            case 0:
                Intent toStartActivity = new Intent(ListOfAvailableRecipesActivity.this, StartActivity.class);
                startActivity(toStartActivity);
                break;
            case 1:
                Intent toAllRecipes = new Intent(ListOfAvailableRecipesActivity.this, AllRecipesActivity.class);
                startActivity(toAllRecipes);
                break;
            default: break;
        }
    }

}
