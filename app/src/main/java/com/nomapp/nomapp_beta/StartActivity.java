package com.nomapp.nomapp_beta;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.melnykov.fab.FloatingActionButton;
import java.lang.reflect.Array;
import java.util.ArrayList;

//import com.nomapp.nomapp_beta.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity implements View.OnClickListener {

    private static final String TABLE_NAME = "Ingridients";
    private static final String RECIPES_TABLE_NAME = "Recipes";

    RecyclerView selectedIngridients;
    SwipeableRecyclerViewTouchListener swipeTouchListener;

    Button showAvailableRecipes;
    FloatingActionButton fab;

    ArrayList<String> forSelectedIngridients;
    ArrayList<String> ingridientsForRecipe;
    ArrayList<Integer> IDs;

    ArrayList<ArrayList<Integer>> convertedIngrodientsForRecipe;

    int nubmerOfAvailableRecipes;

    private CardViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        nubmerOfAvailableRecipes = 0;

        selectedIngridients = (RecyclerView) findViewById(R.id.recycler_view);

        showAvailableRecipes = (Button) findViewById(R.id.showAvailableRecipes);
        showAvailableRecipes.setOnClickListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        //    setUpFAB();
    }


    @Override
    protected void onStart() {
        super.onStart();
        showNumberOfAvailableRecipes();
        fillSelectedIngridients();
        setUpList();
        setUpFAB();
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

        switch (v.getId()) {
            case R.id.showAvailableRecipes:
                Intent intent = new Intent(StartActivity.this, ListOfAvaliableRecipesActivity.class);
                startActivity(intent);
                break;
            default:

                break;
        }
    }

    View.OnClickListener onCircleButtonCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StartActivity.this, AddIngridientsActivity.class);
            startActivity(intent);
        }
    };

    void setUpFAB(){
        fab.attachToRecyclerView(selectedIngridients);
        fab.setColorNormal(getResources().getColor(R.color.primaryDark));
        fab.setColorPressed(getResources().getColor(R.color.primary));
        fab.setColorRipple(getResources().getColor(R.color.primaryDark));
        fab.setOnClickListener(onCircleButtonCliclListener);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_animation);
        fab.startAnimation(hyperspaceJumpAnimation);
    }

    void setUpList() {
        CardViewAdapter.OnItemTouchListener itemTouchListener = new CardViewAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                Toast.makeText(StartActivity.this, "Tapped " + forSelectedIngridients.get(position), Toast.LENGTH_SHORT).show();
            }
        };

        mAdapter = new CardViewAdapter(forSelectedIngridients, itemTouchListener);

        selectedIngridients.setLayoutManager(new LinearLayoutManager(this));
        selectedIngridients.setAdapter(mAdapter);
        setSwipeTouchListener();
    }

    private void fillSelectedIngridients() {
        forSelectedIngridients = new ArrayList<>();
        IDs = new ArrayList<>();
        Cursor cursor = Database.getDatabase().getIngridients().query(TABLE_NAME,
                new String[]
                        {Database.getIngridientId(), Database.getIngridientName(),
                                Database.getIngridientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                if (cursor.getInt(2) != 0) {
                    forSelectedIngridients.add(cursor.getString(1));
                    IDs.add(cursor.getInt(0));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void showNumberOfAvailableRecipes() {
        nubmerOfAvailableRecipes = 0;
        fillIngridientsForRecipe();

        int size = ingridientsForRecipe.size();

        convertedIngrodientsForRecipe = new ArrayList<ArrayList<Integer>>();
        for (int counter = 0; counter < size; counter++) {
            convertedIngrodientsForRecipe.add(convertIngridientsToArrayList(ingridientsForRecipe.get(counter)));
        }

        checking();
        showAvailableRecipes.setText(nubmerOfAvailableRecipes + " recipes available");
    }

    private void checking() {
        boolean isRecipeAvailable = true;

        Cursor cursor = Database.getDatabase().getIngridients().query(TABLE_NAME,
                new String[]
                        {Database.getIngridientId(), Database.getIngridientName(),
                                Database.getIngridientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();

        int numberOfRecipes = convertedIngrodientsForRecipe.size();
        for (int currentRecipe = 0; currentRecipe < numberOfRecipes; currentRecipe++) {
            int numberOfIngridientsInRecipe = convertedIngrodientsForRecipe.get(currentRecipe).size();
            for (int ingridientNumber = 0; ingridientNumber < numberOfIngridientsInRecipe; ingridientNumber++) {
                cursor.moveToPosition(convertedIngrodientsForRecipe.get(currentRecipe).get(ingridientNumber) - 1);
                if (cursor.getInt(2) != 1) {
                    isRecipeAvailable = false;
                    break;
                }
            }

            if (isRecipeAvailable == true) {
                Database.getDatabase().getRecipes().execSQL("UPDATE Recipes SET isAvailable=1 WHERE _id=" + (currentRecipe + 1) + ";");
                nubmerOfAvailableRecipes++;
            } else {
                Database.getDatabase().getRecipes().execSQL("UPDATE Recipes SET isAvailable=0 WHERE _id=" + (currentRecipe + 1) + ";");
            }
            isRecipeAvailable = true;
        }
        cursor.close();
    }

    private void fillIngridientsForRecipe() {
        ingridientsForRecipe = new ArrayList<String>();
        Cursor cursor = Database.getDatabase().getRecipes().query(RECIPES_TABLE_NAME,
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(), Database.getRecipesIngridients(),
                                Database.getRecipesHowToCook(), Database.getRecipesIsAvailable()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                if (cursor.getInt(2) != 0) {
                    ingridientsForRecipe.add(cursor.getString(2));
                    Log.w("MY_TAG", ingridientsForRecipe.get(cursor.getPosition()));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private ArrayList<Integer> convertIngridientsToArrayList(String toConvert) {
        ArrayList<Integer> converted = new ArrayList<Integer>();

        int counter = 0;
        int factor = 1;
        int currentIngridient = 0;
        int size = toConvert.length();
        for (counter = 0; counter < size; counter++) {
            while (toConvert.charAt(counter) != ',' && toConvert.charAt(counter) != '.') {//TODO
                currentIngridient += (toConvert.charAt(counter) - '0') * factor;
                factor *= 10;
                counter++;
            }
            factor = 1;
            converted.add(currentIngridient);
            currentIngridient = 0;
        }
        return converted;
    }

    void setSwipeTouchListener(){
        swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(selectedIngridients,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                                    forSelectedIngridients.remove(position);
                                    IDs.remove(position);
                                    showNumberOfAvailableRecipes();
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                                    forSelectedIngridients.remove(position);
                                    IDs.remove(position);
                                    showNumberOfAvailableRecipes();
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        selectedIngridients.addOnItemTouchListener(swipeTouchListener);

    }


}