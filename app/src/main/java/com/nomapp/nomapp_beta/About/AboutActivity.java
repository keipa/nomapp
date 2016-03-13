package com.nomapp.nomapp_beta.About;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.nomapp.nomapp_beta.CategoriesOfRecipes.CategoriesOfRecipesActivity;
import com.nomapp.nomapp_beta.NavigationDrawer.NavDrawerListAdapter;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.Start.StartActivity;

/**
 * Created by Илья on 13.03.2016.
 */
public class AboutActivity extends AppCompatActivity{

     ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setUpNavigationDraver();
    }

        void setUpNavigationDraver() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.notification));
     //   mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
      //  mDrawerToggle.setDrawerIndicatorEnabled(true);
       // mDrawerLayout.setDrawerListener(mDrawerToggle);


         mDrawerList = (ListView) findViewById(R.id.nav_drawer_list_view);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new NavDrawerListAdapter(this, 3));  // 0 means that we are on the first view
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
        mDrawerLayout.closeDrawers();
        switch (position){
            case 0:
                Intent toStartActivity = new Intent(AboutActivity.this, StartActivity.class);
                startActivity(toStartActivity);
                break;
            case 1:
                Intent toAllRecipes = new Intent(AboutActivity.this, CategoriesOfRecipesActivity.class);
                startActivity(toAllRecipes);
                break;
            default: break;
        }
    }

}
