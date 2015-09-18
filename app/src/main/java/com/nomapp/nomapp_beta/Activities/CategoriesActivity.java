package com.nomapp.nomapp_beta.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.nomapp.nomapp_beta.CategoriesGVAdapter;
import com.nomapp.nomapp_beta.R;

/**
 * Created by antonid on 20.08.2015.
 */
public class CategoriesActivity extends Activity implements View.OnClickListener {
    Button btnSearch;

   // Integer[] nums = {1,2,3,4,5,6,7,8,9,10,11,12,13};
    GridView categoriesGridView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorMainDark));

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        //adapter = new ArrayAdapter<String>(this, R.layout.category_item, R.id.textView5, data);
        //adapter = new CategoriesGVAdapter(this);
        categoriesGridView = (GridView) findViewById(R.id.gridView);
        categoriesGridView.setAdapter(new CategoriesGVAdapter(this));
        adjustGridView();
        categoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.w("MY_TAG","gridItem clicked");
                Intent toIngs = new Intent (CategoriesActivity.this, AddIngridientsActivity.class);
                toIngs.putExtra("numberOfCategory", position + 1);
                startActivity(toIngs);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSearch:
                Intent intent = new Intent(CategoriesActivity.this, SearchIngredientsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void adjustGridView(){
        categoriesGridView.setNumColumns(2);
        categoriesGridView.setHorizontalSpacing(5);
        categoriesGridView.setVerticalSpacing(5);
    }

}
