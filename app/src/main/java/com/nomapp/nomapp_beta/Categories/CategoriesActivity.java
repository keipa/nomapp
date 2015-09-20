package com.nomapp.nomapp_beta.Categories;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.nomapp.nomapp_beta.AddIngredients.AddIngridientsActivity;
import com.nomapp.nomapp_beta.FindIngredients.SearchIngredientsActivity;
import com.nomapp.nomapp_beta.R;

/**
 * Created by antonid on 20.08.2015.
 */
public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSearch;

   // Integer[] nums = {1,2,3,4,5,6,7,8,9,10,11,12,13};
    GridView categoriesGridView;
    LinearLayout llMain;
    LinearLayout.LayoutParams lParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        llMain = (LinearLayout) findViewById(R.id.categoriesLinear);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorMainDark));

        setUpGridView();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dynamics_search, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

       /* if (id == R.id.action_search){
            Log.w("LOG", "search pressed");
            setUpSearchMode();

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()){
            case R.id.btnSearch:
                Intent intent = new Intent(CategoriesActivity.this, SearchIngredientsActivity.class);
                startActivity(intent);
                break;
        }*/
    }

    private void adjustGridView(){
        categoriesGridView.setNumColumns(2);
        categoriesGridView.setHorizontalSpacing(5);
        categoriesGridView.setVerticalSpacing(5);
    }

    private void setUpGridView(){

        lParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.CENTER;


        categoriesGridView = new GridView(this);
        llMain.addView(categoriesGridView, lParams);
       // categoriesGridView = (GridView) findViewById(R.id.gridView);
        categoriesGridView.setAdapter(new CategoriesGVAdapter(this));
        adjustGridView();
        categoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.w("MY_TAG", "gridItem clicked");
                Intent toIngs = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                toIngs.putExtra("numberOfCategory", position + 1);
                startActivity(toIngs);
            }
        });
    }

    private void setUpSearchMode(){
        llMain.removeView(categoriesGridView);
        categoriesGridView = null;

    }

}
