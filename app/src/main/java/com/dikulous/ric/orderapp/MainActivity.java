package com.dikulous.ric.orderapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.dikulous.ric.orderapp.db.AddressDbHelper;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.gcm.RegistrationIntentService;
import com.dikulous.ric.orderapp.menu.MenuDownloadService;
import com.dikulous.ric.orderapp.menu.gallery.MenuGalleryActivity;
import com.dikulous.ric.orderapp.menu.gallery.MenuTabbedActivity;
import com.dikulous.ric.orderapp.util.Globals;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private OrderDbHelper mOrderDbHelper;
    private Button mCreateNewOrderButton;
    private Button mContinueOrderButton;
    private Button mTryAgainButton;
    private TextView mMenuDownloadText;
    private ProgressBar mProgressBar;
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
        mTryAgainButton = (Button) findViewById(R.id.try_again);
        mMenuDownloadText = (TextView) findViewById(R.id.menu_download_text);
        mProgressBar = (ProgressBar) findViewById(R.id.menu_download_spinner);
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
        //If it has been downloading for over 1 minute it is probably stuck and not actually downloading
        if(mSharedPreferences.getLong(Globals.EXTRA_DOWNLOAD_STARTED, 0) < new Date().getTime()-60000){
            mSharedPreferences.edit().putBoolean(Globals.EXTRA_IS_DOWNLOADING, false).apply();
        }
        if(mSharedPreferences.getBoolean(Globals.EXTRA_IS_DOWNLOADING, false) || mStartedDownload) {
            Log.i(TAG, "Is downloading!| mStartedDownload: " + mStartedDownload);
            setUiDownloading();
        } else if(mSharedPreferences.getBoolean(Globals.EXTRA_DOWNLOAD_SUCCESSFUL, true)) {
            setUiDownloadSuccessful();
        } else {
            setUiDownloadFailed();
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
        new AddressDbHelper(this).deleteAllAddresses();
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
                if(intent.getBooleanExtra(Globals.EXTRA_DOWNLOAD_SUCCESSFUL, true)){
                    setUiDownloadSuccessful();
                } else {
                    setUiDownloadFailed();
                }
                //createDownloadFailedDialog();
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

    private void createDownloadFailedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Please ensure you have a stable internet connection and try again")
                .setTitle("Failed to download menu");

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //startMenuDownload();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //startMenuDownload();
            }
        });
        dialog.show();
    }


    public void handleTryAgainButtonClick(View v){
        Intent intent = new Intent(this, MenuDownloadService.class);
        startService(intent);
        setUiDownloading();
    }

    private void setUiDownloading(){
        mCreateNewOrderButton.setEnabled(false);
        mContinueOrderButton.setEnabled(false);
        mMenuDownloadText.setText(R.string.menu_download);
        mProgressBar.setVisibility(View.VISIBLE);
        mMenuDownloadInfo.setVisibility(View.VISIBLE);
        mTryAgainButton.setVisibility(View.GONE);
    }

    private void setUiDownloadSuccessful(){
        mCreateNewOrderButton.setEnabled(true);
        mContinueOrderButton.setEnabled(true);
        mMenuDownloadInfo.setVisibility(View.GONE);
        mTryAgainButton.setVisibility(View.GONE);
    }

    private void setUiDownloadFailed(){
        mCreateNewOrderButton.setEnabled(false);
        mContinueOrderButton.setEnabled(false);
        mMenuDownloadText.setText(R.string.menu_download_failed);
        mProgressBar.setVisibility(View.GONE);
        mMenuDownloadInfo.setVisibility(View.VISIBLE);
        mTryAgainButton.setVisibility(View.VISIBLE);
    }
}
