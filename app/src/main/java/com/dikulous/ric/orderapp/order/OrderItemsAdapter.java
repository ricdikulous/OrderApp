package com.dikulous.ric.orderapp.order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.menu.item.MenuItemActivity;
import com.dikulous.ric.orderapp.model.OrderItem;
import com.dikulous.ric.orderapp.util.Globals;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;

import java.util.List;

/**
 * Created by ric on 31/03/16.
 */
public class OrderItemsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<OrderItem> mOrderItems;
    private MenuDbHelper menuDbHelper;

    public OrderItemsAdapter(Context context, List<OrderItem> orderItems) {
        this.mContext = context;
        this.mOrderItems = orderItems;
        menuDbHelper = new MenuDbHelper(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mFoodName;
        public TextView mAmount;
        public TextView mSubTotal;


        public IMyViewHolderClicks mListener;


        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            mListener = listener;
            mFoodName = (TextView) itemView.findViewById(R.id.food_name);
            mAmount = (TextView) itemView.findViewById(R.id.amount);
            mSubTotal = (TextView) itemView.findViewById(R.id.sub_total);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onOrderItemClick(v, getAdapterPosition());
        }

        public static interface IMyViewHolderClicks {
            public void onOrderItemClick(View caller, int position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_item_order, parent, false);
        ViewHolder vh = new ViewHolder(rowView, new ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onOrderItemClick(View caller, int position) {
                //long pk = mDbHelper.readMenuItemPkByKeyString(mMenuItems.get(position).getKeyString());
                //Intent intent = new Intent(mContext, MenuItemActivity.class);
                //intent.putExtra(Globals.EXTRA_MENU_PK, pk);
                //mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        OrderItem orderItem = mOrderItems.get(position);
        MenuItemEntity menuItem = menuDbHelper.readMenuItemByPk(orderItem.getMenuItemFk());
        viewHolder.mFoodName.setText(menuItem.getName());
        viewHolder.mAmount.setText("Amount: "+String.valueOf(orderItem.getAmount()));
        long subTotal = orderItem.getAmount()*menuItem.getPrice();
        viewHolder.mSubTotal.setText("$"+subTotal);
    }

    @Override
    public int getItemCount() {
        return mOrderItems.size();
    }
}
