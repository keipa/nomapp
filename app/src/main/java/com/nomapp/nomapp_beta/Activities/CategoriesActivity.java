package com.nomapp.nomapp_beta.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.nomapp.nomapp_beta.R;

/**
 * Created by antonid on 20.08.2015.
 */
public class CategoriesActivity extends Activity implements View.OnClickListener {



    String[] data = {"Мясо", "Птица", "Рыба", "Морепродукты", "Овощи", "Фрукты и ягоды", "Бакалея", "Крупы", "Молочные продукты", "Грибы", "Зелень", "Орехи", "Готовые продукты"};
    Integer[] nums = {1,2,3,4,5,6,7,8,9,10,11,12,13};
    GridView gvMain;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorMainDark));
        setContentView(R.layout.activity_categories_grid);
        adapter = new ArrayAdapter<String>(this, R.layout.category_item, R.id.textView5, data);
        gvMain = (GridView) findViewById(R.id.gridView);
        gvMain.setAdapter(adapter);
        adjustGridView();

    }


    private void adjustGridView(){
        gvMain.setNumColumns(2);
        gvMain.setHorizontalSpacing(5);
        gvMain.setVerticalSpacing(5);
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

    }
}
