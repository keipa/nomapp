package com.nomapp.nomapp_beta.CategoriesOfIngredients;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nomapp.nomapp_beta.AddIngredients.AddIngridientsActivity;
import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by antonid on 30.09.2015.
 */

public class GridViewFragment extends Fragment {
    GridView categoriesGridView;

    ArrayList<String> names;
    ArrayList<String> examples;
    ArrayList<Integer> numbersOfIngredients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gridview_categories, null);

        names = new ArrayList<>();
        examples = new ArrayList<>();
        numbersOfIngredients = new ArrayList<>();

        getData();

        categoriesGridView = (GridView) v.findViewById(R.id.categoryGridView);


        CategoriesGVAdapter.OnItemTouchListener itemTouchListener = new CategoriesGVAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {

                Intent toIngs = new Intent(getActivity(), AddIngridientsActivity.class);
                toIngs.putExtra("numberOfCategory", position + 1);
                startActivity(toIngs);

            }
        };

        categoriesGridView.setAdapter(new CategoriesGVAdapter(getActivity(), names,
                numbersOfIngredients, examples, itemTouchListener));

        return v;
    }


    private void getData()
    {
        Cursor categoryCursor = Database.getDatabase().getGeneralDb().query(Database.getCategoriesTableName(),   //connection to the base
                new String[]
                        {Database.getCategoryName(), Database.getCategoryNumberOfIngredients(),
                        Database.getCategoryExample()},
                null, null, null, null
                , null);

        categoryCursor.moveToFirst();

        if (!categoryCursor.isAfterLast()) {            // loop is going throw the all ingridients and shows marked ones (marked has "1" isChecked option)
            do {
                names.add(categoryCursor.getString(0));
                numbersOfIngredients.add(categoryCursor.getInt(1));
                examples.add(categoryCursor.getString(2));
            } while (categoryCursor.moveToNext());
        }

        categoryCursor.close();
    }


    public interface CategoriesGridViewOnClickListener{
        public void onClick(int position);
    }
}
