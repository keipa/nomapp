package com.nomapp.nomapp_beta.CategoriesOfIngredients;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nomapp.nomapp_beta.R;

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
        categoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                gridViewOnClickListener.onClick(position);
            }
        });
        categoriesGridView.setAdapter(new CategoriesGVAdapter(getActivity(),
                (GridView) v.findViewById(R.id.categoryGridView)));

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
