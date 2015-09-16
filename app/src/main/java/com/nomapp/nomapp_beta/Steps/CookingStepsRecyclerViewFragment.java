package com.nomapp.nomapp_beta.Steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.nomapp.nomapp_beta.R;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class CookingStepsRecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int position;


    public static CookingStepsRecyclerViewFragment newInstance(Bundle savedInstanceState, int position) {
        return new CookingStepsRecyclerViewFragment(savedInstanceState, position);
    }

    public CookingStepsRecyclerViewFragment(Bundle savedInstanceState, int position){
        super.onCreate(savedInstanceState);
        this.position = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview_cooking_steps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        Intent intent = getActivity().getIntent();
        String nameOfSteps = intent.getStringExtra("cooking");

        int id = getResources().getIdentifier(nameOfSteps, "array", getActivity().getPackageName());
        String[] stepsArray = getResources().getStringArray(id);


        mAdapter = new RecyclerViewMaterialAdapter(new CookingStepsRecyclerViewAdapter(stepsArray[position]));
        mRecyclerView.setAdapter(mAdapter);


        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }
}
