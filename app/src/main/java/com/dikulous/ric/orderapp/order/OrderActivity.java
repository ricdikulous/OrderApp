package com.dikulous.ric.orderapp.order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.model.OrderItem;
import com.dikulous.ric.orderapp.order.monitor.MonitorOrderActivity;
import com.dikulous.ric.orderapp.util.CurrencyUtil;
import com.dikulous.ric.orderapp.util.DisplayUtil;
import com.dikulous.ric.orderapp.util.Globals;
import com.example.ric.myapplication.backend.api.orderApi.OrderApi;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderEntity;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderReceiptEntity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.prefs.PreferencesFactory;

public class OrderActivity extends AppCompatActivity {

    private static final String TAG = "Order Activity";
    private OrderDbHelper mOrderDbHelper;
    private MenuDbHelper mMenuDbHelper;

    private RecyclerView mRecyclerView;
    private TextView mTotalPriceTextView;
    private Button mContinueOrderButton;

    private OrderItemsAdapter mAdapter;
    private long mOrderPk;

    private ProgressDialog mProgressDialog;

    private SharedPreferences mSharedPreferences;

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(Globals.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mOrderDbHelper = new OrderDbHelper(this);
        mMenuDbHelper = new MenuDbHelper(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mOrderPk = mOrderDbHelper.readCurrentOrderPk();

        mRecyclerView = (RecyclerView) findViewById(R.id.order_list);
        mTotalPriceTextView = (TextView) findViewById(R.id.total);
        mContinueOrderButton = (Button) findViewById(R.id.continue_order_button);

        mAdapter = new OrderItemsAdapter(this, mOrderDbHelper.readCurrentOrderItems(), mTotalPriceTextView, mContinueOrderButton);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Submitting");
        mProgressDialog.setMessage("Sending order...");
        mProgressDialog.setCancelable(false);

        mTotalPriceTextView.setText("Total: " + DisplayUtil.bigDecimalToCurrency(calculateTotal()));

        //Intent intent = new Intent(this, PayPalService.class);
        //intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //startService(intent);

    }

    private BigDecimal calculateTotal(){
        long total = 0;
        for(OrderItem orderItem:mOrderDbHelper.readCurrentOrderItems()){
            long price = mMenuDbHelper.readMenuItemByPk(orderItem.getMenuItemFk()).getPrice();
            total += orderItem.getAmount()*price;
        }
        return CurrencyUtil.longCentsToBigDecimal(total);
    }

    public void handleContinueOrderButton(View view){
        Intent intent = new Intent(this, AddressEntryActivity.class);
        intent.putExtra(Globals.EXTRA_ORDER_PK, mOrderPk);
        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        //stopService(new Intent(this, PayPalService.class));
        mOrderDbHelper.deleteOrderItemsWithZeroAmount();
        super.onDestroy();
    }
}
