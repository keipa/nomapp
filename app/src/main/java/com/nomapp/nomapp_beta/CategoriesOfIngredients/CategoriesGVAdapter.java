package com.nomapp.nomapp_beta.CategoriesOfIngredients;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.CategoriesOfRecipes.FindedRecipesRecyclerAdapter;
import com.nomapp.nomapp_beta.R;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ��������� ���� on 10.09.2015.
 */
public class CategoriesGVAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    ArrayList<String> names;
    ArrayList<String> examples;
    ArrayList<Integer> numbersOfIngredients;

    private Integer[] imagesArray = {
            R.drawable.meat,
            R.drawable.bird,
            R.drawable.fish,
            R.drawable.sea,
            R.drawable.veget,
            R.drawable.fruit,
            R.drawable.backaley,
            R.drawable.beans,
            R.drawable.milk,
            R.drawable.mashroom,
            R.drawable.grass,
            R.drawable.nuts,
            R.drawable.ready
    };

    private OnItemTouchListener onItemTouchListener;

   // TypedArray imagesArray;


    public CategoriesGVAdapter(Context c, ArrayList<String> names,
                               ArrayList<Integer> numbersOfIngredients, ArrayList<String> examples,
                               OnItemTouchListener onItemTouchListener)
    {
        mContext = c;
        this.names = names;
        this.numbersOfIngredients = numbersOfIngredients;
        this.examples = examples;
        this.onItemTouchListener = onItemTouchListener;

        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return 13;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = null;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.card_material_category, parent, false);
            TextView name = (TextView) convertView.findViewById(R.id.name_of_category);
            TextView numOfIngs = (TextView) convertView.findViewById(R.id.count_of_products);
            TextView example = (TextView) convertView.findViewById(R.id.category_example);
            ImageView icon = (ImageView)  convertView.findViewById(R.id.image_of_category);
            icon.setImageResource(imagesArray[position]);
            name.setText(names.get(position));
            numOfIngs.setText(numbersOfIngredients.get(position) + " " + setEnding(numbersOfIngredients.get(position))); //TODO
            example.setText(examples.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onCardViewTap(v, position);
                }
            });
        }
        return convertView;

    }

    private String setEnding(int number)
    {
        String toReturn = "";
        int modNumberOAR =number % 10;
        if (modNumberOAR >=2 && modNumberOAR <=4){
            toReturn = mContext.getResources().getString(R.string.two_or_four_products);

        }
        if (modNumberOAR >=5 || modNumberOAR == 0){
            toReturn = mContext.getResources().getString(R.string.more_products);
        }
        if (modNumberOAR == 1){
            toReturn = mContext.getResources().getString(R.string.one_product);
        }
        return toReturn;
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
