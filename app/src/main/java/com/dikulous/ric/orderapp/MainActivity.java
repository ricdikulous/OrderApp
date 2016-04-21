package com.dikulous.ric.orderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.gcm.RegistrationIntentService;
import com.dikulous.ric.orderapp.menu.MenuDownloadService;
import com.dikulous.ric.orderapp.menu.gallery.MenuGalleryActivity;
import com.dikulous.ric.orderapp.menu.gallery.MenuTabbedActivity;
import com.dikulous.ric.orderapp.util.Globals;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private OrderDbHelper mOrderDbHelper;
    private Button mCreateNewOrderButton;
    private Button mContinueOrderButton;
    private RelativeLayout mMenuDownloadInfo;
    private SharedPreferences mSharedPreferences;
    private boolean mStartedDownload;
    private static  final String TAG = "Main act";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //mSharedPreferences.edit().putBoolean(Globals.EXTRA_IS_DOWNLOADING, false).apply();

        mCreateNewOrderButton = (Button) findViewById(R.id.create_new_order);
        mContinueOrderButton = (Button) findViewById(R.id.continue_order);
        mMenuDownloadInfo = (RelativeLayout) findViewById(R.id.menu_download_info);

        if(checkPlayServices()) {
            Intent gcmIntent = new Intent(this, RegistrationIntentService.class);
            startService(gcmIntent);
        }
        Intent intent = new Intent(this, MenuDownloadService.class);
        startService(intent);
        mStartedDownload = true;

        mCreateNewOrderButton.setEnabled(false);
        mContinueOrderButton.setEnabled(false);
        mMenuDownloadInfo.setVisibility(View.VISIBLE);
        //new MenuDbHelper(this).deleteAllMenuItems();
        mOrderDbHelper = new OrderDbHelper(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mSharedPreferences.getBoolean(Globals.EXTRA_IS_DOWNLOADING, false) || mStartedDownload) {
            Log.i(TAG, "Is downloading!| mStartedDownload: "+mStartedDownload);
            mCreateNewOrderButton.setEnabled(false);
            mContinueOrderButton.setEnabled(false);
            mMenuDownloadInfo.setVisibility(View.VISIBLE);
        } else {
            mCreateNewOrderButton.setEnabled(true);
            mContinueOrderButton.setEnabled(true);
            mMenuDownloadInfo.setVisibility(View.GONE);
        }
        mStartedDownload = false;
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Globals.INTENT_DOWNLOAD_FINISHED));

    }

    @Override
    protected void onPause(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    public void handleCreateNewOrderButtonClick(View view){
        mOrderDbHelper.deleteCurrentOrder();
        mOrderDbHelper.insertNewOrder();
        startMenu();
    }

    public void handleContinueOrderButtonClick(View view){
        if(mOrderDbHelper.readCurrentOrderPk() == 0){
            mOrderDbHelper.insertNewOrder();
        }
        startMenu();
    }

    private void startMenu(){
        Intent intent = new Intent(this, MenuTabbedActivity.class);
        startActivity(intent);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Globals.INTENT_DOWNLOAD_FINISHED)){
                mCreateNewOrderButton.setEnabled(true);
                mContinueOrderButton.setEnabled(true);
                mMenuDownloadInfo.setVisibility(View.GONE);
            }
        }
    };

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                /*apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();*/
                Log.i(TAG, "User sovable error");
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
