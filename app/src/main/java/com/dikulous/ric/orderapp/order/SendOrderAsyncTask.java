package com.dikulous.ric.orderapp.order;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.example.ric.myapplication.backend.api.menuApi.MenuApi;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuTypesEntity;
import com.example.ric.myapplication.backend.api.orderApi.OrderApi;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderEntity;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderReceiptEntity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by ric on 8/04/16.
 */
public class SendOrderAsyncTask extends AsyncTask<Context, Void, Integer> {
    private static final String TAG = "Order Asnc";
    private Context mContext;
    private OrderApi mOrderApi;
    private OrderDbHelper mOrderDbHelper;

    @Override
    protected Integer doInBackground(Context... params) {

        if(mOrderApi == null) {
            OrderApi.Builder builder = new OrderApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://endpointstutorial-1119.appspot.com//_ah/api/");
            mOrderApi = builder.build();
        }

        mContext = params[0];
        mOrderDbHelper = new OrderDbHelper(mContext);
        OrderEntity orderEntity = mOrderDbHelper.readOrderEntity();
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
        //mDbHelper.insertMenuTypes(mMenuTypesEntity);
        //new GetMenuItemsAsync().execute(mContext);
    }
}

