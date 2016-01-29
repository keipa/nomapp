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

package com.nomapp.nomapp_beta.AddIngredients;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nomapp.nomapp_beta.CategoriesOfRecipes.FindedRecipesRecyclerAdapter;
import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SimpleHeaderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private LayoutInflater mInflater;
    private View mHeaderView;

    private OnItemTouchListener onItemTouchListener;

    ArrayList<String> names;
    ArrayList<Integer> IDs;


    public SimpleHeaderRecyclerAdapter(Context context, ArrayList<String> names, ArrayList<Integer> IDs,
                                       OnItemTouchListener onItemTouchListener, View headerView) {
        mInflater = LayoutInflater.from(context);
        this.names = names;
        this.IDs = IDs;
        this.onItemTouchListener = onItemTouchListener;
        mHeaderView = headerView;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return IDs.size();
        } else {
            return IDs.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        } else {
            return new ItemViewHolder(mInflater.inflate(R.layout.card_view_ingregient_color, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            position--;

            ((ItemViewHolder) viewHolder).name.setText(names.get(position));

            Cursor cursor =  Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                    new String[]
                            {Database.getIngredientId(), Database.getIngredientName(),
                                    Database.getIngredientIsChecked()},
                    null, null, null, null
                    , null);

            cursor.moveToFirst();
            cursor.moveToPosition(IDs.get(position) - 1);
            int isChecked = cursor.getInt(2);

            if (isChecked == 1) {
                ((ItemViewHolder) viewHolder).name.setBackgroundColor(viewHolder.itemView.getResources()
                        .getColor(R.color.chosenElement)); // второй вариант
                ((ItemViewHolder) viewHolder).name.setTextColor(viewHolder.itemView.getResources().getColor(R.color.white));
            } else{
                ((ItemViewHolder) viewHolder).name.setBackgroundColor(viewHolder.itemView.getResources()
                        .getColor(R.color.white)); // второй вариант
                ((ItemViewHolder) viewHolder).name.setTextColor(viewHolder.itemView.getResources().getColor(R.color.black));
            }

        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_of_ingredient_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onCardViewTap(v, getPosition());
                }
            });
        }
    }



    /**
     * Interface for the touch events in each item
     */
    public interface OnItemTouchListener {
        /**
         * Callback invoked when the user Taps one of the RecyclerView items
         *
         * @param view     the CardView touched
         * @param position the index of the item touched in the RecyclerView
         */
        public void onCardViewTap(View view, int position);

    }
}
