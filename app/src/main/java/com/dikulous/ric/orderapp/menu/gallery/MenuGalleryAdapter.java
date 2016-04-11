package com.dikulous.ric.orderapp.menu.gallery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.menu.item.MenuItemActivity;
import com.dikulous.ric.orderapp.util.Globals;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;

import java.util.List;

/**
 * Created by ric on 30/03/16.
 */
public class MenuGalleryAdapter extends  RecyclerView.Adapter  {

    private static final String TAG = "Menu Gallery Adapter";
    Context mContext;
    private List<MenuItemEntity> mMenuItems;
    private MenuDbHelper mDbHelper;

    public MenuGalleryAdapter(Context context, List<MenuItemEntity> menuItems) {
        mContext = context;
        mMenuItems = menuItems;
        mDbHelper = new MenuDbHelper(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mFoodImage;
        public TextView mFoodName;

        public IMyViewHolderClicks mListener;


        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            mListener = listener;
            mFoodImage = (ImageView) itemView.findViewById(R.id.food_image);
            mFoodName = (TextView) itemView.findViewById(R.id.food_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onMenuItemClick(v, getAdapterPosition());
        }

        public static interface IMyViewHolderClicks {
            public void onMenuItemClick(View caller, int position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_item_menu, parent, false);
        ViewHolder vh = new ViewHolder(rowView, new ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onMenuItemClick(View caller, int position) {
                long pk = mDbHelper.readMenuItemPkByKeyString(mMenuItems.get(position).getKeyString());
                Intent intent = new Intent(mContext, MenuItemActivity.class);
                intent.putExtra(Globals.EXTRA_MENU_PK, pk);
                mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        MenuItemEntity menuItem = mMenuItems.get(position);
        viewHolder.mFoodName.setText(menuItem.getName());
        Glide.with(mContext).load(menuItem.getServingUrl()).crossFade().into(viewHolder.mFoodImage);
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    public void updateItems(List<MenuItemEntity> menuItems){
        mMenuItems = menuItems;
        notifyDataSetChanged();
    }
}
