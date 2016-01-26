package com.nomapp.nomapp_beta.AvailableRecipes;

import android.app.Activity;
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
public class AvlRecipesRecyclerFragment extends Fragment {
    RecyclerView avlRecipesRecycler;
    AvlReciepesRecyclerViewAdapter mAdapter;

    ArrayList<String> availableRecipesArrayList;
    ArrayList<Integer> timeForCooking;
    ArrayList<Integer> numberOfSteps;
    ArrayList<Integer> IDs;
    ArrayList<Integer> numberOfIngs;
    ArrayList<String> measureForTime;

    Cursor cursor;


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

        AvlReciepesRecyclerViewAdapter.OnItemTouchListener itemTouchListener = new AvlReciepesRecyclerViewAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                cursor.moveToPosition(IDs.get(position) - 1);

                Intent intent = new Intent(getActivity(), RecipePreviewActivity.class); //TODO maybe
                intent.putExtra("numberOfRecipe", IDs.get(position));
                startActivity(intent);
                cursor.close();
            }
        };

        mAdapter = new AvlReciepesRecyclerViewAdapter(getActivity() ,availableRecipesArrayList,
                timeForCooking, numberOfSteps, numberOfIngs, measureForTime, itemTouchListener);  // setting adapter.

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
        measureForTime = new ArrayList<>();

        cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(),
                                Database.getRecipesIsAvailable(), Database.getRecipesNumberOfSteps(),
                                Database.getRecipesTimeForCooking(), Database.getRecipesNumberOfIngredients(),
                                Database.getRecipesMeasureForTime()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                if (cursor.getInt(2) != 0) {
                    availableRecipesArrayList.add(cursor.getString(1));
                    IDs.add(cursor.getInt(0));
                    timeForCooking.add(cursor.getInt(4));
                    numberOfSteps.add(cursor.getInt(3));
                    numberOfIngs.add(cursor.getInt(5));
                    measureForTime.add(cursor.getString(6));
                }
            } while (cursor.moveToNext());
        }
    }

}
