package com.nomapp.nomapp_beta.CategoriesOfIngredients;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.nomapp.nomapp_beta.AddIngredients.AddIngridientsActivity;
import com.nomapp.nomapp_beta.AllRecipes.AllRecipesActivity;
import com.nomapp.nomapp_beta.NavigationDrawer.NavDrawerListAdapter;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.Start.StartActivity;

/**
 * Created by antonid on 20.08.2015.
 */
public class CategoriesActivity extends AppCompatActivity implements GridViewFragment.CategoriesGridViewOnClickListener {

    EditText enteredText;
    ImageButton back;

    GridViewFragment gridViewFragment;
    SearchFragment searchFragment;
    FragmentTransaction fTransaction;


    ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;

    ActionBar actionBar;


    boolean searchMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        gridViewFragment = new GridViewFragment();
        searchFragment = new SearchFragment();

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.notification));

        setUpEditText();
        setUpGridViewFragment();
        setUpBackButton();
        setUpNavigationDraver();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dynamics_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

       if (id == R.id.action_search){
            Log.w("LOG", "search pressed");
            setUpSearchMode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Auto-generated method stub
        // super.onBackPressed();
        if (searchMode)
            setNoSearchMode();
        else
            super.onBackPressed();
    }


   /* adds GridView,
    * sets its params
    * and onItemClickListener
    */
    private void setUpGridViewFragment(){
        fTransaction = getFragmentManager().beginTransaction();
        fTransaction.add(R.id.categoriesFragmentCont, gridViewFragment);
        fTransaction.commit();
    }

    /*
    * Enables search mode
    * (removes GridView with categories
    * , adds RecyclerView for results
    * , make EditText and BackButton visible
     */
    private void setUpSearchMode(){
        actionBar.setDisplayHomeAsUpEnabled(false);
        fTransaction = getFragmentManager().beginTransaction();
        fTransaction.replace(R.id.categoriesFragmentCont, searchFragment);
        fTransaction.commit();


        enteredText.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        back.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, 0);

        searchMode = true;
        //enteredText.setEnabled(true);
    }

    /*
    * Sets usual mode
    * (remove RecyclerView,
    * add categories,
    * make button and EditText invisible
     */
    private void setNoSearchMode() {
        actionBar.setDisplayHomeAsUpEnabled(true);
        fTransaction = getFragmentManager().beginTransaction();
        fTransaction.replace(R.id.categoriesFragmentCont, gridViewFragment);
        fTransaction.commit();


        enteredText.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(back.getWindowToken(), 0);
        imm = null;

        searchMode = false;
    }

    /*
    * EditText for making queries.
     */
    void setUpEditText() {
        enteredText = (EditText) findViewById(R.id.search_field);

        enteredText.addTextChangedListener(new TextWatcher() { //Listener invokes when data in ExitText changes.
            @Override
            public void afterTextChanged(Editable s) {
                searchFragment.search(enteredText.getText().toString());
                //    setUpRecyclerView(); TODO
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }



    void setUpBackButton(){
        back = (ImageButton) findViewById(R.id.image_button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNoSearchMode();
            }
        });
    }

    @Override
    public void onClick(int position) {
        Log.w("MY_TAG", "gridItem clicked");
        Intent toIngs = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
        toIngs.putExtra("numberOfCategory", position + 1);
        startActivity(toIngs);
    }

    void setUpNavigationDraver() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
            //   actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

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
                Intent toStartActivity = new Intent(CategoriesActivity.this, StartActivity.class);
                startActivity(toStartActivity);
                break;
            case 1:
                mDrawerLayout.closeDrawers();
                Intent toAllRecipes = new Intent(CategoriesActivity.this, AllRecipesActivity.class);
                startActivity(toAllRecipes);
                break;
            default: break;
        }
    }
}
