package com.dikulous.ric.orderapp.order.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.util.Globals;

public class MonitorOrderActivity extends AppCompatActivity {

    private static final String TAG = "Monitor Order";
    private TextView mOrderReceived;
    private TextView mPreparingOrder;
    private TextView mCooking;
    private TextView mDispatched;

    private int mCurrentStatus;
    private long mOrderPk;

    private OrderDbHelper mOrderDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_order);

        mOrderPk = getIntent().getLongExtra(Globals.EXTRA_ORDER_PK, 0);
        mOrderDbHelper = new OrderDbHelper(this);

        mOrderReceived = (TextView) findViewById(R.id.order_received);
        mPreparingOrder = (TextView) findViewById(R.id.preparing_order);
        mCooking = (TextView) findViewById(R.id.cooking);
        mDispatched = (TextView) findViewById(R.id.dispatched);
    }

    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Globals.INTENT_ORDER_STATUS_CHANGED));
        updateStatus();
    }

    @Override
    public void onPause(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateStatus();
        }
    };

    private void updateStatus(){
        mCurrentStatus = mOrderDbHelper.readOrderStatus(mOrderPk);
        Log.i(TAG, "current status: "+mCurrentStatus);
        resetUi();
        switch (mCurrentStatus){
            case Globals.ORDER_RECEIVED:
                mOrderReceived.setText("here");
                break;
            case Globals.ORDER_PREPARING:
                mPreparingOrder.setText("here");
                break;
            case Globals.ORDER_COOKING:
                mCooking.setText("here");
                break;
            case Globals.ORDER_DISPATCHED:
                mDispatched.setText("here");
                break;
        }
    }

    private void resetUi(){
        //Reset everything back to original
    }
}
