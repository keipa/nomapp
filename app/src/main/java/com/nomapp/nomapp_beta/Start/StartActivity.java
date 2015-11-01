package com.nomapp.nomapp_beta.Start;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
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
import com.nomapp.nomapp_beta.AllRecipes.AllRecipesActivity;
import com.nomapp.nomapp_beta.AvailableRecipes.ListOfAvailableRecipesActivity;
import com.nomapp.nomapp_beta.Categories.CategoriesActivity;
import com.nomapp.nomapp_beta.NavigationDrawer.NavDrawerListAdapter;
import com.nomapp.nomapp_beta.R;


public class StartActivity extends AppCompatActivity implements StartFragment.StartFragmentEventsListener {

    ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    FloatingActionButton fab;

    StartFragment startFragment;
    EmptyImgStartFragment imgFragment;
    FragmentTransaction fTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);    //first screen activation

        startFragment = new StartFragment();
        imgFragment = new EmptyImgStartFragment();

        fTransaction = getFragmentManager().beginTransaction();
        fTransaction.add(R.id.startFragmentContainer, imgFragment);
        fTransaction.commit();

        fab = (FloatingActionButton) findViewById(R.id.fab);  //floating action button init

        setUpNavigationDraver();        //navigation drawer initiation


    }

    @Override
    protected void onStart() {
        super.onStart();

        // setUpUserSettings();     //this function initiate settings

        startFragment.numberOfSelectedIngredients = startFragment.fillSelectedIngridients();
        if (startFragment.numberOfSelectedIngredients != 0) {
            fTransaction = getFragmentManager().beginTransaction();
            fTransaction.replace(R.id.startFragmentContainer, startFragment);
            fTransaction.commit();
        } else {
            fTransaction = getFragmentManager().beginTransaction();
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

    void setUpUserSettings() {
        setUpLocalization();
    }

    void setUpLocalization() {          //language change
        /*SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = settings.getString("language", "");

        TextView fridgeText = (TextView) findViewById(R.id.addFridge);
        TextView dishOfADayText = (TextView) findViewById(R.id.addDishOfADay);
        TextView allRecipesText = (TextView) findViewById(R.id.addAllRecepies);
        TextView settingsText = (TextView) findViewById(R.id.addSettings);
        TextView leaveFeedbackText = (TextView) findViewById(R.id.addReply);
        TextView aboutText = (TextView) findViewById(R.id.addHelp);

        switch (lang){
            case "1":
                fridgeText.setText(getString(R.string.fridge_en));
                dishOfADayText.setText(getString(R.string.dish_of_a_day_en));
                allRecipesText.setText(getString(R.string.all_recipes_en));
                settingsText.setText(getString(R.string.settings_en));
                leaveFeedbackText.setText(getString(R.string.leave_feedback_en));
                aboutText.setText(getString(R.string.about_en));
                break;

            case "2":
                fridgeText.setText(getString(R.string.fridge_ru));
                dishOfADayText.setText(getString(R.string.dish_of_a_day_ru));
                allRecipesText.setText(getString(R.string.all_recipes_ru));
                settingsText.setText(getString(R.string.settings_ru));
                leaveFeedbackText.setText(getString(R.string.leave_feedback_ru));
                aboutText.setText(getString(R.string.about_ru));
                break;

            default:
                break;
        }
        Log.w("MY_TAG", lang);// log note*/
    }

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
         //   actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.hamburger_icon);
        }

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.notification));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


         mDrawerList = (ListView) findViewById(R.id.nav_drawer_list_view);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new NavDrawerListAdapter(this));
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
        fTransaction = getFragmentManager().beginTransaction();
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
        switch (position){
            case 0: break;
            case 1:
                mDrawerLayout.closeDrawers();
                Intent toAllRecipes = new Intent(StartActivity.this, AllRecipesActivity.class);
                startActivity(toAllRecipes);
                break;
            default: break;
        }
    }
}