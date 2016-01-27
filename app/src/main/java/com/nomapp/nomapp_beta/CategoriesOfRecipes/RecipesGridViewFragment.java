package com.nomapp.nomapp_beta.CategoriesOfRecipes;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nomapp.nomapp_beta.AddIngredients.AddIngridientsActivity;
import com.nomapp.nomapp_beta.AllRecipes.AllRecipesActivity;
import com.nomapp.nomapp_beta.CategoriesOfIngredients.CategoriesGVAdapter;
import com.nomapp.nomapp_beta.R;

/**
 * Created by antonid on 30.09.2015.
 */
public class RecipesGridViewFragment extends Fragment {

    GridView categoriesGridView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gridview_recipes, null);

        categoriesGridView = (GridView) v.findViewById(R.id.category_recipes_gridview);


        CategoriesOfRecipesGVAdapter.OnItemTouchListener itemTouchListener = new CategoriesOfRecipesGVAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                Intent toIngs = new Intent(getActivity(), AllRecipesActivity.class);
                toIngs.putExtra("numberOfCategory", position + 1);
                startActivity(toIngs);
            }
        };

        categoriesGridView.setAdapter(new CategoriesOfRecipesGVAdapter(getActivity(), itemTouchListener));

        return v;
    }

    public interface CategoriesGridViewOnClickListener{
        public void onClick(int position);
    }
}
