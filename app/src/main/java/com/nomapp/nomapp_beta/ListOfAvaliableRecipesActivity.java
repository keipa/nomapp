package com.nomapp.nomapp_beta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by antonid on 26.07.2015.
 */
public class ListOfAvaliableRecipesActivity extends Activity {

    private static final String RECIPES_TABLE_NAME = "Recipes";

    ListView availableRecipes;

    ArrayList<String> recipesForList;
    ArrayList<Integer> IDs;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_available_recipes);

        availableRecipes = (ListView) findViewById(R.id.availableRecipes);

    }

    @Override
    protected void onStart(){
        super.onStart();
        fillAvailableRecipes();
        setUpRecipesList();
    }

    private void setUpRecipesList(){
        availableRecipes.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, recipesForList));


        availableRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                cursor.moveToPosition(IDs.get(position) - 1);
                Intent intent = new Intent(ListOfAvaliableRecipesActivity.this, TabsActivity.class);
                intent.putExtra("cooking", cursor.getString(3));
                intent.putExtra("numberOfSteps", cursor.getInt(5));
                startActivity(intent);
                cursor.close();
            }
        });
    }

    private void fillAvailableRecipes() {
        recipesForList = new ArrayList<>();
        IDs = new ArrayList<>();

        cursor = Database.getDatabase().getRecipes().query(RECIPES_TABLE_NAME,
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(), Database.getRecipesIngridients(),
                                Database.getRecipesHowToCook(),Database.getRecipesIsAvailable(),
                                Database.getRecipesNumberOfSteps()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                if (cursor.getInt(4) != 0) {
                    recipesForList.add(cursor.getString(1));
                    IDs.add(cursor.getInt(0));
                }
            } while (cursor.moveToNext());
        }
    }
}
