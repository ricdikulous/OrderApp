package com.dikulous.ric.orderapp.menu.item;


import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class MenuItemActivity extends MenuAbstractActivity implements IngredientsExcludedDialog.IngredientsExcludedDialogListener{

    private static final String TAG = "Menu Item Act";

    private long mMenuItemPk;
    private MenuItemEntity mMenuItem;
    private MenuDbHelper mDbHelper;
    private OrderDbHelper mOrderDbHelper;

    private List<String> mIngredientsExcluded;

    private boolean[] mDialogIngredientsSelected;

    private ImageView mFoodImage;
    private TextView mFoodName;
    private TextView mDescription;
    private TextView mPrice;
    private TextView mIngredients;
    private TextView mAllergens;
    private EditText mSpecialRequest;
    private TextView mIngredientsExcludedTextView;
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
        mIngredientsExcludedTextView = (TextView) findViewById(R.id.ingredients_excluded);
        mSpecialRequest = (EditText) findViewById(R.id.special_request_edit_text);
        mNumberPicker = (NumberPicker) findViewById(R.id.number_picker);
        //mSelectAmount = (TextView) findViewById(R.id.select_amount);
        mAddToOrderButton = (Button) findViewById(R.id.add_to_order_button);

        mFoodName.setText(mMenuItem.getName());
        mDescription.setText(mMenuItem.getDescription());
        Glide.with(this).load(mMenuItem.getServingUrl()).asGif().crossFade().into(mFoodImage);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mMenuItem.getName());

        String priceString = getResources().getString(R.string.price);
        String ingredientsString = mMenuItem.getIngredients().toString().replaceAll("\\[|\\]", "").trim();
        mIngredients.setText("Ingredients: " + ingredientsString);
        if(mMenuItem.getAllergens() !=null){
            String allergensString = mMenuItem.getAllergens().toString().replaceAll("\\[|\\]", "").trim();
            if(allergensString.length()>0) {
                mAllergens.setText("Allergens: " + allergensString);
                mAllergens.setVisibility(View.VISIBLE);
            }
        }

        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(10);

        priceString = String.format(priceString, DisplayUtil.longCentsToCurrency(mMenuItem.getPrice()));
        mPrice.setText(priceString);

        mIngredientsExcluded = new ArrayList<>();

        mDialogIngredientsSelected = new boolean[mMenuItem.getIngredients().size()];
        makeDialogAllSelectedTrue();

    }

    private void makeDialogAllSelectedTrue() {
        for(int i=0;i<mDialogIngredientsSelected.length;i++){
            mDialogIngredientsSelected[i] = true;
        }
    }

    public void handleCustomiseIngredientsButton(View view){
        IngredientsExcludedDialog dialogFragment = IngredientsExcludedDialog.newInstance(mMenuItem.getIngredients().toArray(new String[0]), mDialogIngredientsSelected);
        dialogFragment.show(getSupportFragmentManager(), "IngredientsFragment");
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ordermenu, menu);
        return true;
    }*/

    @Override
    public void onIngredientsDialogFinish(ArrayList<Integer> selectedItems) {
        makeDialogAllSelectedTrue();
        mIngredientsExcluded = new ArrayList<>();
        for(int i=0;i<mMenuItem.getIngredients().size();i++){
            if(!selectedItems.contains(i)){
                mIngredientsExcluded.add(mMenuItem.getIngredients().get(i));
                mDialogIngredientsSelected[i] = false;
            }
        }
        if(mIngredientsExcluded.size()>0) {
            mIngredientsExcludedTextView.setVisibility(View.VISIBLE);
            mIngredientsExcludedTextView.setText("Ingredients excluded: " + DisplayUtil.listToString(mIngredientsExcluded));
        } else {
            mIngredientsExcludedTextView.setVisibility(View.GONE);
        }
    }

    public void handleAddToOrderButton(View view){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderFk(mOrderDbHelper.readCurrentOrderPk());
        orderItem.setMenuItemKeyString(mMenuItem.getKeyString());
        orderItem.setMenuItemFk(mMenuItemPk);
        orderItem.setIngredientsExcluded(mIngredientsExcluded);
        orderItem.setAmount(mNumberPicker.getValue());
        orderItem.setSpecialRequest(mSpecialRequest.getText().toString());
        mOrderDbHelper.insertOrderItem(orderItem);
        Toast.makeText(this, "Added to order", Toast.LENGTH_LONG).show();
        updateCartCount();
        Intent intent = new Intent(this, MenuItemActivity.class);
        intent.putExtra(Globals.EXTRA_MENU_PK, mMenuItemPk);
        startActivity(intent);
    }
}
