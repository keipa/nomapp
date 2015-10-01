package com.nomapp.nomapp_beta.AddIngredients;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import com.nomapp.nomapp_beta.R;


/**
 * Created by antonid on 05.07.2015.
 */

public class AddIngridientsActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        AddIngredientsRecyclerFragment frag = new AddIngredientsRecyclerFragment();
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.add(R.id.add_ings_fragment_cont,frag);
        trans.commit();

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorMainDark));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
