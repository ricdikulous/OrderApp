package com.dikulous.ric.orderapp.menu.item;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.menu.MenuAbstractActivity;
import com.dikulous.ric.orderapp.model.OrderItem;
import com.dikulous.ric.orderapp.util.CurrencyUtil;
import com.dikulous.ric.orderapp.util.DisplayUtil;
import com.dikulous.ric.orderapp.util.Globals;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntityCollection;

public class MenuItemActivity extends MenuAbstractActivity{

    private static final String TAG = "Menu Item Act";

    private long mMenuItemPk;
    private MenuItemEntity mMenuItem;
    private MenuDbHelper mDbHelper;
    private OrderDbHelper mOrderDbHelper;

    private ImageView mFoodImage;
    private TextView mFoodName;
    private TextView mDescription;
    private TextView mPrice;
    private TextView mIngredients;
    private TextView mAllergens;
    private NumberPicker mNumberPicker;
    private TextView mSelectAmount;
    private Button mAddToOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        mMenuItemPk = getIntent().getLongExtra(Globals.EXTRA_MENU_PK, 0);
        mDbHelper = new MenuDbHelper(this);
        mOrderDbHelper = new OrderDbHelper(this);
        mMenuItem = mDbHelper.readMenuItemByPk(mMenuItemPk);

        mFoodImage = (ImageView) findViewById(R.id.food_image);
        mFoodName = (TextView) findViewById(R.id.food_name);
        mDescription = (TextView) findViewById(R.id.description);
        mPrice = (TextView) findViewById(R.id.price);
        mIngredients = (TextView) findViewById(R.id.ingredients);
        mAllergens = (TextView) findViewById(R.id.allergens);
        //mNumberPicker = (NumberPicker) findViewById(R.id.number_picker);
        //mSelectAmount = (TextView) findViewById(R.id.select_amount);
        //mAddToOrderButton = (Button) findViewById(R.id.add_to_order_button);

        mFoodName.setText(mMenuItem.getName());
        mDescription.setText(mMenuItem.getDescription());
        Glide.with(this).load(mMenuItem.getServingUrl()).asGif().crossFade().into(mFoodImage);

        String priceString = getResources().getString(R.string.price);
        String ingredientsString = mMenuItem.getIngredients().toString().replaceAll("\\[|\\]", "").trim();
        mIngredients.setText("Ingredients: "+ingredientsString);
        if(mMenuItem.getAllergens() !=null){
            String allergensString = mMenuItem.getAllergens().toString().replaceAll("\\[|\\]", "").trim();
            if(allergensString.length()>0) {
                mAllergens.setText("Allergens: " + allergensString);
                mAllergens.setVisibility(View.VISIBLE);
            }
        }
        priceString = String.format(priceString, DisplayUtil.longCentsToCurrency(mMenuItem.getPrice()));
        mPrice.setText(priceString);
    }

    public void handleMenuItemSelect(View view){
        Intent intent = new Intent(this, MenuItemDetailsActivity.class);
        intent.putExtra(Globals.EXTRA_MENU_PK, mMenuItemPk);
        startActivity(intent);
    }

}
