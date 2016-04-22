package com.dikulous.ric.orderapp.menu.item;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.model.OrderItem;
import com.dikulous.ric.orderapp.util.Globals;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;

import java.util.List;

public class MenuItemDetailsActivity extends AppCompatActivity {

    private static final String TAG = "Menu Item Details";
    private long mMenuItemPk;
    private MenuItemEntity mMenuItem;
    private MenuItemDetailsAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private Button mAddToOrderButton;
    private NumberPicker mNumberPicker;
    private RelativeLayout mAddToOrderLayout;
    private RelativeLayout mAddedToOrderLayout;

    private OrderDbHelper mOrderDbHelper;
    private MenuDbHelper mDbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_details);

        mMenuItemPk = getIntent().getLongExtra(Globals.EXTRA_MENU_PK, 0);
        Log.i(TAG, "menuItemPk: " + mMenuItemPk);
        mDbHelper = new MenuDbHelper(this);
        mMenuItem = mDbHelper.readMenuItemByPk(mMenuItemPk);
        mAdapter = new MenuItemDetailsAdapter(this, mMenuItem);
        mOrderDbHelper = new OrderDbHelper(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.ingredients_list);
        mAddToOrderButton = (Button) findViewById(R.id.add_to_order_button);
        mNumberPicker = (NumberPicker) findViewById(R.id.number_picker);
        mAddToOrderLayout = (RelativeLayout) findViewById(R.id.add_to_order_layout);
        mAddedToOrderLayout = (RelativeLayout) findViewById(R.id.added_to_order_layout);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(10);

        mAddToOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "BUtton clicked");
                mAddToOrderLayout.setVisibility(View.GONE);
                mAddedToOrderLayout.setVisibility(View.VISIBLE);
                List<String> ingredientsExcluded = mAdapter.getIngredientsExcluded();
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderFk(mOrderDbHelper.readCurrentOrderPk());
                orderItem.setIngredientsExcluded(ingredientsExcluded);
                orderItem.setMenuItemKeyString(mMenuItem.getKeyString());
                orderItem.setMenuItemFk(mMenuItemPk);
                orderItem.setAmount(mNumberPicker.getValue());
                mOrderDbHelper.insertOrderItem(orderItem);
            }
        });

    }
}
