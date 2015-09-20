package com.nomapp.nomapp_beta.Categories;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.nomapp.nomapp_beta.AddIngredients.AddIngridientsActivity;
import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by antonid on 20.08.2015.
 */
public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSearch;

   // Integer[] nums = {1,2,3,4,5,6,7,8,9,10,11,12,13};
    GridView categoriesGridView;
    LinearLayout llMain;
    EditText enteredText;
    ImageButton back;

    RecyclerView findedIngsRecycler;
    ArrayList<String> findedIngsArray;
    ArrayList<Integer> IDs;

    FindedIngredientsRecyclerAdapter mAdapter;


    LinearLayout.LayoutParams lParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        //LinearLayout for adding and removing views.
        llMain = (LinearLayout) findViewById(R.id.categoriesLinear);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorMainDark));

        setUpEditText();
        setUpGridView();
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
    public void onClick(View v) {
       /* switch (v.getId()){
            case R.id.btnSearch:
                Intent intent = new Intent(CategoriesActivity.this, SearchIngredientsActivity.class);
                startActivity(intent);
                break;
        }*/
    }

   /* adds GridView,
    * sets its params
    * and onItemClickListener
    */
    private void setUpGridView(){
        lParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.CENTER;


        categoriesGridView = new GridView(this);
        llMain.addView(categoriesGridView, lParams);
       // categoriesGridView = (GridView) findViewById(R.id.gridView);
        categoriesGridView.setAdapter(new CategoriesGVAdapter(this));

        categoriesGridView.setNumColumns(2);
        categoriesGridView.setHorizontalSpacing(5);
        categoriesGridView.setVerticalSpacing(5);

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

    /*
    * Enables search mode
    * (removes GridView with categories
    * , adds RecyclerView for results
    * , make EditText and BackButton visible
     */
    private void setUpSearchMode(){
        llMain.removeView(categoriesGridView);
        categoriesGridView = null;

        findedIngsRecycler = new RecyclerView(this);
        llMain.addView(findedIngsRecycler, lParams);

        setUpRecyclerView();

        enteredText.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        //enteredText.setEnabled(true);
    }

    /*
    * Sets usual mode
    * (remove RecyclerView,
    * add categories,
    * make button and EditText invisible
     */
    private void setNoSearchMode() {
        llMain.removeView(findedIngsRecycler);
        findedIngsRecycler = null;

        setUpGridView();

        enteredText.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
    }

    /*
    * EditText for making queries.
     */
    void setUpEditText() {
        enteredText = (EditText) findViewById(R.id.search_field);

        enteredText.addTextChangedListener(new TextWatcher(){ //Listener invokes when data in ExitText changes.
            @Override
            public void afterTextChanged(Editable s) {
                search(enteredText.getText().toString());
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

    /*
    * Method for searching
    * items in database
    * by current query
     */
    void search(String enteredText){
        findedIngsArray = new ArrayList<>();
        IDs = new ArrayList<>();

        findedIngsArray.clear();
        IDs.clear();

        if (enteredText.length() != 0) {
            Cursor cursor = Database.getDatabase().getIngridients().query(Database.getIngredientsTableName(),
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
                        IDs.add(cursor.getInt(0));
                    }
                } while (cursor.moveToNext());
            }
        }

    }


    /*
    * Sets up RecyclerView with finded items.
    * Add possibility to select items by OnItemTouchListener
    * interface in Adapter.
     */
    void setUpRecyclerView() {
        FindedIngredientsRecyclerAdapter.OnItemTouchListener itemTouchListener = new FindedIngredientsRecyclerAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                Cursor cursor = Database.getDatabase().getIngridients().query(Database.getIngredientsTableName(),
                        new String[]
                                {Database.getIngridientId(), Database.getIngridientName(),
                                        Database.getIngridientIsChecked()},
                        null, null, null, null
                        , null);
                Log.w("MY_LOG", "kek");
                cursor.moveToFirst();
                cursor.moveToPosition(IDs.get(position) - 1);
                int isChecked = cursor.getInt(2);
                if (isChecked == 0) {
                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=1 WHERE _id=" + IDs.get(position) + ";");
                    view.setBackgroundColor(getResources().getColor(R.color.chosenElement));
                    //  ((TextView) view).setTextColor(getResources().getColor(R.color.chosenElement));
                    Log.d("MY_TAG", "Checked position " + IDs.get(position));

                } else {
                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                    Log.d("MY_TAG", "Unchecked position " + IDs.get(position));

                }
                cursor.close();
            }
        };
        mAdapter = new FindedIngredientsRecyclerAdapter(findedIngsArray, IDs, itemTouchListener);  // setting adapter.

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()); //setting layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        findedIngsRecycler.setLayoutManager(layoutManager);
        findedIngsRecycler.setAdapter(mAdapter);

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

}
