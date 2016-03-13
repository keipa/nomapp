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
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;

public class SimpleHeaderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_INFO = 1;
    private static final int VIEW_TYPE_DESCRIPTION = 2;
    private static final int VIEW_TYPE_INGREDIENTS = 3;
    private static final int VIEW_TYPE_GO_COOK = 4;

    private LayoutInflater mInflater;
    private View mHeaderView;

    private int numberOfRecipe;
    private int numberOfCards;

    private String timeForCooking;
    private String ingredientsForRecipe;
    private String measureOfTime;
    private int numberOfPortionsForRecipe;
    private int numberOfSteps;
    private int numberOfIngredients;

    private OnItemTouchListener onItemTouchListener;


    private Context ctx;

    public SimpleHeaderRecyclerAdapter(Context context, int numberOfCards, View headerView, int numberOfRecipe,
                                       OnItemTouchListener onItemTouchListener) {
        mInflater = LayoutInflater.from(context);
        mHeaderView = headerView;

        this.numberOfCards = numberOfCards;
        this.numberOfRecipe = numberOfRecipe;
        this.ctx = context;
        this.onItemTouchListener = onItemTouchListener;

        initDataForRecipe();
    }

    private void initDataForRecipe() {
        Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getRecipesTableName(),
                new String[]
                        {Database.getRecipesName(), Database.getRecipesTimeForCooking(),
                                Database.getRecipesNumberOfEveryIng(), Database.getRecipesNumberOfPersons(),
                                Database.getRecipesMeasureForTime(), Database.getRecipesNumberOfSteps(),
                                Database.getRecipesNumberOfIngredients()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        cursor.moveToPosition(numberOfRecipe - 1);

        timeForCooking = cursor.getString(1);
        ingredientsForRecipe = cursor.getString(2);
        numberOfPortionsForRecipe = cursor.getInt(3);
        measureOfTime = cursor.getString(4);
        numberOfSteps = cursor.getInt(5);
        numberOfIngredients = cursor.getInt(6);
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return numberOfCards;
        } else {
            return numberOfCards + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType){
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(mHeaderView);

            case VIEW_TYPE_INFO:
                return new InfoViewHolder(mInflater.inflate(R.layout.card_view_note_refactor, parent, false));

            case VIEW_TYPE_DESCRIPTION:
                return new DescriptionViewHolder(mInflater.inflate(R.layout.card_view_desc, parent, false));

            case VIEW_TYPE_INGREDIENTS:
                return new IngredientsViewHolder(mInflater.inflate(R.layout.card_recipe_preview_ingridients, parent, false),
                        ingredientsForRecipe, ctx);
            case VIEW_TYPE_GO_COOK:
                return new CookingButtonViewHolder(mInflater.inflate(R.layout.go_cook_button, parent, false));

            default:
                return new HeaderViewHolder(mHeaderView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int viewType) {
        if (viewHolder instanceof IngredientsViewHolder) {

            if (viewType == VIEW_TYPE_INFO)
            {
                ((InfoViewHolder)viewHolder).portions.setText(numberOfPortionsForRecipe + "");
                ((InfoViewHolder)viewHolder).timeText.setText(timeForCooking);
            }
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    class InfoViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView portions;
        TextView products;
        TextView steps;

        TextView stepsText;
        TextView productsText;
        TextView portionsText;
        TextView timeText;

        public InfoViewHolder(View view) {
            super(view);

            time = (TextView) view.findViewById(R.id.time_tv);
            portions = (TextView) view.findViewById(R.id.portions_tv);
            steps = (TextView) view.findViewById(R.id.steps_tv);
            products = (TextView) view.findViewById(R.id.number_of_products_tv);

            stepsText = (TextView) view.findViewById(R.id.steps_text);
            productsText = (TextView) view.findViewById(R.id.text_ings);
            portionsText = (TextView) view.findViewById(R.id.portions_text);
            timeText = (TextView) view.findViewById(R.id.measure_of_time_tv);

            time.setText(timeForCooking + ""/* + " " + measureOfTime.toLowerCase().charAt(0) */);
            portions.setText(numberOfPortionsForRecipe + "");
            steps.setText(numberOfSteps + "");
            products.setText(numberOfIngredients + "");

            stepsText.setText(getStepsWithEnding(numberOfSteps));
            //productsText.setText();

        }

        private String getStepsWithEnding(int count) //TODO �������, ����� ��� �������� ������������, ��� ���������� � ������ ������������
        {
            int modNumberOAR =count  % 10;
            if (modNumberOAR >=2 && modNumberOAR <=4){
                return ctx.getResources().getString(R.string.two_or_four_steps);

            }
            if (modNumberOAR >=5 || modNumberOAR == 0){
                return ctx.getResources().getString(R.string.more_steps);
            }
            if (modNumberOAR == 1){
                return ctx.getResources().getString(R.string.one_step);
            }
            return null;
        }


    }

    static class IngredientsViewHolder extends RecyclerView.ViewHolder {
     TextView ingredientsTextView;

        public IngredientsViewHolder(View view ,String path, Context ctx) {
            super(view);
            ingredientsTextView= (TextView) view.findViewById(R.id.numberOfIngridientsTextView);
            int idOfStringWithIngs = ctx.getResources().getIdentifier(path, "string", ctx.getPackageName());
            ingredientsTextView.setText(ctx.getResources().getText(idOfStringWithIngs));
        }
    }

    static class DescriptionViewHolder extends RecyclerView.ViewHolder {
        public DescriptionViewHolder(View view) {
            super(view);
        }
    }

    class CookingButtonViewHolder extends RecyclerView.ViewHolder{
        public CookingButtonViewHolder (View view)
        {
            super(view);
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
