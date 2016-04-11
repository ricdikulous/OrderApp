package com.dikulous.ric.orderapp.menu.item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;


import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderItemEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ric on 7/04/16.
 */
public class MenuItemDetailsAdapter extends RecyclerView.Adapter {

    private static final String TAG = "Menu Gallery Adapter";
    Context mContext;
    private MenuItemEntity mMenuItem;
    private MenuDbHelper mDbHelper;
    private HashSet<String> mIngredientsExcluded;
    private ArrayList<String> mIngredientsExcludedList;

    public MenuItemDetailsAdapter(Context context, MenuItemEntity menuItem) {
        mContext = context;
        mMenuItem = menuItem;
        mDbHelper = new MenuDbHelper(context);
        mIngredientsExcluded = new HashSet<>();
        mIngredientsExcludedList = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mIngredient;
        public CheckBox mIncludeIngredient;


        public ViewHolder(View itemView) {
            super(itemView);
            mIngredient = (TextView) itemView.findViewById(R.id.ingredient);
            mIncludeIngredient = (CheckBox) itemView.findViewById(R.id.include_ingredient);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_ingredient, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final String ingredient = mMenuItem.getIngredients().get(position);
        viewHolder.mIngredient.setText(ingredient);
        viewHolder.mIncludeIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox view = (CheckBox) v;
                if (((CheckBox) v).isChecked()) {
                    mIngredientsExcluded.remove(ingredient);
                    mIngredientsExcludedList.remove(ingredient);
                } else {
                    mIngredientsExcluded.add(ingredient);
                    mIngredientsExcludedList.add(ingredient);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMenuItem.getIngredients().size();
    }

    public ArrayList<String> getIngredientsExcluded(){
        return mIngredientsExcludedList;
    }
}
