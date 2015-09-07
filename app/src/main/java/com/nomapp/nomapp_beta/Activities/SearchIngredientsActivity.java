package com.nomapp.nomapp_beta.Activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.nomapp.nomapp_beta.Database;
import com.nomapp.nomapp_beta.FindedIngredientsRecyclerAdapter;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by Антоненко Илья on 06.09.2015.
 */
public class SearchIngredientsActivity extends Activity {
    private static final String TABLE_NAME = "Ingridients";

    RecyclerView searchedIngredientsRecycler;
    ArrayList<String> findedIngsArray;

    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ingredients);    //first screen activation
        searchedIngredientsRecycler = (RecyclerView) findViewById(R.id.findedIngsRecycler);
        searchedIngredientsRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpEditText(); //Setting listener and ect.
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

    void setUpEditText() {
        search = (EditText) findViewById(R.id.findIngsET);
        search.addTextChangedListener(new TextWatcher(){ //Listener invokes when data in ExitText changes.
            @Override
            public void afterTextChanged(Editable s) {
                fillArrayList(search.getText().toString());
                setUpRecyclerView();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }


    void setUpRecyclerView() { //RecyclerView settings
        searchedIngredientsRecycler.setHasFixedSize(false);
        searchedIngredientsRecycler.setAdapter(new FindedIngredientsRecyclerAdapter(this, findedIngsArray));
    }


    //We clear our list of ingredients when data in EditText changed.
    //Then we find needed strings in database by means of func 'contains()'
    //I use 'contains()' and 'toLowerCase.contains()' to give to the user
    //ability to begin input with a small letter
    void fillArrayList(String enteredText){
        findedIngsArray = new ArrayList<>();
        findedIngsArray.clear();
        if (enteredText.length() != 0) {
            Cursor cursor = Database.getDatabase().getIngridients().query(TABLE_NAME,
                    new String[]
                            {Database.getIngridientId(), Database.getIngridientName(),
                                    Database.getIngridientIsChecked()},
                    null, null, null, null
                    , null);

            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {
                do {
                    String name = cursor.getString(1);
                    if (name.toLowerCase().contains(enteredText) || name.contains(enteredText)) {
                        findedIngsArray.add(name);
                    }
                } while (cursor.moveToNext());
            }
        }

    }
}
