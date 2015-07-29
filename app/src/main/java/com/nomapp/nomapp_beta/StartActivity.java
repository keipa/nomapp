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

import java.lang.reflect.Array;
import java.util.ArrayList;

//import com.nomapp.nomapp_beta.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity implements View.OnClickListener {

    private static final String TABLE_NAME = "Ingridients";
    private static final String RECIPES_TABLE_NAME = "Recipes";

    ListView selectedIngridients;
    RecyclerView recyclerView;

    Button showAvailableRecipes;

    ArrayList<String> forSelectedIngridients;
    ArrayList<String> ingridientsForRecipe;

    ArrayList<ArrayList<Integer>> convertedIngrodientsForRecipe;

    int nubmerOfAvailableRecipes;

    private CardViewAdapter mAdapter;
    private ArrayList<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);  //активация activity

        nubmerOfAvailableRecipes = 0;

       // selectedIngridients = (ListView) findViewById(R.id.selectedList);

        showAvailableRecipes = (Button) findViewById(R.id.showAvailableRecipes);
        showAvailableRecipes.setOnClickListener(this);

        //активация кнопки FAB (сделано програмно)
        FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ieon))
                .withButtonColor(Color.WHITE)
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.setOnClickListener(onCircleButtonCliclListener);

        mItems = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            mItems.add(String.format("Card number %02d", i));
        }

        OnItemTouchListener itemTouchListener = new OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                Toast.makeText(StartActivity.this, "Tapped " + mItems.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButton1Click(View view, int position) {
                Toast.makeText(StartActivity.this, "Clicked Button1 in " + mItems.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButton2Click(View view, int position) {
                Toast.makeText(StartActivity.this, "Clicked Button2 in " + mItems.get(position), Toast.LENGTH_SHORT).show();
            }
        };

        mAdapter = new CardViewAdapter(mItems, itemTouchListener);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
//                                    Toast.makeText(MainActivity.this, mItems.get(position) + " swiped left", Toast.LENGTH_SHORT).show();
                                    mItems.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
//                                    Toast.makeText(MainActivity.this, mItems.get(position) + " swiped right", Toast.LENGTH_SHORT).show();
                                    mItems.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }


    @Override
    protected void onStart() {
        super.onStart();
        nubmerOfAvailableRecipes = 0;

        showNumberOfAvailableRecipes();
        fillSelectedIngridients();
        setUpList();
        checking();

        showAvailableRecipes.setText(nubmerOfAvailableRecipes + " recipes available");
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

    void setUpList() {
  //      selectedIngridients.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, forSelectedIngridients));
    }

    private void fillSelectedIngridients() {
        forSelectedIngridients = new ArrayList<String>();
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
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void showNumberOfAvailableRecipes() {
        fillIngridientsForRecipe();

        int size = ingridientsForRecipe.size();

        convertedIngrodientsForRecipe = new ArrayList<ArrayList<Integer>>();
        for (int counter = 0; counter < size; counter++) {
            convertedIngrodientsForRecipe.add(convertIngridientsToArrayList(ingridientsForRecipe.get(counter)));
        }

        Log.w("MY_TAG", Integer.toString(convertedIngrodientsForRecipe.get(1).get(1)));
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

        int size = convertedIngrodientsForRecipe.size();
        for (int counter = 0; counter < size; counter++) {
            int numberOfRepipes = convertedIngrodientsForRecipe.get(counter).size();
            for (int recipeNumber = 0; recipeNumber < numberOfRepipes; recipeNumber++) {
                cursor.moveToPosition(convertedIngrodientsForRecipe.get(counter).get(recipeNumber) - 1);
                if (cursor.getInt(2) != 1) {
                    isRecipeAvailable = false;
                }
            }

            if (isRecipeAvailable == true) {
                Database.getDatabase().getRecipes().execSQL("UPDATE Recipes SET isAvailable=1 WHERE _id=" + (counter + 1) + ";");
                nubmerOfAvailableRecipes++;
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
        // while (toConvert.charAt(counter) != '.'){
        for (counter = 0; counter < size; counter++) {
            while (toConvert.charAt(counter) != ',' && toConvert.charAt(counter) != '.') {//TODO
                currentIngridient += (toConvert.charAt(counter) - '0') * factor;
                factor *= 10;
              /*  if(toConvert.charAt(counter + 1) == '.'){
                    break;
                }*/
                counter++;
            }
            factor = 1;
            //counter++;
            converted.add(currentIngridient);
            currentIngridient = 0;
            //counter++;
        }
        return converted;
    }


    // cardview code


    /**
     * Interface for the touch events in each item
     */
    public interface OnItemTouchListener {
        /**
         * Callback invoked when the user Taps one of the RecyclerView items
         *
         * @param view     the CardView touched
         * @param position the index of the item touched in the RecyclerView
         */
        public void onCardViewTap(View view, int position);

        /**
         * Callback invoked when the Button1 of an item is touched
         *
         * @param view     the Button touched
         * @param position the index of the item touched in the RecyclerView
         */
        public void onButton1Click(View view, int position);

        /**
         * Callback invoked when the Button2 of an item is touched
         *
         * @param view     the Button touched
         * @param position the index of the item touched in the RecyclerView
         */
        public void onButton2Click(View view, int position);
    }

    /**
     * A simple adapter that loads a CardView layout with one TextView and two Buttons, and
     * listens to clicks on the Buttons or on the CardView
     */
    public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
        private List<String> cards;
        private OnItemTouchListener onItemTouchListener;

        public CardViewAdapter(List<String> cards, OnItemTouchListener onItemTouchListener) {
            this.cards = cards;
            this.onItemTouchListener = onItemTouchListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_layout, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.title.setText(cards.get(i));
        }

        @Override
        public int getItemCount() {
            return cards == null ? 0 : cards.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title;
            private Button button1;
            private Button button2;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.card_view_title);
                button1 = (Button) itemView.findViewById(R.id.card_view_button1);
                button2 = (Button) itemView.findViewById(R.id.card_view_button2);

                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemTouchListener.onButton1Click(v, getPosition());
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemTouchListener.onButton2Click(v, getPosition());
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemTouchListener.onCardViewTap(v, getPosition());
                    }
                });
            }
        }

    }
}