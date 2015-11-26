
package com.nomapp.nomapp_beta.Steps;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.nomapp.nomapp_beta.AllRecipes.AllRecipesActivity;
import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.NavigationDrawer.NavDrawerListAdapter;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.Start.StartActivity;

public class TabsActivity extends AppCompatActivity {

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;

    private Bundle s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s = savedInstanceState;
        setContentView(R.layout.activity_tabs);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.notification));




        setTitle("");

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mViewPager.getPagerTitleStrip().setDividerColor(R.color.white);
        mViewPager.getPagerTitleStrip().setTextColor(getResources().getColor(R.color.white));
        //mViewPager.getPagerTitleStrip().setIndicatorColor(R.color.white);
        //mViewPager.
        mViewPager.getPagerTitleStrip().getIndicatorColor();

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);

        mDrawerList = (ListView) findViewById(R.id.nav_drawer_list_view);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new NavDrawerListAdapter(this));
        // Click events for Navigation Drawer (now available only on start screen)
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());



        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return CookingStepsRecyclerViewFragment.newInstance(s, position);
            }


            @Override
            public int getCount() {
                Intent intent = getIntent();

                Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                        new String[]
                                {Database.getRecipesNumberOfSteps()},
                        null, null, null, null
                        , null);
                cursor.moveToPosition(intent.getIntExtra("numberOfRecipe", 0) - 1);

                int count = cursor.getInt(0);
                cursor.close();

                return count;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "STEP " + (position + 1);
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {

                switch (page % 5) {
                    case 0:
                        return HeaderDesign.fromColorAndDrawable(
                                getResources().getColor(R.color.colorMain),
                                getResources().getDrawable(R.drawable.image_for_steps1));
                    case 1:
                        return HeaderDesign.fromColorAndDrawable(
                                getResources().getColor(R.color.colorMain),
                                getResources().getDrawable(R.drawable.image_for_steps2));
                    case 2:
                        return HeaderDesign.fromColorAndDrawable(
                                getResources().getColor(R.color.colorMain),
                                getResources().getDrawable(R.drawable.image_for_steps3));
                    case 3:
                        return HeaderDesign.fromColorAndDrawable(
                                getResources().getColor(R.color.colorMain),
                                getResources().getDrawable(R.drawable.image_for_steps4));
                    case 4:
                        return HeaderDesign.fromColorAndDrawable(
                                getResources().getColor(R.color.colorMain),
                                getResources().getDrawable(R.drawable.image_for_steps5));


                }


                //execute others actions if needed (ex : modify your header logo)
                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
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
                mDrawer.closeDrawers();
                Intent toStartActivity = new Intent(TabsActivity.this, StartActivity.class);
                startActivity(toStartActivity);
                break;
            case 1:
                mDrawer.closeDrawers();
                Intent toAllRecipes = new Intent(TabsActivity.this, AllRecipesActivity.class);
                startActivity(toAllRecipes);
                break;
            default: break;
        }
    }
}
