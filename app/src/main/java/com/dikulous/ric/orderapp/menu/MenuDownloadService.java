package com.dikulous.ric.orderapp.menu;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.dikulous.ric.orderapp.MainActivity;
import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.util.Globals;
import com.example.ric.myapplication.backend.api.menuApi.MenuApi;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntityCollection;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuTypesEntity;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuVersionEntity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

public class MenuDownloadService extends Service {

    private static final String TAG = "Download Service";
    private MenuDbHelper mDbHelper;
    private MenuTypesEntity mMenuTypesEntity;
    private List<MenuItemEntity> mMenuItems;
    private MenuApi mMenuApi;
    protected SharedPreferences mSharedPreferences;
    private Integer mMenuVersion;


    public MenuDownloadService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mDbHelper = new MenuDbHelper(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MenuApi.Builder builder = new MenuApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setRootUrl("https://endpointstutorial-1119.appspot.com//_ah/api/");
        mMenuApi = builder.build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(Globals.NOTIFICATION_MENU_DOWNLOAD, makeNotification());

        //SO IT DOWNLOADS EVERY TIME
        mSharedPreferences.edit().putInt(Globals.EXTRA_MENU_VERSION, 0).apply();

        mSharedPreferences.edit().putBoolean(Globals.EXTRA_IS_DOWNLOADING, true).apply();
        new GetMenuVersionAsync().execute(this);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected class GetMenuVersionAsync extends AsyncTask<Context, Void, Integer> {
        Context mContext;
        @Override
        protected Integer doInBackground(Context... params) {
            mContext = params[0];
            try {
                MenuVersionEntity menuVersionEntity = mMenuApi.getMenuVersion().execute();
                mMenuVersion = menuVersionEntity.getVersion();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(mMenuVersion != mSharedPreferences.getInt(Globals.EXTRA_MENU_VERSION, 0)){
                mDbHelper.deleteAllMenuItems();
                mDbHelper.deleteAllMenuTypes();
                new GetMenuTypesAsync().execute(mContext);
            } else {
                stopSelf();
            }
        }
    }

    protected class GetMenuTypesAsync extends AsyncTask<Context, Void, Integer> {
        private Context mContext;

        @Override
        protected Integer doInBackground(Context... params) {
            mContext = params[0];
            try {
                MenuTypesEntity menuTypes = mMenuApi.getMenuTypes().execute();
                mMenuTypesEntity = menuTypes;
                Log.i(TAG, "Got menu types: " + menuTypes.getMenuTypes().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            mDbHelper.insertMenuTypes(mMenuTypesEntity);
            new GetMenuItemsAsync().execute(mContext);
        }
    }

    protected class GetMenuItemsAsync extends AsyncTask<Context, Void, Integer> {
        private Context mContext;

        @Override
        protected Integer doInBackground(Context... params) {
            mContext = params[0];
            try {
                MenuItemEntityCollection menuItems = mMenuApi.getMenuItems().execute();
                mMenuItems = menuItems.getItems();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            mDbHelper.insertMenuItems(mMenuItems);
            mSharedPreferences.edit().putBoolean(Globals.EXTRA_IS_DOWNLOADING, false).apply();
            mSharedPreferences.edit().putInt(Globals.EXTRA_MENU_VERSION, mMenuVersion).apply();
            broadcastDownloadFinished();
            stopSelf();
        }
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "download service being destroyed");
        stopForeground(true);
        mSharedPreferences.edit().putBoolean(Globals.EXTRA_IS_DOWNLOADING, false).apply();
        super.onDestroy();
    }

    private Notification makeNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_shopping_cart_48dp)
                .setContentTitle("Order app")
                .setContentText("Downloading menu");

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);

        return notificationBuilder.build();
    }

    private void broadcastDownloadFinished() {
        Intent broadcastIntent = new Intent(Globals.INTENT_DOWNLOAD_FINISHED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

}
