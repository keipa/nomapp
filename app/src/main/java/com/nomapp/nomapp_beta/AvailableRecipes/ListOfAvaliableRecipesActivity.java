package com.nomapp.nomapp_beta.AvailableRecipes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.RecipePreview.RecipePreviewActivity;

import java.util.ArrayList;

/**
 * Created by antonid on 26.07.2015.
 */
public class ListOfAvaliableRecipesActivity extends Activity {

    private static final String RECIPES_TABLE_NAME = "Recipes";

    RecyclerView availableRecipes;
    AvlReciepesRecyclerViewAdapter mAdapter;

    ArrayList<String> availableRecipesArrayList;
    ArrayList<Integer> timeForCooking;
    ArrayList<Integer> numberOfSteps;
    ArrayList<Integer> IDs;
    ArrayList<Integer> numberOfIngs;

    Cursor cursor;

    //TES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_available_recipes);

        availableRecipes = (RecyclerView) findViewById(R.id.availableReciepesLW);


        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorMainDark));
    }

    @Override
    protected void onStart(){
        super.onStart();
        fillAvailableRecipes();
        setUpRecipesList();
    }

    private void setUpRecipesList(){

        AvlReciepesRecyclerViewAdapter.OnItemTouchListener itemTouchListener = new AvlReciepesRecyclerViewAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                cursor.moveToPosition(IDs.get(position) - 1);

                Intent intent = new Intent(ListOfAvaliableRecipesActivity.this, RecipePreviewActivity.class);
                intent.putExtra("numberOfRecipe", IDs.get(position) - 1);
                intent.putExtra("cooking", cursor.getString(3));
                intent.putExtra("numberOfSteps", cursor.getInt(5));
                intent.putExtra("nameOfRecipe", cursor.getString(1));
                startActivity(intent);
                cursor.close();
            }
        };

        mAdapter = new AvlReciepesRecyclerViewAdapter(availableRecipesArrayList, timeForCooking, numberOfSteps, numberOfIngs, itemTouchListener);  // setting adapter.

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()); //setting layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        availableRecipes.setLayoutManager(layoutManager);
        availableRecipes.setAdapter(mAdapter);



    }

    private void fillAvailableRecipes() {
        availableRecipesArrayList = new ArrayList<>();
        IDs = new ArrayList<>();
        timeForCooking = new ArrayList<>();
        numberOfSteps = new ArrayList<>();
        numberOfIngs = new ArrayList<>();

        cursor = Database.getDatabase().getGeneralDb().query(RECIPES_TABLE_NAME,
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(), Database.getRecipesIngredients(),
                        Database.getRecipesHowToCook(),Database.getRecipesIsAvailable(),
                        Database.getRecipesNumberOfSteps(), Database.getRecipesTimeForCooking(),
                        Database.getRecipesDescription(), Database.getRecipesNumberOfPersons(),
                        Database.getRecipesNumberOfEveryIng(), Database.getRecipesNumberOfSteps()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                if (cursor.getInt(4) != 0) {
                    availableRecipesArrayList.add(cursor.getString(1));
                    IDs.add(cursor.getInt(0));
                    timeForCooking.add(cursor.getInt(6));
                    numberOfSteps.add(cursor.getInt(5));
                    numberOfIngs.add(cursor.getInt(10));
                }
            } while (cursor.moveToNext());
        }
    }
}
