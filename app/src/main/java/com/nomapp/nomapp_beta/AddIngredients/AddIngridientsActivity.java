package com.nomapp.nomapp_beta.AddIngredients;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.nomapp.nomapp_beta.AllRecipes.AllRecipesActivity;
import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.NavigationDrawer.NavDrawerListAdapter;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.Start.StartActivity;


/**
 * Created by antonid on 05.07.2015.
 */

public class AddIngridientsActivity extends AppCompatActivity {
    ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    ActionBar actionBar;

    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        AddIngredientsRecyclerFragment frag = new AddIngredientsRecyclerFragment();
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.add(R.id.add_ings_fragment_cont,frag);
        trans.commit();

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.notification));
        fab = (FloatingActionButton) findViewById(R.id.fabtohome);  //floating action button init
        setUpFAB();
        setUpNavigationDraver();

    }

    View.OnClickListener onCircleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddIngridientsActivity.this, StartActivity.class);   //fab listner to the categories list activity
            startActivity(intent);
        }
    };

    void setUpFAB() {
        fab.setColorNormal(getResources().getColor(R.color.chosenElement));  //normal state color
        fab.setColorPressed(getResources().getColor(R.color.primary)); //pressed state color
        fab.setColorRipple(getResources().getColor(R.color.chosenElement));  //??? color
        fab.setOnClickListener(onCircleButtonClickListener);   //setting listner
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_animation);  //animation init
        fab.startAnimation(hyperspaceJumpAnimation);            //beautiful animation on the start(button appear on from the point)
    }
    @Override
    protected void onStart() {
        super.onStart();
    }


    void setUpNavigationDraver() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
            //   actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.hamburger_icon);
        }


        //Setting title of the toolbar
        Cursor categoryCursor = Database.getDatabase().getGeneralDb().query(Database.getCategoriesTableName(),
                new String[]
                        {Database.getCategoriesId(), Database.getCategoryName(),
                                Database.getCategoryIngredients()},
                null, null, null, null
                , null);

        categoryCursor.moveToFirst();
        Intent intent = getIntent();
        categoryCursor.moveToPosition(intent.getIntExtra("numberOfCategory", 0) - 1);

        String title = categoryCursor.getString(1);
        actionBar.setTitle(title);
        categoryCursor.close();

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
                mDrawerLayout.closeDrawers();
                Intent toStartActivity = new Intent(AddIngridientsActivity.this, StartActivity.class);
                startActivity(toStartActivity);
                break;
            case 1:
                mDrawerLayout.closeDrawers();
                Intent toAllRecipes = new Intent(AddIngridientsActivity.this, AllRecipesActivity.class);
                startActivity(toAllRecipes);
                break;
            default: break;
        }
    }
}
