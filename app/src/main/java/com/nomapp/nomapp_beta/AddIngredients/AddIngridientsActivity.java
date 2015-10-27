package com.nomapp.nomapp_beta.AddIngredients;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.melnykov.fab.FloatingActionButton;
import com.nomapp.nomapp_beta.Categories.CategoriesActivity;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.Start.StartActivity;


/**
 * Created by antonid on 05.07.2015.
 */

public class AddIngridientsActivity extends FragmentActivity {

    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        AddIngredientsRecyclerFragment frag = new AddIngredientsRecyclerFragment();
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.add(R.id.add_ings_fragment_cont,frag);
        trans.commit();

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.notification));
        fab = (FloatingActionButton) findViewById(R.id.fabtohome);  //floating action button init
        setUpFAB();

    }

    View.OnClickListener onCircleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddIngridientsActivity.this, StartActivity.class);   //fab listner to the categories list activity
            startActivity(intent);
        }
    };

    void setUpFAB() {
        fab.setColorNormal(getResources().getColor(R.color.chosenElement));  //normal state color
        fab.setColorPressed(getResources().getColor(R.color.primary)); //pressed state color
        fab.setColorRipple(getResources().getColor(R.color.chosenElement));  //??? color
        fab.setOnClickListener(onCircleButtonClickListener);   //setting listner
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_animation);  //animation init
        fab.startAnimation(hyperspaceJumpAnimation);            //beautiful animation on the start(button appear on from the point)
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
