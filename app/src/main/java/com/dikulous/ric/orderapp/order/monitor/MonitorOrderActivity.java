package com.dikulous.ric.orderapp.order.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.util.Globals;

public class MonitorOrderActivity extends AppCompatActivity {

    private static final String TAG = "Monitor Order";

    private RelativeLayout mOrderReceivedLayout;
    private RelativeLayout mPreparingOrderLayout;
    private RelativeLayout mCookingOrderLayout;
    private RelativeLayout mOrderDispatchedLayout;
    private ImageView mOrderReceivedImage;
    private ImageView mPreparingOrderImage;
    private ImageView mCookingOrderImage;
    private ImageView mOrderDispatchedImage;
    private TextView mOrderReceivedText;
    private TextView mPreparingOrderText;
    private TextView mCookingOrderText;
    private TextView mOrderDispatchedText;

    private MonitorOrderStep mReceivedStep;
    private MonitorOrderStep mPreparingStep;
    private MonitorOrderStep mCookingStep;
    private MonitorOrderStep mDispatchedStep;

    private int mCurrentStatus;
    private long mOrderPk;

    private OrderDbHelper mOrderDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_order);

        mOrderPk = getIntent().getLongExtra(Globals.EXTRA_ORDER_PK, 0);
        mOrderDbHelper = new OrderDbHelper(this);

        mOrderReceivedLayout = (RelativeLayout) findViewById(R.id.order_received_layout);
        mPreparingOrderLayout = (RelativeLayout) findViewById(R.id.preparing_order_layout);
        mCookingOrderLayout = (RelativeLayout) findViewById(R.id.cooking_order_layout);
        mOrderDispatchedLayout = (RelativeLayout) findViewById(R.id.order_dispatched_layout);

        mOrderReceivedImage = (ImageView) findViewById(R.id.order_received_image);
        mPreparingOrderImage = (ImageView) findViewById(R.id.preparing_order_image);
        mCookingOrderImage = (ImageView) findViewById(R.id.cooking_order_image);
        mOrderDispatchedImage = (ImageView) findViewById(R.id.order_dispatched_image);

        mOrderReceivedText = (TextView) findViewById(R.id.order_received_text);
        mPreparingOrderText = (TextView) findViewById(R.id.preparing_order_text);
        mCookingOrderText = (TextView) findViewById(R.id.cooking_order_text);
        mOrderDispatchedText = (TextView) findViewById(R.id.order_dispatched_text);

        mReceivedStep = new MonitorOrderStep(this, mOrderReceivedLayout, mOrderReceivedImage, mOrderReceivedText, R.drawable.ic_received_48dp, R.drawable.ic_received_white_48dp);
        mPreparingStep = new MonitorOrderStep(this, mPreparingOrderLayout, mPreparingOrderImage, mPreparingOrderText, R.drawable.ic_preparing_48dp, R.drawable.ic_preparing_white_48dp);
        mCookingStep = new MonitorOrderStep(this, mCookingOrderLayout, mCookingOrderImage, mCookingOrderText, R.drawable.ic_cooking_48dp, R.drawable.ic_cooking_white_48dp);
        mDispatchedStep = new MonitorOrderStep(this, mOrderDispatchedLayout, mOrderDispatchedImage, mOrderDispatchedText, R.drawable.ic_dispatched_48dp, R.drawable.ic_dispatched_white_48dp);

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
            mReceivedStep.setSelected();
            updateStatus();
        }
    };

    private void updateStatus(){
        mCurrentStatus = mOrderDbHelper.readOrderStatus(mOrderPk);
        Log.i(TAG, "current status: "+mCurrentStatus);
        resetUi();
        switch (mCurrentStatus){
            case Globals.ORDER_RECEIVED:
                mReceivedStep.setSelected();
                break;
            case Globals.ORDER_PREPARING:
                mPreparingStep.setSelected();
                break;
            case Globals.ORDER_COOKING:
                mCookingStep.setSelected();
                break;
            case Globals.ORDER_DISPATCHED:
                mDispatchedStep.setSelected();
                break;
        }
    }

    private void resetUi(){
        mReceivedStep.setDefault();
        mPreparingStep.setDefault();
        mCookingStep.setDefault();
        mDispatchedStep.setDefault();
    }
}
