package com.nomapp.nomapp_beta.Start;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by antonid on 24.09.2015.
 */
public class StartFragment extends Fragment implements ObservableScrollViewCallbacks {
    CountingIngredientsThread countingIngredientsThread;
    Handler handler;
    Handler imgBtnEnabler;

    ObservableRecyclerView selectedIngredients;
    ImageButton toRecipesBtn;
    TextView numOfRecipesTV;

   // CardViewAdapter mAdapter;
    SimpleHeaderRecyclerAdapter mAdapter;

    SwipeableRecyclerViewTouchListener swipeTouchListener;

    ArrayList<String> forSelectedIngridients;
    ArrayList<ArrayList<Integer>> ingredientsForAvailableRecipes;
    ArrayList<Integer> IDsOfSelectedIngs;
    ArrayList<Integer> IDsOfAvailableRecipes;

    int numberOfAvailableRecipes;
    int numberOfSelectedIngredients;

    //--------------------------------------------------------Variables for FlexibleSpace------------------------------------------------//
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private static final int NUM_OF_ITEMS = 100;

    private View mImageView;
    private View mOverlayView;
    private View mRecyclerViewBackground;
    private TextView mTitleView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    //--------------------------------------------------------End of variables for FlexibleSpace------------------------------------------------//


    StartFragmentEventsListener eventsListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.framgent_start, null);

        selectedIngredients = (ObservableRecyclerView) v.findViewById(R.id.start_recycler);
        numOfRecipesTV = (TextView) v.findViewById(R.id.numOfRecipesTV);

        toRecipesBtn = (ImageButton) v.findViewById(R.id.startFridgeButton);
        toRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventsListener.onImgBtnClick();
            }
        });


        //------------------------------------------------Code for FlexibleSpace-------------------------------------------//

        mImageView = v.findViewById(R.id.container_with_image);
        mOverlayView = v.findViewById(R.id.overlay);
        // mRecyclerViewBackground makes RecyclerView's background except header view.
        mRecyclerViewBackground = v.findViewById(R.id.list_background);


        mTitleView = (TextView) getActivity().findViewById(R.id.start_title);
        mTitleView.setText("");
        // setTitle(null);

        //-----------------------------------------------End of code for FlexibleSpace-------------------------------------//

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
        //creating and starting new thread
        countingIngredientsThread = new CountingIngredientsThread();
        countingIngredientsThread.start();


        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = getActionBarSize();

        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header, null);
        headerView.post(new Runnable() {
            @Override
            public void run() {
                headerView.getLayoutParams().height = mFlexibleSpaceImageHeight;
            }
        });

        //List of selectedIngredients already filled in StartActivity
        setUpRecyclerView(headerView);
        //setDummyDataWithHeader(recyclerView, headerView);


        //since you cannot programmatically add a header view to a RecyclerView we added an empty view as the header
        // in the adapter and then are shifting the views OnCreateView to compensate
        final float scale = 1 + MAX_TEXT_SCALE_DELTA;
        mRecyclerViewBackground.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationY(mRecyclerViewBackground, mFlexibleSpaceImageHeight);
            }
        });
        ViewHelper.setTranslationY(mOverlayView, mFlexibleSpaceImageHeight);
        mTitleView.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationY(mTitleView, (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale));
                ViewHelper.setPivotX(mTitleView, 0);
                ViewHelper.setPivotY(mTitleView, 0);
                ViewHelper.setScaleX(mTitleView, scale);
                ViewHelper.setScaleY(mTitleView, scale);
            }
        });

        setSwipeTouchListener();
    }

    int fillSelectedIngridients() { // fill ArrayList for RecyclerView
        int number = 0;
        forSelectedIngridients = new ArrayList<>();
        IDsOfSelectedIngs = new ArrayList<>();
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
                    forSelectedIngridients.add(cursor.getString(1));
                    IDsOfSelectedIngs.add(cursor.getInt(0));
                    number++;
                    Log.w("TAG", number + "");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return number;
    }


    void setUpRecyclerView(View headerView) {
        // setting up visual RecyclerView
        CardViewAdapter.OnItemTouchListener itemTouchListener = new CardViewAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                //    Toast.makeText(StartActivity.this, "Tapped " + forSelectedIngridients.get(position), Toast.LENGTH_SHORT).show();        // notification, when you press the element
            }
        };

        //forSelectedIngredients alreadyFilled in StartActivity.
       // mAdapter = new CardViewAdapter(forSelectedIngridients, itemTouchListener, headerView);  // setting adapter.
        mAdapter = new SimpleHeaderRecyclerAdapter(getActivity(), forSelectedIngridients, headerView);

        selectedIngredients.setScrollViewCallbacks(this);
        selectedIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));
        selectedIngredients.setHasFixedSize(false);

       /* LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()); //setting layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        selectedIngredients.setLayoutManager(layoutManager);*/
        selectedIngredients.setAdapter(mAdapter);
    }
