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

package com.nomapp.nomapp_beta.RecipePreview;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nomapp.nomapp_beta.Database;
import com.nomapp.nomapp_beta.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SimpleHeaderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_INFO = 1;
    private static final int VIEW_TYPE_DESCRIPTION = 2;
    private static final int VIEW_TYPE_INGREDIENTS = 3;

    private LayoutInflater mInflater;
    private ArrayList<String> mItems;
    private View mHeaderView;

    private int numberOfRecipe;

    private Context ctx;

    public SimpleHeaderRecyclerAdapter(Context context, ArrayList<String> items, View headerView, int numberOfRecipe, Context ctx) {
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mHeaderView = headerView;
        this.numberOfRecipe = numberOfRecipe;
        this.ctx = ctx;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mItems.size();
        } else {
            return mItems.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {

   /*     switch (position){
            case 0:
                return VIEW_TYPE_DESCRIPTION;

            case 2:
                return VIEW_TYPE_DESCRIPTION;

            case 1:
                return VIEW_TYPE_INFO;

            case 3:
                return VIEW_TYPE_INGREDIENTS;

            default:
                return VIEW_TYPE_HEADER;
        }*/
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Cursor cursor = Database.getDatabase().getRecipes().query(Database.getRecipesTableName(),
                new String[]
                        {Database.getRecipesId(), Database.getRecipesName(), Database.getRecipesIngridients(),
                                Database.getRecipesHowToCook(),Database.getRecipesIsAvailable(),
                                Database.getRecipesNumberOfSteps(), Database.getRecipesTimeForCooking(),
                                Database.getRecipesDescription(), Database.getRecipesNumberOfPersons(),
                                Database.getRecipesNumberOfIngridients()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        cursor.moveToPosition(numberOfRecipe);

        switch (viewType){
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(mHeaderView);

            case VIEW_TYPE_INFO:
                return new InfoViewHolder(mInflater.inflate(R.layout.card_view_note, parent, false),
                        cursor.getInt(5), cursor.getInt(8));

            case VIEW_TYPE_DESCRIPTION:
                return new DescriptionViewHolder(mInflater.inflate(R.layout.card_view_desc, parent, false));

            case VIEW_TYPE_INGREDIENTS:
                return new IngredientsViewHolder(mInflater.inflate(R.layout.card_recipe_preview_ingridients, parent, false),
                        cursor.getString(9), ctx);

            default:
                return new HeaderViewHolder(mHeaderView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof IngredientsViewHolder) {
          //  ((ItemViewHolder) viewHolder).textView.setText(mItems.get(position - 1));
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    static class InfoViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        TextView personText;

        public InfoViewHolder(View view, int time, int persons) {
            super(view);
            timeText = (TextView) view.findViewById(R.id.timeText);
            personText = (TextView) view.findViewById(R.id.personsText);
            personText.setText(persons+"");
            timeText.setText(time+"m");
        }
    }

    static class IngredientsViewHolder extends RecyclerView.ViewHolder {
     TextView ingredientsTextView;

        public IngredientsViewHolder(View view ,String path, Context ctx) {
            super(view);
            ingredientsTextView= (TextView) view.findViewById(R.id.numberOfIngridientsTextView);
            int id = ctx.getResources().getIdentifier(path, "string", ctx.getPackageName());
            ingredientsTextView.setText(ctx.getResources().getText(id));
        }
    }

    static class DescriptionViewHolder extends RecyclerView.ViewHolder {
        public DescriptionViewHolder(View view) {
            super(view);
        }
    }
}
