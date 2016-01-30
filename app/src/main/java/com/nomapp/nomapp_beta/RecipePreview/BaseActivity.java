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

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.nomapp.nomapp_beta.AllRecipes.AllRecipesRecyclerAdapter;
import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.Steps.TabsActivity;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity {
    private static final int NUM_OF_ITEMS = 4;
    private static final int NUM_OF_ITEMS_FEW = 4;

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }


    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }


    public static ArrayList<String> getDummyData(int num) {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            items.add("Item " + i);
        }
        return items;
    }



    protected void setDummyDataWithHeader(RecyclerView recyclerView, int headerHeight) {
        View headerView = new View(this);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, headerHeight));
        headerView.setMinimumHeight(headerHeight);
        // This is required to disable header's list selector effect
        headerView.setClickable(true);
        setDummyDataWithHeader(recyclerView, headerView);
    }

    protected void setDummyDataWithHeader(RecyclerView recyclerView, View headerView) {

        SimpleHeaderRecyclerAdapter.OnItemTouchListener itemTouchListener = new SimpleHeaderRecyclerAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                Intent data = getIntent();
                Intent intent = new Intent(BaseActivity.this, TabsActivity.class);
                intent.putExtra("numberOfRecipe", data.getIntExtra("numberOfRecipe", 0));
                startActivity(intent);
            }
        };


        Intent data = getIntent();
        recyclerView.setAdapter(new SimpleHeaderRecyclerAdapter(this, NUM_OF_ITEMS, headerView, data.getIntExtra("numberOfRecipe", 0), itemTouchListener));
    }

}
