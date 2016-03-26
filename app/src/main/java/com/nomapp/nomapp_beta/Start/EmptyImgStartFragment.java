package com.nomapp.nomapp_beta.Start;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nomapp.nomapp_beta.R;

/**
 * Created by antonid on 24.09.2015.
 */
public class EmptyImgStartFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_empty_img_start, null);

        return v;
    }

}
