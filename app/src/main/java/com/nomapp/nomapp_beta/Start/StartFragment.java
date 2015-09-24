package com.nomapp.nomapp_beta.Start;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by antonid on 24.09.2015.
 */
public class StartFragment extends Fragment {

    RecyclerView selectedIngredients;
    ImageButton toRecipesBtn;
    TextView numOfRecipesTV;

    CardViewAdapter mAdapter;
    SwipeableRecyclerViewTouchListener swipeTouchListener;

    ArrayList<String> forSelectedIngridients;
    ArrayList<String> ingridientsForRecipe;
    ArrayList<Integer> IDs;
    ArrayList<ArrayList<Integer>> convertedIngrodientsForRecipe;

    int nubmerOfAvailableRecipes;
    int numberOfSelectedIngredients;

    onImageButtonClickListener imgBtnClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.framgent_start, null);

        selectedIngredients =  (RecyclerView) v.findViewById(R.id.start_recycler);
        numOfRecipesTV = (TextView) v.findViewById(R.id.numOfRecipesTV);

        toRecipesBtn = (ImageButton) v.findViewById(R.id.startFridgeButton);
        toRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBtnClickListener.onClick();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            imgBtnClickListener = (onImageButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    public void onStart() {
        super.onStart();


        numberOfSelectedIngredients = 0;
        fillSelectedIngridients();

        countNumberOfAvailableRecipes();

        setUpRecyclerView();
        setSwipeTouchListener();


        // Log.d(LOG_TAG, "Fragment1 onStart");
    }

    void fillSelectedIngridients() { // fill ArrayList for RecyclerView
        forSelectedIngridients = new ArrayList<>();
        IDs = new ArrayList<>();
        Cursor cursor = Database.getDatabase().getIngridients().query(Database.getIngredientsTableName(),   //connection to the base
                new String[]
                        {Database.getIngridientId(), Database.getIngridientName(),
                                Database.getIngridientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {            // loop is going throw the all ingridients and shows marked ones (marked has "1" isChecked option)
            do {
                if (cursor.getInt(2) != 0) {
                    forSelectedIngridients.add(cursor.getString(1));
                    IDs.add(cursor.getInt(0));
                    numberOfSelectedIngredients++;
                    Log.w("TAG", numberOfSelectedIngredients + "");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }



    void setUpRecyclerView() {
        // setting up visual RecyclerView
        CardViewAdapter.OnItemTouchListener itemTouchListener = new CardViewAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                //    Toast.makeText(StartActivity.this, "Tapped " + forSelectedIngridients.get(position), Toast.LENGTH_SHORT).show();        // notification, when you press the element
            }
        };

        mAdapter = new CardViewAdapter(forSelectedIngridients, itemTouchListener);  // setting adapter.

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()); //setting layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        selectedIngredients.setLayoutManager(layoutManager);
        selectedIngredients.setAdapter(mAdapter);
    }

     void countNumberOfAvailableRecipes() { // calculating Available recipes;
        nubmerOfAvailableRecipes = 0;
        fillIngridientsForRecipe();
        int size = ingridientsForRecipe.size();
        convertedIngrodientsForRecipe = new ArrayList<>();
        for (int counter = 0; counter < size; counter++) {
            convertedIngrodientsForRecipe.add(convertIngridientsToArrayList(ingridientsForRecipe.get(counter)));
        }
        checking();
        numOfRecipesTV.setText(nubmerOfAvailableRecipes+"");
    }

    private void checking() {
        boolean isRecipeAvailable = true;

        Cursor cursor = Database.getDatabase().getIngridients().query(Database.getIngredientsTableName(),
                new String[]
                        {Database.getIngridientId(), Database.getIngridientName(),
                                Database.getIngridientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();

        int numberOfRecipes = convertedIngrodientsForRecipe.size();
        for (int currentRecipe = 0; currentRecipe < numberOfRecipes; currentRecipe++) {                 //mainframe alhorithm of the program
            int numberOfIngridientsInRecipe = convertedIngrodientsForRecipe.get(currentRecipe).size();
            for (int ingridientNumber = 0; ingridientNumber < numberOfIngridientsInRecipe; ingridientNumber++) {
                cursor.moveToPosition(convertedIngrodientsForRecipe.get(currentRecipe).get(ingridientNumber) - 1);
                if (cursor.getInt(2) != 1) {
                    isRecipeAvailable = false;
                    break;
                }
            }

            if (isRecipeAvailable) {
                Database.getDatabase().getRecipes().execSQL("UPDATE Recipes SET isAvailable=1 WHERE _id=" + (currentRecipe + 1) + ";");
                nubmerOfAvailableRecipes++;
            } else {
                Database.getDatabase().getRecipes().execSQL("UPDATE Recipes SET isAvailable=0 WHERE _id=" + (currentRecipe + 1) + ";");
            }
            isRecipeAvailable = true;
        }
        cursor.close();
    }

    void fillIngridientsForRecipe() { // parsing requed ingridients for every recipe from Database (part 1)
        ingridientsForRecipe = new ArrayList<String>();
        Cursor cursor = Database.getDatabase().getRecipes().query(Database.getRecipesTableName(),
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(), Database.getRecipesIngridients(),
                                Database.getRecipesHowToCook(), Database.getRecipesIsAvailable(),
                                Database.getRecipesNumberOfSteps()},
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

    private ArrayList<Integer> convertIngridientsToArrayList(String toConvert) { // parsing requed ingridients for every recipe from Database (part 2)
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
                new SwipeableRecyclerViewTouchListener(selectedIngredients,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {        //throw ingredients from your fridge
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {  //swipe to the left
                                for (int position : reverseSortedPositions) {
                                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                                    forSelectedIngridients.remove(position);
                                    IDs.remove(position);
                                    countNumberOfAvailableRecipes();
                                    mAdapter.notifyItemRemoved(position);
                                    numberOfSelectedIngredients--;
                                    if(numberOfSelectedIngredients == 0)
                                    {

                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {   //swipe to the right
                                for (int position : reverseSortedPositions) {
                                    Database.getDatabase().getIngridients().execSQL("UPDATE Ingridients SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                                    forSelectedIngridients.remove(position);
                                    IDs.remove(position);
                                    countNumberOfAvailableRecipes();
                                    mAdapter.notifyItemRemoved(position);
                                    numberOfSelectedIngredients--;
                                    if(numberOfSelectedIngredients == 0)
                                    {

                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        selectedIngredients.addOnItemTouchListener(swipeTouchListener);
    }

    public interface onImageButtonClickListener {
        public void onClick();
    }
}
