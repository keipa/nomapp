package com.nomapp.nomapp_beta.Start;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.nomapp.nomapp_beta.UserGuide.HelpDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * Created by antonid on 24.09.2015.
 */
public class StartFragment extends Fragment {
    CountingIngredientsThread countingIngredientsThread;
    Handler handler;
    Handler imgBtnEnabler;
    Handler setterRecCaseText;
    Handler setterAvailCaseText;

    RecyclerView selectedIngredients;
    ImageButton toRecipesBtn;
    TextView numOfRecipesTV;
    TextView recTextView;
    TextView availTextView;


    CardViewAdapter mAdapter;
    SwipeableRecyclerViewTouchListener swipeTouchListener;

    ArrayList<String> namesOfSelectedIngredients;

    /*
        It needs to do not parse ings for recipes after every swipe.
        We write ingredients for every already available recipe here.
     */
    ArrayList<ArrayList<Integer>> ingredientsForAvailableRecipes;

    ArrayList<Integer> IDsOfSelectedIngredients;
    ArrayList<Integer> IDsOfAvailableRecipes;
    ArrayList<Integer> IDsOfPotentialRecipes;


    StartFragmentEventsListener eventsListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, null);

        selectedIngredients = (RecyclerView) v.findViewById(R.id.start_recycler);
        numOfRecipesTV = (TextView) v.findViewById(R.id.numOfRecipesTV);
        recTextView = (TextView) v.findViewById(R.id.textView5);
        availTextView = (TextView) v.findViewById(R.id.textView8);
        toRecipesBtn = (ImageButton) v.findViewById(R.id.imageButton);
        toRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventsListener.onImgBtnClick();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            eventsListener = (StartFragmentEventsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement inteface");
        }

        showHelpDialog();
    }

    public void onStart() {
        super.onStart();
        //ArrayList of selected recipes already filled in StartActivity's method.
        //   numberOfAvailableRecipes = calculateNumberOfAvailableRecipes();


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                numOfRecipesTV.setText(text);
            }
        };

        imgBtnEnabler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                if (text == "enable")
                    toRecipesBtn.setEnabled(true);
                if (text == "disable")
                    toRecipesBtn.setEnabled(false);
            }
        };

        setterRecCaseText = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                recTextView.setText(text);
            }
        };

        setterAvailCaseText = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                availTextView.setText((String) msg.obj);
            }
        };
        //creating and starting new thread
        countingIngredientsThread = new CountingIngredientsThread();
        countingIngredientsThread.start();

        fillSelectedIngridients();
        setUpRecyclerView();
        setSwipeTouchListener();
    }

    boolean isAnyIngredientSelected(){
        Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),   //connection to the base
                new String[]
                        {Database.getIngredientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {            // loop is going throw the all ingridients and shows marked ones (marked has "1" isChecked option)
            do {
                if (cursor.getInt(0) != 0) {
                    cursor.close();
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    void fillSelectedIngridients() { // fill ArrayList for RecyclerView
        namesOfSelectedIngredients = new ArrayList<>();
        IDsOfSelectedIngredients = new ArrayList<>();
        Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),   //connection to the base
                new String[]
                        {Database.getIngredientId(), Database.getIngredientName(),
                                Database.getIngredientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {            // loop is going throw the all ingridients and shows marked ones (marked has "1" isChecked option)
            do {
                if (cursor.getInt(2) != 0) {
                    namesOfSelectedIngredients.add(cursor.getString(1));
                    IDsOfSelectedIngredients.add(cursor.getInt(0));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        //mAdapter.notifyDataSetChanged();
    }


    void setUpRecyclerView() {
        // setting up visual RecyclerView
        CardViewAdapter.OnItemTouchListener itemTouchListener = new CardViewAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                //    Toast.makeText(StartActivity.this, "Tapped " + forSelectedIngridients.get(position), Toast.LENGTH_SHORT).show();        // notification, when you press the element
            }
        };

        mAdapter = new CardViewAdapter(namesOfSelectedIngredients, itemTouchListener);  // setting adapter.

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()); //setting layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        selectedIngredients.setLayoutManager(layoutManager);
        selectedIngredients.setAdapter(mAdapter);
    }
//----------------------------------------------------------------------------------------Main algorithm's functions block--------------------------------------------------------------------------------------//
    /*
    *Algorithm: First of all we parse strings with potential recipes of ingredients
    * to ArrayList. Then we delete the repeating elements. After that we check all potential recipes.
    * After every swipe we check only already available recipes (their IDs stores in separate
    * ArrayList) because we cant make available new recipe by swipe, only unavailable.
    * Thus we need check only, for example, 3 or 15 or 20 already available recipes after swipe,
    * but not 1000 (all recipes in the database).
    *
     */


    //Check every recipe in database. Perform only in OnStart.
    private int calculateNumberOfAvailableRecipes() {
        int numOfAvlRecipes = 0;
        boolean isAvailable;

        HashSet<Integer> potentialRecipesSet = new HashSet<>();  //for potential recipes. HashSet works without repeats.


        //Parsing potential recipes.
        Cursor ingredientsTableCursor = Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                new String[]
                        {Database.getIngredientIsChecked(), Database.getIngredientsForWhatRecipes()},
                null, null, null, null
                , null);

        ingredientsTableCursor.moveToFirst();

        //parsing potential recipes from every selected ingredient.
        if (!ingredientsTableCursor.isAfterLast()) {
            do {
                if (!ingredientsTableCursor.getString(1).equals("NULL") &&
                        ingredientsTableCursor.getInt(0) == 1) { //TODO "NULL"
                    potentialRecipesSet.addAll(parsePotentialRecipesFromIngredient(ingredientsTableCursor.getString(1)));
                }
            } while (ingredientsTableCursor.moveToNext());
        }
        ingredientsTableCursor.close();


        IDsOfAvailableRecipes = new ArrayList<>();
        ingredientsForAvailableRecipes = new ArrayList<>();

        //Converting potential recipes to ArrayList to use it in "foreach" cycle
        IDsOfPotentialRecipes = new ArrayList<>(potentialRecipesSet);

        ArrayList<Integer> ingredientsForCurrentRecipe; //Parsed from database ingredients for current recipe;

        //Cursor for working with recipes from the database (setting up is it available, etc.)
        Cursor recipesCursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                new String[]{Database.getRecipesIngredients()}, null, null, null, null, null);

        //Checking for availability all potential recipes.
        for (int currentRecipe : IDsOfPotentialRecipes) {
            recipesCursor.moveToPosition(currentRecipe - 1);
            ingredientsForCurrentRecipe = convertIngridientsToArrayList(recipesCursor.getString(0)); // bad solution. need to go "where id bla bla"

            isAvailable = checkIsRecipeAvailable(ingredientsForCurrentRecipe);
            if (isAvailable) {
                /* If it is available we note it in the database,add its id to the ArrayList,
                * add ingredients for recipe to the ArrayList
                * and increment numberOfAvailableIngredients.
                */
                ingredientsForAvailableRecipes.add(ingredientsForCurrentRecipe);
                Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getRecipesTableName()
                        + " SET isAvailable=1 WHERE _id=" + (currentRecipe) + ";");
                IDsOfAvailableRecipes.add(currentRecipe);
                numOfAvlRecipes++;
            } else {
                //If it is not we also note it in the database.
                Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getRecipesTableName()
                        + " SET isAvailable=0 WHERE _id=" + (currentRecipe) + ";");
            }
        }
        recipesCursor.close();

        return numOfAvlRecipes;
    }

    //Check only recipes which IDs is in the ArrayList.
    //Perform after every swipe.
    private void calculateNumberOfAvlRcpsAfterSwipe() {
        boolean isAvailable;

        ArrayList<Integer> ingredientsForCurrentRecipe;

        Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(), Database.getRecipesIngredients(),
                                Database.getRecipesHowToCook(), Database.getRecipesIsAvailable(),
                                Database.getRecipesNumberOfSteps()},
                null, null, null, null
                , null);

        cursor.moveToFirst();

        //We check only "numberOfAvailableRecipes" (which already calculated) recipes.
        //Their IDs is in IDsOfAvailableRecipes. (Yeah, it is that ArrayList).
        for (int counter = 0; counter < IDsOfAvailableRecipes.size(); counter++) {
            //Move to, for example, first ID, or second, etc.
            cursor.moveToPosition(IDsOfAvailableRecipes.get(counter) - 1);

            //After swipe we get ingredients from the ArrayList
            //And we don't need to make query to the database.
            ingredientsForCurrentRecipe = ingredientsForAvailableRecipes.get(counter);

            //Check is current recipe available.
            isAvailable = checkIsRecipeAvailable(ingredientsForCurrentRecipe);
            if (isAvailable) {
                //If it is available we note it in the database and increment numberOfAvlRecipes.
                Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getRecipesTableName()
                        + " SET isAvailable=1 WHERE _id=" + (cursor.getPosition() + 1) + ";");
            } else {
                //If it is not we also note it in the database and remove it's id from the
                //ArrayList of IDs.
                Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getRecipesTableName()
                        + " SET isAvailable=0 WHERE _id=" + (cursor.getPosition() + 1) + ";");
                IDsOfAvailableRecipes.remove(counter);
                ingredientsForAvailableRecipes.remove(counter);
            }
        }
        cursor.close();

        int numberOfAvailableRecipes = IDsOfAvailableRecipes.size();

        numOfRecipesTV.setText(numberOfAvailableRecipes + "");
        recTextView.setText(setEndingInRecipe(numberOfAvailableRecipes));
        availTextView.setText(setEndingInAvailable(numberOfAvailableRecipes));
    }


    String setEndingInRecipe(int rec) {
        String toReturn = "";
        int modNumberOAR = rec % 10;
        if (modNumberOAR >= 2 && modNumberOAR <= 4) {
            toReturn = getResources().getString(R.string.twofourrec);

        }
        if (modNumberOAR >= 5 || modNumberOAR == 0) {
            toReturn = getResources().getString(R.string.morerec);
        }
        if (modNumberOAR == 1) {
            toReturn = getResources().getString(R.string.onerec);
        }
        return toReturn;
    }

    String setEndingInAvailable(int rec) {
        String toReturn = "";
        int modNumberOAR = rec % 10;
        if (modNumberOAR >= 2 && modNumberOAR <= 4) {
            toReturn = getResources().getString(R.string.twoavail);

        }
        if (modNumberOAR >= 5 || modNumberOAR == 0) {
            toReturn = getResources().getString(R.string.moreavail);

        }
        if (modNumberOAR == 1) {
            toReturn = getResources().getString(R.string.oneavail);
        }
        return toReturn;
    }

    //Check is recipe available. Argument - ingredients which required for recipe.
    private boolean checkIsRecipeAvailable(ArrayList<Integer> ingredientsForCurrentRecipe) {

        Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                new String[]
                        {Database.getIngredientId(), Database.getIngredientName(),
                                Database.getIngredientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();

        int numberOfIngredients = ingredientsForCurrentRecipe.size();

        //Check every ingredient in the list of ingredients for current recipe.
        for (int counter = 0; counter < numberOfIngredients; counter++) {
            cursor.moveToPosition(ingredientsForCurrentRecipe.get(counter) - 1);

            if (cursor.getInt(2) != 1) {
                //If current ingredient is not selected we go out of cycle and return that
                //current recipe is not available.
                return false;
            }

            /*
            //Alternative method to define is recipe available.
            //But it is maybe too slow. Its need to pass all elements in IdsOfSelectedIngs
            //in the worst case.
            if (!IDsOfSelectedIngs.contains(ingredientsForCurrentRecipe.get(counter) - 1)){
                return false;
            }*/

        }
        cursor.close();

        //If we checked all ingredients and every of them is selected we return that current
        // recipe is available.
        return true;
    }

    //Parse ingredients to ArrayList from the database.
    //How it looks in the database: for example (1,3,35,50.).
    //WARNING: I don't comment this function because it is not optimized. I will rewrite it.
    private ArrayList<Integer> convertIngridientsToArrayList(String toConvert) { // parsing requed ingridients for every recipe from Database (part 2)
        ArrayList<Integer> converted = new ArrayList<>();
        Stack<Integer> temporaryStack = new Stack<>();
        int counter = 0;
        int factor = 1;
        int currentIngridient = 0;
        int size = toConvert.length();
        for (counter = 0; counter < size; counter++) {
            while (toConvert.charAt(counter) != ',' && toConvert.charAt(counter) != '.') {//TODO
                temporaryStack.push(toConvert.charAt(counter) - '0');
                counter++;
            }
            while (!temporaryStack.empty()) {
                currentIngridient += temporaryStack.pop() * factor;
                factor *= 10;
            }
            factor = 1;
            converted.add(currentIngridient);
            currentIngridient = 0;
        }
        return converted;
    }

    private ArrayList<Integer> parsePotentialRecipesFromIngredient(String toConvert) { // parsing requed ingridients for every recipe from Database (part 2)
        ArrayList<Integer> converted = new ArrayList<>();
        Stack<Integer> temporaryStack = new Stack<>();
        int counter = 0;
        int factor = 1;
        int currentIngredient = 0;
        int size = toConvert.length();
        for (counter = 0; counter < size; counter++) {
            while (toConvert.charAt(counter) != ',' && toConvert.charAt(counter) != '.') {//TODO
                temporaryStack.push(toConvert.charAt(counter) - '0');
                counter++;
            }
            while (!temporaryStack.empty()) {
                currentIngredient += temporaryStack.pop() * factor;
                factor *= 10;
            }
            factor = 1;
            converted.add(currentIngredient);
            currentIngredient = 0;
        }
        return converted;
    }


//----------------------------------------------------------------------------------------End of main algorithm's functions block--------------------------------------------------------------------------------------//

    void setSwipeTouchListener() {
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
                                    Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getIngredientsTableName()
                                            + " SET checked=0 WHERE _id=" + IDsOfSelectedIngredients.get(position) + ";");
                                    namesOfSelectedIngredients.remove(position);
                                    IDsOfSelectedIngredients.remove(position);

                                    calculateNumberOfAvlRcpsAfterSwipe();

                                    //make unavailable imageButton when we havent available recipes
                                    if (IDsOfAvailableRecipes.isEmpty())
                                        toRecipesBtn.setEnabled(false);
                                    else
                                        toRecipesBtn.setEnabled(true);
                                    mAdapter.notifyItemRemoved(position);

                                    if (IDsOfSelectedIngredients.isEmpty()) {
                                        eventsListener.onFridgeEmpty();
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {   //swipe to the right
                                for (int position : reverseSortedPositions) {
                                    Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getIngredientsTableName()
                                            + " SET checked=0 WHERE _id=" + IDsOfSelectedIngredients.get(position) + ";");
                                    namesOfSelectedIngredients.remove(position);
                                    IDsOfSelectedIngredients.remove(position);

                                    calculateNumberOfAvlRcpsAfterSwipe();

                                    //make unavailable imageButton when we havent available recipes
                                    if (IDsOfAvailableRecipes.isEmpty())
                                        toRecipesBtn.setEnabled(false);
                                    else
                                        toRecipesBtn.setEnabled(true);
                                    mAdapter.notifyItemRemoved(position);

                                    if (IDsOfSelectedIngredients.isEmpty()) {
                                        eventsListener.onFridgeEmpty();
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        selectedIngredients.addOnItemTouchListener(swipeTouchListener);
    }

    //----------------------------------------------------------Thread for counting number of available recipes------------------------------------------------------------//
    class CountingIngredientsThread extends Thread {
        @Override
        public void run() {
            super.run();
            // Log.w("THREAD_TAG", "hello");
            //ArrayList of selected recipes already filled in StartActivity's method.
            calculateNumberOfAvailableRecipes();

            int numberOfAvailableRecipes = IDsOfAvailableRecipes.size();

            String recCase = setEndingInRecipe(numberOfAvailableRecipes); //for cases of 2 views with text
            String availCase = setEndingInAvailable(numberOfAvailableRecipes);

            Message textOfRecCase = new Message(); //Messages for .setText in main thread
            textOfRecCase.obj = recCase;
            setterRecCaseText.sendMessage(textOfRecCase);

            Message textOfAvailCase = new Message();
            textOfAvailCase.obj = availCase;
            setterAvailCaseText.sendMessage(textOfAvailCase);

            //make unavailable imageButton when we havent available recipes
            Message enableButton = new Message();
            if (numberOfAvailableRecipes == 0)
                enableButton.obj = "disable";
            else
                enableButton.obj = "enable";
            imgBtnEnabler.sendMessage(enableButton);

            Message msg = new Message();
            msg.obj = numberOfAvailableRecipes + "";
            handler.sendMessage(msg);
        }
    }
    //----------------------------------------------------------End of thread for counting number of available recipes------------------------------------------------------//


    public interface StartFragmentEventsListener {
        void onImgBtnClick();

        void onFridgeEmpty();
    }

    private void showHelpDialog() {
        SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        HelpDialog helpDialog;

        if (prefs.getBoolean("isFirstSelectProducts", true)) {
            Bundle bundle = new Bundle();
            bundle.putInt("message", R.string.u_can_swipe_ings_help_msg);

            helpDialog = new HelpDialog();
            helpDialog.setArguments(bundle);
            helpDialog.show(getFragmentManager(), "helpDialog");

            prefs.edit().putBoolean("isFirstSelectProducts", false).apply();
        }
    }
}
