package com.nomapp.nomapp_beta.Categories;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.nomapp.nomapp_beta.AddIngredients.AddIngridientsActivity;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by antonid on 20.08.2015.
 */
public class CategoriesActivity extends AppCompatActivity implements GridViewFragment.CategoriesGridViewOnClickListener {

    EditText enteredText;
    ImageButton back;

    GridViewFragment gridViewFragment;
    SearchFragment searchFragment;
    FragmentTransaction fTransaction;

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
        window.setStatusBarColor(getResources().getColor(R.color.colorMainDark));

        setUpEditText();
        setUpGridViewFragment();
        setUpBackButton();
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
        if (id == R.id.action_settings) {
            return true;
        }

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

        enteredText.addTextChangedListener(new TextWatcher(){ //Listener invokes when data in ExitText changes.
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
}
