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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.model.OrderItem;
import com.dikulous.ric.orderapp.order.monitor.MonitorOrderActivity;
import com.dikulous.ric.orderapp.util.CurrencyUtil;
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

        mAdapter = new OrderItemsAdapter(this, mOrderDbHelper.readCurrentOrderItems(), mTotalPriceTextView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Submitting");
        mProgressDialog.setMessage("Sending order...");
        mProgressDialog.setCancelable(false);

        mTotalPriceTextView.setText("Total: $" + calculateTotal());

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

    }


    private BigDecimal calculateTotal(){
        long total = 0;
        for(OrderItem orderItem:mOrderDbHelper.readCurrentOrderItems()){
            long price = mMenuDbHelper.readMenuItemByPk(orderItem.getMenuItemFk()).getPrice();
            total += orderItem.getAmount()*price;
        }
        return CurrencyUtil.longCentsToBigDecimal(total);
    }

    public void handleSubmitOrderButton(View view){
        Log.i(TAG, "Button clicked");
        //new SendOrderAsyncTask().execute(this);
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(calculateTotal(), "AUD", "Order",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    Log.i(TAG, confirm.getProofOfPayment().getPaymentId());
                    mOrderDbHelper.updateWithPaymentId(confirm.getProofOfPayment().getPaymentId(), mOrderPk);
                    mProgressDialog.show();
                    new SendOrderAsyncTask().execute(this);
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }


    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public class SendOrderAsyncTask extends AsyncTask<Context, Void, Integer> {
        private static final String TAG = "Order Asnc";
        private Context mContext;
        private OrderApi mOrderApi;
        private OrderDbHelper mOrderDbHelper;

        @Override
        protected Integer doInBackground(Context... params) {

            if (mOrderApi == null) {
                OrderApi.Builder builder = new OrderApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://endpointstutorial-1119.appspot.com//_ah/api/");
                mOrderApi = builder.build();
            }

            mContext = params[0];
            mOrderDbHelper = new OrderDbHelper(mContext);
            OrderEntity orderEntity = mOrderDbHelper.readOrderEntity();
            orderEntity.setRegistrationToken(mSharedPreferences.getString(Globals.GCM_TOKEN, "no token"));
            Log.i(TAG, "sending order");
            try {
                OrderReceiptEntity orderReceipt = mOrderApi.putOrder(orderEntity).execute();
                mOrderDbHelper.updateOrderReceived(mOrderDbHelper.readCurrentOrderPk(), orderReceipt);
                //MenuTypesEntity menuTypes = mMenuApi.getMenuTypes().execute();
                //mMenuTypesEntity = menuTypes;
                //Log.i(TAG, "Got menu types: " + menuTypes.getMenuTypes().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.i(TAG, "done sending");
            mProgressDialog.cancel();
            Intent intent = new Intent(mContext, MonitorOrderActivity.class);
            intent.putExtra(Globals.EXTRA_ORDER_PK, mOrderPk);
            mContext.startActivity(intent);
            //mDbHelper.insertMenuTypes(mMenuTypesEntity);
            //new GetMenuItemsAsync().execute(mContext);
        }
    }
}
