package com.nomapp.nomapp_beta.AddIngredients;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by antonid on 01.10.2015.
 */

public class AddIngredientsRecyclerFragment extends Fragment{
    private ArrayList<String> forIngridients;
    private ArrayList<Integer> IDs;

    RecyclerView addIngredientsRecycler;
    AddIngredientsRecyclerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_add_ingredients, null);

        addIngredientsRecycler = (RecyclerView) v.findViewById(R.id.add_ings_recycler);

        fillIngridients();
        setUpRecyclerView();
        return v;
    }


    //Извлечение элментов из базы данных
    private void fillIngridients() { //
        forIngridients = new ArrayList<>();
        IDs = new ArrayList<>();

        Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                new String[]
                        {Database.getIngredientId(), Database.getIngredientName(),
                                Database.getIngredientIsChecked()},
                null, null, null, null
                , null);

        Cursor categoryCursor = Database.getDatabase().getGeneralDb().query(Database.getCategoriesTableName(),
                new String[]
                        {Database.getCategoriesId(), Database.getCategoryName(),
                                Database.getCategoryIngredients()},
                null, null, null, null
                , null);

        categoryCursor.moveToFirst();
        Intent intent = getActivity().getIntent();
        categoryCursor.moveToPosition(intent.getIntExtra("numberOfCategory", 0) - 1);

        ((TextView)getActivity().findViewById(R.id.nameOfCategory)).setText(categoryCursor.getString(1));

        IDs = parse(categoryCursor.getString(2));
        categoryCursor.close();

        cursor.moveToFirst();
//        if (!cursor.isAfterLast()) {
//            do {
//                String name = cursor.getString(1);
//                forIngridients.add(name);
//            } while (cursor.moveToNext());
//        }
        int size = IDs.size();
        for (int counter = 0; counter < size; counter++) {
            cursor.moveToPosition(IDs.get(counter) - 1);
            forIngridients.add(cursor.getString(1));
        }

        cursor.close();

    }

    private ArrayList<Integer> parse(String toConvert) {
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


    void setUpRecyclerView() {
        AddIngredientsRecyclerAdapter.OnItemTouchListener itemTouchListener = new AddIngredientsRecyclerAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                        new String[]
                                {Database.getIngredientId(), Database.getIngredientName(),
                                        Database.getIngredientIsChecked()},
                        null, null, null, null
                        , null);
                Log.w("MY_LOG", "kek");
                cursor.moveToFirst();
                cursor.moveToPosition(IDs.get(position) - 1);
                int isChecked = cursor.getInt(2);
                if (isChecked == 0) {
                    Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getIngredientsTableName()
                            + " SET checked=1 WHERE _id=" + IDs.get(position) + ";");
                    (view.findViewById(R.id.name_of_ingredient_tv)).setBackgroundColor(getResources().getColor(R.color.chosenElement));
                    //  ((TextView) view).setTextColor(getResources().getColor(R.color.chosenElement));
                    Log.d("MY_TAG", "Checked position " + IDs.get(position));

                } else {
                    Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getIngredientsTableName()
                            + " SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                    (view.findViewById(R.id.name_of_ingredient_tv)).setBackgroundColor(getResources().getColor(R.color.white));
                    Log.d("MY_TAG", "Unchecked position " + IDs.get(position));

                }
                cursor.close();
            }
        };
        mAdapter = new AddIngredientsRecyclerAdapter(forIngridients, IDs, itemTouchListener);  // setting adapter.

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()); //setting layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        addIngredientsRecycler.setLayoutManager(layoutManager);
        addIngredientsRecycler.setAdapter(mAdapter);

    }



}
