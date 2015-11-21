package com.nomapp.nomapp_beta.AllRecipes;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.RecipePreview.RecipePreviewActivity;

import java.util.ArrayList;

/**
 * Created by antonid on 02.10.2015.
 */
public class AllRecipesRecyclerFragment extends Fragment {
    RecyclerView avlRecipesRecycler;
    AllRecipesRecyclerAdapter mAdapter;

    ArrayList<String> availableRecipesArrayList;
    ArrayList<String> timeForCooking;
    ArrayList<Integer> numberOfSteps;
    ArrayList<Integer> IDs;
    ArrayList<Integer> numberOfIngs;

    Cursor cursor;
    Cursor categoryCursor;

    String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_avl_recipes, null);

        avlRecipesRecycler = (RecyclerView) v.findViewById(R.id.availableReciepesLW);


        return v;
    }
    public void onStart() {
        super.onStart();
        fillAvailableRecipes();
        setUpRecyclerView();
    }


    private void setUpRecyclerView(){
        AllRecipesRecyclerAdapter.OnItemTouchListener itemTouchListener = new AllRecipesRecyclerAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                if (cursor.isClosed())
                {
                    cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                            new String[]
                                    {Database.getRecipesId(), Database.getRecipesName(),
                                            Database.getRecipesIsAvailable(), Database.getRecipesNumberOfSteps(),
                                            Database.getRecipesTimeForCooking(), Database.getRecipesNumberOfIngredients(),
                                            Database.getRecipesHowToCook()},
                            null, null, null, null
                            , null);
                }
                cursor.moveToPosition(IDs.get(position) - 1);

                Intent intent = new Intent(getActivity(), RecipePreviewActivity.class);
                intent.putExtra("numberOfRecipe", cursor.getInt(0));
                intent.putExtra("nameOfRecipe", cursor.getString(1));
                cursor.close();
                startActivity(intent);
            }
        };

        mAdapter = new AllRecipesRecyclerAdapter(IDs, itemTouchListener);  // setting adapter.

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()); //setting layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        avlRecipesRecycler.setLayoutManager(layoutManager);
        avlRecipesRecycler.setAdapter(mAdapter);

    }

    private void fillAvailableRecipes() {
        availableRecipesArrayList = new ArrayList<>();
        IDs = new ArrayList<>();
        timeForCooking = new ArrayList<>();
        numberOfSteps = new ArrayList<>();
        numberOfIngs = new ArrayList<>();

        cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(),
                                Database.getRecipesIsAvailable(), Database.getRecipesNumberOfSteps(),
                                Database.getRecipesTimeForCooking(), Database.getRecipesNumberOfIngredients(),
                                Database.getRecipesHowToCook()},
                null, null, null, null
                , null);

       categoryCursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesCategoriesTableName(),
                new String[]
                        {Database.getRecipesCategoriesId(), Database.getRecipesCategoriesName(),
                                Database.getRecipesCategoriesRecipes()},
                null, null, null, null
                , null);

        categoryCursor.moveToFirst();
        Intent intent = getActivity().getIntent();
        categoryCursor.moveToPosition(intent.getIntExtra("numberOfCategory", 0) - 1);

        title = categoryCursor.getString(1);

        IDs = parse(categoryCursor.getString(2));
        categoryCursor.close();


    }

    private ArrayList<Integer> parse(String toConvert) {
        ArrayList<Integer> converted = new ArrayList<>();

        int counter;
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


}
