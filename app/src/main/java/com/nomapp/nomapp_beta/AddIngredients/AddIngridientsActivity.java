package com.nomapp.nomapp_beta.AddIngredients;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.view.ViewHelper;
import com.nomapp.nomapp_beta.AllRecipes.AllRecipesActivity;
import com.nomapp.nomapp_beta.CategoriesOfRecipes.CategoriesOfRecipesActivity;
import com.nomapp.nomapp_beta.Database.Database;
import com.nomapp.nomapp_beta.NavigationDrawer.NavDrawerListAdapter;
import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.Start.StartActivity;

import java.util.ArrayList;


/**
 * Created by antonid on 05.07.2015.
 */

public class AddIngridientsActivity extends android.support.v7.app.AppCompatActivity implements ObservableScrollViewCallbacks{
    ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;

    ObservableRecyclerView recyclerView;

    Toolbar mToolbar;

    FloatingActionButton fab;

    private static int NUM_OF_ITEMS = 0;

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
   // private View mImageView;
    private ImageView image;
    private View mOverlayView;
    private View mRecyclerViewBackground;
    private TextView mTitleView;
    private int mActionBarSize;


    private int mFlexibleSpaceImageHeight;
    private ArrayList<String> forIngridients;

    private ArrayList<Integer> IDs;

    String title;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);
        image = (ImageView)findViewById(R.id.bigImageOfCategory);

        fillIngridients();

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = getActionBarSize();

        recyclerView = (ObservableRecyclerView) findViewById(R.id.recycler);

        final View headerView = LayoutInflater.from(this).inflate(R.layout.recycler_header, null);
        headerView.post(new Runnable() {
            @Override
            public void run() {
                headerView.getLayoutParams().height = mFlexibleSpaceImageHeight;
            }
        });

        setUpRecyclerView(headerView);

      //  mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);

        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(title);
        setTitle(null);

        // mRecyclerViewBackground makes RecyclerView's background except header view.
        mRecyclerViewBackground = findViewById(R.id.list_background);

        //since you cannot programmatically add a header view to a RecyclerView we added an empty view as the header
        // in the adapter and then are shifting the views OnCreateView to compensate
        final float scale = 1 + MAX_TEXT_SCALE_DELTA;
        mRecyclerViewBackground.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationY(mRecyclerViewBackground, mFlexibleSpaceImageHeight);
            }
        });
        ViewHelper.setTranslationY(mOverlayView, mFlexibleSpaceImageHeight);
        mTitleView.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationY(mTitleView, (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale));
                ViewHelper.setPivotX(mTitleView, 0);
                ViewHelper.setPivotY(mTitleView, 0);
                ViewHelper.setScaleX(mTitleView, scale);
                ViewHelper.setScaleY(mTitleView, scale);
            }
        });


   //     Window window = getWindow();
