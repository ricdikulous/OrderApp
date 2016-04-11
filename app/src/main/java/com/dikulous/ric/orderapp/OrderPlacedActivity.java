package com.dikulous.ric.orderapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OrderPlacedActivity extends AppCompatActivity {

    protected TextView mOrderReceivedTextview;
    protected TextView mOrderProcessedTextview;
    protected TextView mOrderDispatchedTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        mOrderReceivedTextview = (TextView) findViewById(R.id.order_received_textview);
        mOrderProcessedTextview = (TextView) findViewById(R.id.order_processed_textview);
        mOrderDispatchedTextview = (TextView) findViewById(R.id.order_dispatched_textview);
    }
}
