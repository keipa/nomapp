package com.nomapp.nomapp_beta.Categories;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by antonid on 30.09.2015.
 */
public class SearchFragment extends Fragment {
    RecyclerView findedIngsRecycler;
    ArrayList<String> findedIngsArray;
    ArrayList<Integer> IDs;
    FindedIngredientsRecyclerAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_categories, null);

        findedIngsRecycler = (RecyclerView) v.findViewById(R.id.findedIngsRecyclerView);
        findedIngsArray = new ArrayList<>();
        IDs = new ArrayList<>();

        return v;
    }

    /*
    * Method for searching
    * items in database
    * by current query
    */
    void search(String enteredText){

        findedIngsArray.clear();
        IDs.clear();

        if (enteredText.length() != 0) {
            Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                    new String[]
                            {Database.getIngredientId(), Database.getIngredientName(),
                                    Database.getIngredientIsChecked()},
                    null, null, null, null
                    , null);

            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {
                do {
                    String name = cursor.getString(1);
                    if (name.toLowerCase().contains(enteredText) || name.contains(enteredText)) {
                        findedIngsArray.add(name);
                        IDs.add(cursor.getInt(0));
                    }
                } while (cursor.moveToNext());
            }
        }
        setUpRecyclerView();
    }

    /*
   * Sets up RecyclerView with finded items.
   * Add possibility to select items by OnItemTouchListener
   * interface in Adapter.
    */
    void setUpRecyclerView() {
        FindedIngredientsRecyclerAdapter.OnItemTouchListener itemTouchListener = new FindedIngredientsRecyclerAdapter.OnItemTouchListener() {
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
                    view.findViewById(R.id.name_of_ingredient_tv).setBackgroundColor(getResources().getColor(R.color.chosenElement));
                    ((TextView)view.findViewById(R.id.name_of_ingredient_tv)).setTextColor(getResources().getColor(R.color.white));
                    //  ((TextView) view).setTextColor(getResources().getColor(R.color.chosenElement));
                    Log.d("MY_TAG", "Checked position " + IDs.get(position));

                } else {
                    Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getIngredientsTableName()
                            + " SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                    view.findViewById(R.id.name_of_ingredient_tv).setBackgroundColor(getResources().getColor(R.color.white));
                    ((TextView)view.findViewById(R.id.name_of_ingredient_tv)).setTextColor(getResources().getColor(R.color.black));
                    Log.d("MY_TAG", "Unchecked position " + IDs.get(position));

                }
                cursor.close();
            }
        };
        mAdapter = new FindedIngredientsRecyclerAdapter(findedIngsArray, IDs, itemTouchListener);  // setting adapter.

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()); //setting layout manager
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        findedIngsRecycler.setLayoutManager(layoutManager);
        findedIngsRecycler.setAdapter(mAdapter);

    }


}
