package com.dikulous.ric.orderapp.menu.gallery;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.menu.MenuAbstractActivity;
import com.example.ric.myapplication.backend.api.menuApi.MenuApi;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntityCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuGalleryActivity extends MenuAbstractActivity {

    private static final String TAG = "Menu Gallery Activity";
    private MenuGalleryAdapter mAdapter;
    private List<MenuItemEntity> mMenuItems;
    private MenuDbHelper mDbHelper;

    private RecyclerView mRecyclerView;
    private ProgressBar mMenuSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_gallery);
        mMenuItems = new ArrayList<>();
        mDbHelper = new MenuDbHelper(this);

        mAdapter = new MenuGalleryAdapter(this, mMenuItems);
        //mPhotoDbHelper = new PhotoDbHelper(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.menu_list);
        mMenuSpinner = (ProgressBar) findViewById(R.id.menu_spinner);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);


        new GetMenuItemsAsync().execute(this);
    }

    protected class GetMenuItemsAsync extends AsyncTask<Context, Void, Integer>{
        private Context mContext;

        @Override
        protected Integer doInBackground(Context... params) {
            mContext = params[0];
            MenuApi.Builder builder = new MenuApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://endpointstutorial-1119.appspot.com//_ah/api/");
            MenuApi menuApi = builder.build();
            try {
                MenuItemEntityCollection menuItems = menuApi.getMenuItems().execute();
                Log.i(TAG, "Got menu Items: "+menuItems.getItems().size());
                mMenuItems = menuItems.getItems();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            //Toast.makeText(mContext, result + " End API call", Toast.LENGTH_LONG).show();
            for(MenuItemEntity menuItem:mMenuItems){
                mDbHelper.insertMenuItem(menuItem);
            }
            mAdapter.updateItems(mMenuItems);
            mMenuSpinner.setVisibility(View.GONE);
            //new JourneyLocationAsyncTask().execute(new Pair<Context, Long>(mContext, journeyPk));
        }
    }
}
