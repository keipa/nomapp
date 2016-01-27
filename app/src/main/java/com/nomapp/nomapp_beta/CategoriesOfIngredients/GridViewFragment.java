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
    CategoriesGridViewOnClickListener gridViewOnClickListener;

    GridView categoriesGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gridview_categories, null);

        categoriesGridView = (GridView) v.findViewById(R.id.categoryGridView);

        CategoriesGVAdapter.OnItemTouchListener itemTouchListener = new CategoriesGVAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {

                Intent toIngs = new Intent(getActivity(), AddIngridientsActivity.class);
                toIngs.putExtra("numberOfCategory", position + 1);
                startActivity(toIngs);

            }
        };

        categoriesGridView.setAdapter(new CategoriesGVAdapter(getActivity(), itemTouchListener));

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


    public interface CategoriesGridViewOnClickListener{
        public void onClick(int position);
    }
}
