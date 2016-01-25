package com.nomapp.nomapp_beta.CategoriesOfIngredients;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import java.util.ArrayList;

/**
 * Created by antonid on 30.09.2015.
 */

public class GridViewFragment extends Fragment {
    CategoriesGridViewOnClickListener gridViewOnClickListener;

    GridView categoriesGridView;

    ArrayList<String> names;
    ArrayList<Integer> numbersOfIngredients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gridview_categories, null);

        names = new ArrayList<>();
        numbersOfIngredients = new ArrayList<>();

        getData();

        categoriesGridView = (GridView) v.findViewById(R.id.categoryGridView);
        categoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                gridViewOnClickListener.onClick(position);
            }
        });
        categoriesGridView.setAdapter(new CategoriesGVAdapter(getActivity(), names, numbersOfIngredients));

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            gridViewOnClickListener = (CategoriesGridViewOnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement inteface");
        }
    }

    private void getData()
    {
        Cursor categoryCursor = Database.getDatabase().getGeneralDb().query(Database.getCategoriesTableName(),   //connection to the base
                new String[]
                        {Database.getCategoryName(), Database.getCategoryNumberOfIngredients()},
                null, null, null, null
                , null);

        categoryCursor.moveToFirst();

        if (!categoryCursor.isAfterLast()) {            // loop is going throw the all ingridients and shows marked ones (marked has "1" isChecked option)
            do {
                names.add(categoryCursor.getString(0));
                numbersOfIngredients.add(categoryCursor.getInt(0));
            } while (categoryCursor.moveToNext());
        }

        categoryCursor.close();
    }


    public interface CategoriesGridViewOnClickListener{
        public void onClick(int position);
    }
}
