package com.nomapp.nomapp_beta.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.nomapp.nomapp_beta.R;

/**
 * Created by antonid on 20.08.2015.
 */
public class CategoriesActivity extends Activity implements View.OnClickListener {
    Button category1;
    Button category2;
    Button category3;
    Button category4;
    Button category5;
    Button category6;
    Button category7;
    Button category8;
    Button category9;
    Button category10;
    Button category11;
    Button category12;
    Button category13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorMainDark));

        category1 = (Button) findViewById(R.id.button);
        category2 = (Button) findViewById(R.id.button2);
        category3 = (Button) findViewById(R.id.button3);
        category4 = (Button) findViewById(R.id.button4);
        category5 = (Button) findViewById(R.id.button5);
        category6 = (Button) findViewById(R.id.button6);
        category7 = (Button) findViewById(R.id.button7);
        category8 = (Button) findViewById(R.id.button8);
        category9 = (Button) findViewById(R.id.button9);
        category10 = (Button) findViewById(R.id.button10);
        category11 = (Button) findViewById(R.id.button11);
        category12 = (Button) findViewById(R.id.button12);
        category13 = (Button) findViewById(R.id.button13);

        category1.setOnClickListener(this);
        category2.setOnClickListener(this);
        category3.setOnClickListener(this);
        category4.setOnClickListener(this);
        category5.setOnClickListener(this);
        category6.setOnClickListener(this);
        category7.setOnClickListener(this);
        category8.setOnClickListener(this);
        category9.setOnClickListener(this);
        category10.setOnClickListener(this);
        category11.setOnClickListener(this);
        category12.setOnClickListener(this);
        category13.setOnClickListener(this);
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
        Intent intent;
        switch (v.getId()){
            case R.id.button:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 1);
                startActivity(intent);
                break;
            case R.id.button2:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 2);
                startActivity(intent);
                break;
            case R.id.button3:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 3);
                startActivity(intent);
                break;
            case R.id.button4:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 4);
                startActivity(intent);
                break;
            case R.id.button5:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 5);
                startActivity(intent);
                break;
            case R.id.button6:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 6);
                startActivity(intent);
                break;
            case R.id.button7:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 7);
                startActivity(intent);
                break;
            case R.id.button8:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 8);
                startActivity(intent);
                break;
            case R.id.button9:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 9);
                startActivity(intent);
                break;
            case R.id.button10:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 10);
                startActivity(intent);
                break;
            case R.id.button11:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 11);
                startActivity(intent);
                break;
            case R.id.button12:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 12);
                startActivity(intent);
                break;
            case R.id.button13:
                intent = new Intent(CategoriesActivity.this, AddIngridientsActivity.class);
                intent.putExtra("numberOfCategory", 13);
                startActivity(intent);
                break;

        }
    }
}