//----------------------------------------------------------------------------------------Main algorithm's functions block--------------------------------------------------------------------------------------//
    /*
    *Algorithm: We check all recipes in the database to availability (in separated thread).
    * Then we save IDs of available recipes to separate ArrayList.
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

        IDsOfAvailableRecipes = new ArrayList<>();
        ingredientsForAvailableRecipes = new ArrayList<>();

        ArrayList<Integer> ingredientsForCurrentRecipe; //Parsed from database ingredients for current recipe;

        Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(), Database.getRecipesIngredients(),
                                Database.getRecipesHowToCook(), Database.getRecipesIsAvailable(),
                                Database.getRecipesNumberOfSteps()},
                null, null, null, null
                , null);


        // We check all recipes in the database.
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                if (cursor.getInt(2) != 0) {
                    //Parse ingredients to ArrayList from the database.
                    //How it looks in the database: (1,3,35,50.).
                    ingredientsForCurrentRecipe = convertIngridientsToArrayList(cursor.getString(2));

                    //Check is current recipe available.
                    isAvailable = checkIsRecipeAvailable(ingredientsForCurrentRecipe);
                    if (isAvailable){
                        //If it is available we note it in the database,add its id to the ArrayList,
                        //add ingredients for recipe to the ArrayList
                        // and increment numberOfAvailableIngredients.
                        ingredientsForAvailableRecipes.add(ingredientsForCurrentRecipe);
                        Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getRecipesTableName()
                                + " SET isAvailable=1 WHERE _id=" + (cursor.getPosition() + 1) + ";");
                        IDsOfAvailableRecipes.add(cursor.getInt(0));
                        numOfAvlRecipes++;
                    } else{
                        //If it is not we also note it in the database.
                        Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getRecipesTableName()
                                + " SET isAvailable=0 WHERE _id=" + (cursor.getPosition() + 1) + ";");
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

      //  numOfRecipesTV.setText(numOfAvlRecipes + "");

        return numOfAvlRecipes;
    }

    //Check only recipes which IDs is in the ArrayList.
    //Perform after every swipe.
    private int calculateNumberOfAvlRcpsAfterSwipe(){
        int numberOfAvlRecipes = 0;
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
        for (int counter = 0; counter < IDsOfAvailableRecipes.size(); counter++){
            //Move to, for example, first ID, or second, etc.
            cursor.moveToPosition(IDsOfAvailableRecipes.get(counter) - 1);

            //After swipe we get ingredients from the ArrayList
            //And we don't need to make query to the database.
            ingredientsForCurrentRecipe = ingredientsForAvailableRecipes.get(counter);

            //Check is current recipe available.
            isAvailable = checkIsRecipeAvailable(ingredientsForCurrentRecipe);
            if (isAvailable){
                //If it is available we note it in the database and increment numberOfAvlRecipes.
                Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getRecipesTableName()
                        + " SET isAvailable=1 WHERE _id=" + (cursor.getPosition() + 1) + ";");
                numberOfAvlRecipes++;
            } else{
                //If it is not we also note it in the database and remove it's id from the
                //ArrayList of IDs.
                Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getRecipesTableName()
                         + " SET isAvailable=0 WHERE _id=" + (cursor.getPosition() + 1) + ";");
                IDsOfAvailableRecipes.remove(counter);
                ingredientsForAvailableRecipes.remove(counter);
            }
        }
        cursor.close();

        numOfRecipesTV.setText(numberOfAvlRecipes + "");
        return numberOfAvlRecipes;
    }

    //Check is recipe available. Argument - ingredients which required for recipe.
    private boolean checkIsRecipeAvailable(ArrayList<Integer> ingredientsForCurrentRecipe){

        Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                new String[]
                        {Database.getIngredientId(), Database.getIngredientName(),
                                Database.getIngredientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();

        int numberOfIngredients = ingredientsForCurrentRecipe.size();

        //Check every ingredient in the list of ingredients for current recipe.
        for (int counter = 0; counter < numberOfIngredients; counter++){
            cursor.moveToPosition(ingredientsForCurrentRecipe.get(counter) - 1);

            if (cursor.getInt(2) != 1){
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
            while (!temporaryStack.empty()){
                currentIngridient += temporaryStack.pop() * factor;
                factor *= 10;
            }
            factor = 1;
            converted.add(currentIngridient);
            currentIngridient = 0;
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
                                            + " SET checked=0 WHERE _id=" + IDsOfSelectedIngs.get(position-1) + ";");
                                    forSelectedIngridients.remove(position-1);
                                    IDsOfSelectedIngs.remove(position-1);
                                    numberOfAvailableRecipes = calculateNumberOfAvlRcpsAfterSwipe();

                                    //make unavailable imageButton when we havent available recipes
                                    if (numberOfAvailableRecipes == 0)
                                        toRecipesBtn.setEnabled(false);
                                    else
                                        toRecipesBtn.setEnabled(true);
                                    mAdapter.notifyItemRemoved(position);
                                    numberOfSelectedIngredients--;
                                    if (numberOfSelectedIngredients == 0) {
                                        eventsListener.onFridgeEmpty();
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                                //onScrollChanged(0, false, false);
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {   //swipe to the right
                                for (int position : reverseSortedPositions) {
                                    Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getIngredientsTableName()
                                             + " SET checked=0 WHERE _id=" + IDsOfSelectedIngs.get(position-1) + ";");
                                    forSelectedIngridients.remove(position-1);
                                    IDsOfSelectedIngs.remove(position-1);
                                    numberOfAvailableRecipes = calculateNumberOfAvlRcpsAfterSwipe();
                                    //make unavailable imageButton when we havent available recipes
                                    if (numberOfAvailableRecipes == 0)
                                        toRecipesBtn.setEnabled(false);
                                    else
                                        toRecipesBtn.setEnabled(true);
                                    mAdapter.notifyItemRemoved(position);
                                    numberOfSelectedIngredients--;
                                    if (numberOfSelectedIngredients == 0) {
                                        eventsListener.onFridgeEmpty();
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                        });

        selectedIngredients.addOnItemTouchListener(swipeTouchListener);

    }


    //---------------------------------------------------------Realization of FlexibleSpace----------------------------------------------------------------------//
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mRecyclerViewBackground, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle();
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle() {
      /*  Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, v.findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
        }*/
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }


    //-------------------------------------------------------End of realization of FlexibleSpqce----------------------------------------------------------------//


    //----------------------------------------------------------Thread for counting number of available recipes------------------------------------------------------------//
    class CountingIngredientsThread extends Thread {
        @Override
        public void run() {
            super.run();
            Log.w("THREAD_TAG", "hello");
            //ArrayList of selected recipes already filled in StartActivity's method.
            numberOfAvailableRecipes = calculateNumberOfAvailableRecipes();

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
        public void onImgBtnClick();

        public void onFridgeEmpty();
    }
}
