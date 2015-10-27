
package com.nomapp.nomapp_beta.Steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.nomapp.nomapp_beta.R;

public class TabsActivity extends AppCompatActivity {

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
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

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return CookingStepsRecyclerViewFragment.newInstance(s, position);
            }


            @Override
            public int getCount() {
                Intent intent = getIntent();
                int count = intent.getIntExtra("numberOfSteps", 0);
                return count;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "Step " + position;
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
}
