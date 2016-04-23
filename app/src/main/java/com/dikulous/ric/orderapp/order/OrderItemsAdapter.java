package com.dikulous.ric.orderapp.order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.menu.item.MenuItemActivity;
import com.dikulous.ric.orderapp.model.OrderItem;
import com.dikulous.ric.orderapp.util.CurrencyUtil;
import com.dikulous.ric.orderapp.util.Globals;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ric on 31/03/16.
 */
public class OrderItemsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ADAPTER Order items";
    private Context mContext;
    private List<OrderItem> mOrderItems;
    private MenuDbHelper menuDbHelper;
    private OrderDbHelper mOrderDbHelper;
    private TextView mTotalPriceTextView;

    public OrderItemsAdapter(Context context, List<OrderItem> orderItems, TextView totalPriceTextView) {
        this.mContext = context;
        this.mOrderItems = orderItems;
        menuDbHelper = new MenuDbHelper(context);
        mOrderDbHelper = new OrderDbHelper(context);
        mTotalPriceTextView = totalPriceTextView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mFoodName;
        public TextView mSubTotal;
        public TextView mAmountTicker;
        public ImageView mAddAmount;
        public ImageView mRemoveAmount;

        public IMyViewHolderClicks mListener;


        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            mListener = listener;
            mFoodName = (TextView) itemView.findViewById(R.id.food_name);
            mSubTotal = (TextView) itemView.findViewById(R.id.sub_total);
            mAmountTicker = (TextView) itemView.findViewById(R.id.amount_ticker);
            mAddAmount = (ImageView) itemView.findViewById(R.id.add_amount);
            mRemoveAmount = (ImageView) itemView.findViewById(R.id.remove_amount);
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
            public void onOrderItemClick(View view, int position) {
                TextView ingredientsExcluded = (TextView)view.findViewById(R.id.ingredients_excluded);
                if(ingredientsExcluded.getMaxLines() == Integer.MAX_VALUE) {
                    ingredientsExcluded.setMaxLines(1);
                    ingredientsExcluded.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    ingredientsExcluded.setMaxLines(Integer.MAX_VALUE);
                    ingredientsExcluded.setEllipsize(null);
                }

                //long pk = mDbHelper.readMenuItemPkByKeyString(mMenuItems.get(position).getKeyString());
                //Intent intent = new Intent(mContext, MenuItemActivity.class);
                //intent.putExtra(Globals.EXTRA_MENU_PK, pk);
                //mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final OrderItem orderItem = mOrderItems.get(position);
        final MenuItemEntity menuItem = menuDbHelper.readMenuItemByPk(orderItem.getMenuItemFk());
        viewHolder.mFoodName.setText(menuItem.getName());
        viewHolder.mAmountTicker.setText("x"+String.valueOf(orderItem.getAmount()));
        long subTotal = orderItem.getAmount()*menuItem.getPrice();
        viewHolder.mSubTotal.setText("$"+ CurrencyUtil.longCentsToBigDecimal(subTotal));
        viewHolder.mAddAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAmount(viewHolder, orderItem, position, menuItem,  1);
            }
        });
        viewHolder.mRemoveAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAmount(viewHolder, orderItem, position, menuItem, -1);
            }
        });
    }

    private void updateAmount(ViewHolder viewHolder, OrderItem orderItem, int position, MenuItemEntity menuItem, int delta) {
        int amount = orderItem.getAmount()+delta;
        if(amount >= 0) {
            mOrderDbHelper.updateAmount(orderItem, amount);
            orderItem.setAmount(amount);
            mOrderItems.get(position).setAmount(amount);
            long subTotal = orderItem.getAmount() * menuItem.getPrice();
            viewHolder.mSubTotal.setText("$" + CurrencyUtil.longCentsToBigDecimal(subTotal));
            viewHolder.mAmountTicker.setText("x" + String.valueOf(amount));

            long total = 0;
            for(OrderItem item:mOrderItems){
                long price = menuDbHelper.readMenuItemByPk(item.getMenuItemFk()).getPrice();
                total += item.getAmount()*price;
            }
            BigDecimal totalPrice = CurrencyUtil.longCentsToBigDecimal(total);
            mTotalPriceTextView.setText("Total: $" + totalPrice);

        }
    }

    @Override
    public int getItemCount() {
        return mOrderItems.size();
    }
}