//        window.setStatusBarColor(getResources().getColor(R.color.notification));
        fab = (FloatingActionButton) findViewById(R.id.fabtohome);  //floating action button init
        setUpFAB();
        setUpNavigationDraver();

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


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(image, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mRecyclerViewBackground, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle();
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle() {
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
        }
    }



    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    private Integer[] imagesArray = {
            R.drawable.in_category_meat,
            R.drawable.in_category_bird,
            R.drawable.in_category_fish,
            R.drawable.in_category_sea,
            R.drawable.in_category_veget,
            R.drawable.in_category_fruit,
            R.drawable.in_category_bakalei,
            R.drawable.in_category_bob,
            R.drawable.in_category_milk,
            R.drawable.in_category_mashrooms,
            R.drawable.in_category_green,
            R.drawable.in_category_nuts,
            R.drawable.in_category_ready
    };


    protected void setUpRecyclerView(View headerView) {

        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);


        SimpleHeaderRecyclerAdapter.OnItemTouchListener itemTouchListener = new SimpleHeaderRecyclerAdapter.OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                position--;
                Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                        new String[]
                                {Database.getIngredientId(), Database.getIngredientName(),
                                        Database.getIngredientIsChecked()},
                        null, null, null, null
                        , null);
                Log.w("MY_LOG", "kek");
                cursor.moveToFirst();
                cursor.moveToPosition(IDs.get(position) - 1);
                int isChecked = cursor.getInt(2);
                ((TextView)view.findViewById(R.id.name_of_ingredient_tv)).setTextColor(getResources().getColor(R.color.black));
                if (isChecked == 0) {
                    Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getIngredientsTableName()
                            + " SET checked=1 WHERE _id=" + IDs.get(position) + ";");
                    (view.findViewById(R.id.name_of_ingredient_tv)).setBackgroundColor(getResources().getColor(R.color.chosenElement));
                    ((TextView)view.findViewById(R.id.name_of_ingredient_tv)).setTextColor(getResources().getColor(R.color.white));
                    //  ((TextView) view).setTextColor(getResources().getColor(R.color.chosenElement));
                    Log.d("MY_TAG", "Checked position " + IDs.get(position));

                } else {
                    Database.getDatabase().getGeneralDb().execSQL("UPDATE " + Database.getIngredientsTableName()
                            + " SET checked=0 WHERE _id=" + IDs.get(position) + ";");
                    (view.findViewById(R.id.name_of_ingredient_tv)).setBackgroundColor(getResources().getColor(R.color.white));
                    ((TextView)view.findViewById(R.id.name_of_ingredient_tv)).setTextColor(getResources().getColor(R.color.black));
                    Log.d("MY_TAG", "Unchecked position " + IDs.get(position));

                }
                cursor.close();
            }
        };

        recyclerView.setAdapter(new SimpleHeaderRecyclerAdapter(this, forIngridients, IDs, itemTouchListener, headerView));



    }


    void setUpNavigationDraver()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.notification));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        mDrawerList = (ListView) findViewById(R.id.nav_drawer_list_view);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new NavDrawerListAdapter(this));
        // Click events for Navigation Drawer (now available only on start screen)
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


    }




    //Извлечение элментов из базы данных
    private void fillIngridients() { //
        forIngridients = new ArrayList<>();
        IDs = new ArrayList<>();

        Cursor cursor = Database.getDatabase().getGeneralDb().query(Database.getIngredientsTableName(),
                new String[]
                        {Database.getIngredientId(), Database.getIngredientName(),
                                Database.getIngredientIsChecked()},
                null, null, null, null
                , null);

        Cursor categoryCursor = Database.getDatabase().getGeneralDb().query(Database.getCategoriesTableName(),
                new String[]
                        {Database.getCategoriesId(), Database.getCategoryName(),
                                Database.getCategoryIngredients()},
                null, null, null, null
                , null);

        categoryCursor.moveToFirst();
        Intent intent = getIntent();
        categoryCursor.moveToPosition(intent.getIntExtra("numberOfCategory", 0) - 1);

        image.setImageResource(imagesArray[intent.getIntExtra("numberOfCategory", 0)-1]);   //получение номера по ключу numberOfCategory из intent
        title = categoryCursor.getString(1);

        IDs = parse(categoryCursor.getString(2));
        categoryCursor.close();

        cursor.moveToFirst();

        int size = IDs.size();
        NUM_OF_ITEMS = size;
        for (int counter = 0; counter < size; counter++) {
            cursor.moveToPosition(IDs.get(counter) - 1);
            forIngridients.add(cursor.getString(1));
        }

        cursor.close();

    }

    private ArrayList<Integer> parse(String toConvert) {
        ArrayList<Integer> converted = new ArrayList<Integer>();

        int counter = 0;
        int factor = 1;
        int currentIngridient = 0;
        int size = toConvert.length();
        for (counter = 0; counter < size; counter++) {
            while (toConvert.charAt(counter) != ',' && toConvert.charAt(counter) != '.') {//TODO
                currentIngridient += (toConvert.charAt(counter) - '0') * factor;
                factor *= 10;
                counter++;
            }
            factor = 1;
            converted.add(currentIngridient);
            currentIngridient = 0;
        }
        return converted;
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            navDrawerSelectItem(i);
        }
    }

    //
    private void navDrawerSelectItem(int position){
        switch (position){
            case 0:
                mDrawerLayout.closeDrawers();
                Intent toStartActivity = new Intent(AddIngridientsActivity.this, StartActivity.class);
                startActivity(toStartActivity);
                break;
            case 1:
                mDrawerLayout.closeDrawers();
                Intent toAllRecipes = new Intent(AddIngridientsActivity.this, CategoriesOfRecipesActivity.class);
                startActivity(toAllRecipes);
                break;
            default: break;
        }
    }
}
