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
    ArrayList<Integer> timeForCooking;
    ArrayList<Integer> numberOfSteps;
    ArrayList<Integer> IDs;
    ArrayList<Integer> numberOfIngs;

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
        AllRecipesRecyclerAdapter.OnItemTouchListener itemTouchListener = new AllRecipesRecyclerAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                cursor.moveToPosition(position - 1);

                Intent intent = new Intent(getActivity(), RecipePreviewActivity.class); //TODO maybe
                intent.putExtra("numberOfRecipe", IDs.get(position) - 1);
                intent.putExtra("cooking", cursor.getString(6));
                intent.putExtra("numberOfSteps", numberOfSteps.get(position));
                intent.putExtra("nameOfRecipe", availableRecipesArrayList.get(position));
                startActivity(intent);
                cursor.close();
            }
        };

        mAdapter = new AllRecipesRecyclerAdapter(availableRecipesArrayList, timeForCooking, numberOfSteps, numberOfIngs, itemTouchListener);  // setting adapter.

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

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                    availableRecipesArrayList.add(cursor.getString(1));
                    IDs.add(cursor.getInt(0));
                    timeForCooking.add(cursor.getInt(4));
                    numberOfSteps.add(cursor.getInt(3));
                    numberOfIngs.add(cursor.getInt(5));
            } while (cursor.moveToNext());
        }
    }


}
