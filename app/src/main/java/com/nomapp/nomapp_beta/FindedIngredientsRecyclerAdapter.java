package com.nomapp.nomapp_beta;

/**
 * Created by Антоненко Илья on 06.09.2015.
 */
/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FindedIngredientsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<String> mItems;

    public FindedIngredientsRecyclerAdapter (Context context, ArrayList<String> items) {
        mInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IngredientsViewHolder(mInflater.inflate(R.layout.card_ingredient, parent, false), mItems.get(viewType)) ;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof IngredientsViewHolder) {
            //  ((ItemViewHolder) viewHolder).textView.setText(mItems.get(position - 1));
        }
    }


    static class IngredientsViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName;

        public IngredientsViewHolder(View view, String text) {
            super(view);
            ingredientName = (TextView) view.findViewById(R.id.nameOfIngTextView);
            ingredientName.setText(text);
        }
    }

}

