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
    View view;

    ArrayList<String> names;
    ArrayList<String> examples;
    ArrayList<Integer> numbersOfIngredients;


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
//        ImageView imageView;
//        if (convertView == null) {
//            w = gridView.getColumnWidth();
//            h = w;
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//          //  imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageView.getLayoutParams().width));
//            imageView.setLayoutParams(new GridView.LayoutParams(w, h));
//            imageView.setPadding(8, 8, 8, 8);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//
//        } else {
//            imageView = (ImageView) convertView;
//        }
//
//        imageView.setImageResource(imagesArray[position]);
//        return imageView;


// references to our images
//    private Integer[] imagesArray = {
//            R.drawable.category_meat, R.drawable.category_bird,
//            R.drawable.category_fish, R.drawable.category_seaproducts,
//            R.drawable.category_vegetables, R.drawable.category_fruits,
//            R.drawable.category_bakalei, R.drawable.category_crups,
//            R.drawable.category_milk, R.drawable.category_mashrooms,
//            R.drawable.category_zelen, R.drawable.category_nuts,
//            R.drawable.category_readyproducts
//    };


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
