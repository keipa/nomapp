package com.nomapp.nomapp_beta.Start;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.nomapp.nomapp_beta.About.AboutActivity;
import com.nomapp.nomapp_beta.Alarms.AlarmReceiver;
import com.nomapp.nomapp_beta.AvailableRecipes.ListOfAvailableRecipesActivity;
import com.nomapp.nomapp_beta.CategoriesOfIngredients.CategoriesActivity;
import com.nomapp.nomapp_beta.CategoriesOfRecipes.CategoriesOfRecipesActivity;
import com.nomapp.nomapp_beta.NavigationDrawer.NavDrawerListAdapter;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.UserGuide.HelpDialog;

import java.util.Calendar;


public class StartActivity extends AppCompatActivity implements StartFragment.StartFragmentEventsListener {

    ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    FloatingActionButton fab;

    StartFragment startFragment;
    EmptyImgStartFragment imgFragment;
    FragmentTransaction fTransaction;

    AlarmManager alarmManager;
    private PendingIntent alarmIntent;

   DialogFragment helpDialog;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
        //finish();
       // System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);    //first screen activation

        startFragment = new StartFragment();
        imgFragment = new EmptyImgStartFragment();

        fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.add(R.id.startFragmentContainer, imgFragment);
        fTransaction.commit();

        fab = (FloatingActionButton) findViewById(R.id.fab);  //floating action button init

        setUpNavigationDraver();        //navigation drawer initiation

        showHelp();
    }

    @Override
    protected void onStart() {
        super.onStart();

            restartNotify();
        // setUpUserSettings();     //this function initiate settings

        startFragment.numberOfSelectedIngredients = startFragment.fillSelectedIngridients();
        if (startFragment.numberOfSelectedIngredients != 0) {
            fTransaction = getSupportFragmentManager().beginTransaction();
            fTransaction.replace(R.id.startFragmentContainer, startFragment);
            fTransaction.commit();
        } else {
            fTransaction = getSupportFragmentManager().beginTransaction();
            fTransaction.replace(R.id.startFragmentContainer, imgFragment);
            fTransaction.commit();
        }

        setUpFAB();             // this function sets up floating action button
    }


    View.OnClickListener onCircleButtonCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StartActivity.this, CategoriesActivity.class);   //fab listner to the categories list activity
            startActivity(intent);
        }
    };


    void setUpNavigationDraver() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            setTitle(getResources().getString(R.string.main_activity_title));
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.notification));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


         mDrawerList = (ListView) findViewById(R.id.nav_drawer_list_view);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new NavDrawerListAdapter(this, 0));  // 0 means that we are on the first view
        // Click events for Navigation Drawer (now available only on start screen)

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    void setUpFAB() {
        fab.setColorNormal(getResources().getColor(R.color.chosenElement));  //normal state color
        fab.setColorPressed(getResources().getColor(R.color.primary)); //pressed state color
        fab.setColorRipple(getResources().getColor(R.color.chosenElement));  //??? color
        fab.setOnClickListener(onCircleButtonCliclListener);   //setting listner
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_animation);  //animation init
        fab.startAnimation(hyperspaceJumpAnimation);            //beautiful animation on the start(button appear on from the point)
    }


    @Override
    public void onImgBtnClick() {
        Intent intent = new Intent(StartActivity.this, ListOfAvailableRecipesActivity.class);  //listner to the recipe list activity
        startActivity(intent);
    }

    @Override
    public void onFridgeEmpty() {
        fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.replace(R.id.startFragmentContainer, imgFragment);
        fTransaction.commit();
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
                break;
            case 1:
                Intent toAllRecipes = new Intent(StartActivity.this, CategoriesOfRecipesActivity.class);
                startActivity(toAllRecipes);
                break;
            case 3:
                Intent toAbout = new Intent(StartActivity.this, AboutActivity.class);
                startActivity(toAbout);
                break;
            default: break;
        }
    }

    private void restartNotify() {
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(StartActivity.this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(StartActivity.this, 1, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 19);
//        calendar.set(Calendar.MINUTE, 00);
        long timeToStart = calendar.getTimeInMillis();

        alarmManager.cancel(alarmIntent);
       // Log.d("Time", "Time to start: " + timeToStart);
        //Log.d("Time", "Current time: " + System.currentTimeMillis());
        if(System.currentTimeMillis() > timeToStart){
            timeToStart += 24 * 60 * 60 * 1000; // one day
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeToStart, AlarmManager.INTERVAL_DAY * 2 , alarmIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToStart, alarmIntent);
    }

    public void showHelp(){
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        if (prefs.getBoolean("isFirstRun", true)){
            Bundle bundle = new Bundle();
            bundle.putInt("message", R.string.main_activity_help_msg);

            helpDialog = new HelpDialog();
            helpDialog.setArguments(bundle);
            helpDialog.show(getSupportFragmentManager(), "helpDialog");

            prefs.edit().putBoolean("isFirstRun", false).apply();
        }
    }
}