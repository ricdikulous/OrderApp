package com.dikulous.ric.orderapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dikulous.ric.orderapp.model.OrderItem;
import com.dikulous.ric.orderapp.util.Globals;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderEntity;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderItemEntity;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderReceiptEntity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ric on 31/03/16.
 */
public class OrderDbHelper extends MenuDbHelper {

    public OrderDbHelper(Context context) {
        super(context);
    }

    public long insertNewOrder(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MenuContract.OrderEntry.COLUMN_NAME_CREATED_AT, new Date().getTime());
        values.put(MenuContract.OrderEntry.COLUMN_NAME_IS_CURRENT, true);
        values.put(MenuContract.OrderEntry.COLUMN_NAME_STATUS, Globals.ORDER_CREATED);

        long newRowId = db.insert(
                MenuContract.OrderEntry.TABLE_NAME, null, values
        );
        return newRowId;
    }

    public long insertOrderItem(OrderItem orderItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Gson gson = new Gson();

        values.put(MenuContract.OrderItemEntry.COLUMN_NAME_ORDER_FK, orderItem.getOrderFk());
        values.put(MenuContract.OrderItemEntry.COLUMN_NAME_MENU_ITEM_FK, orderItem.getMenuItemFk());
        values.put(MenuContract.OrderItemEntry.COLUMN_NAME_AMOUNT, orderItem.getAmount());
        if(orderItem.getIngredientsExcluded() != null && orderItem.getIngredientsExcluded().size()>0) {
            values.put(MenuContract.OrderItemEntry.COLUMN_NAME_INGREDIENTS_EXCLUDED, gson.toJson(orderItem.getIngredientsExcluded()));
        }
        values.put(MenuContract.OrderItemEntry.COLUMN_NAME_MENU_ITEM_KEY_STRING, orderItem.getMenuItemKeyString());


        long newRowId = db.insert(
                MenuContract.OrderItemEntry.TABLE_NAME, null, values
        );
        Log.i(TAG, "inserted order Item: "+newRowId);
        return newRowId;
    }

    public int updateWithPaymentId(String paymentId, long orderIdPk){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MenuContract.OrderEntry.COLUMN_NAME_PAYMENT_ID, paymentId);
        String selection = MenuContract.OrderEntry._ID +" = ? ";
        String[] selectionArgs = {String.valueOf(orderIdPk)};

        int count = db.update(
                MenuContract.OrderEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        Log.i(TAG, "Update paymentId: "+count);
        return count;
    }

    public int updateOrderReceived(long orderPk, OrderReceiptEntity orderReceipt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int statusValue;
        if(orderReceipt.getSuccess()) {
            statusValue = Globals.ORDER_RECEIVED;
        } else {
            statusValue = Globals.ORDER_REJECTED_BY_SERVER;
        }

        Log.i(TAG, "order received staty: "+statusValue);

        values.put(MenuContract.OrderEntry.COLUMN_NAME_STATUS, statusValue);

        String selection = MenuContract.OrderEntry._ID +" = ? ";
        String[] selectionArgs = {String.valueOf(orderPk)};

        int count = db.update(
                MenuContract.OrderEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        Log.i(TAG, "Update order receieved: "+count);
        return count;
    }

    public int updateOrderStatus(int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long orderPk = readCurrentOrderPk();
        values.put(MenuContract.OrderEntry.COLUMN_NAME_STATUS, status);

        String selection = MenuContract.OrderEntry._ID +" = ? ";
        String[] selectionArgs = {String.valueOf(orderPk)};

        int count = db.update(
                MenuContract.OrderEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        Log.i(TAG, "Update order to status: "+status+" of "+count+" orders");
        return count;
    }

    public Long readCurrentOrderPk() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                MenuContract.OrderEntry._ID,
        };

        String selection = MenuContract.OrderEntry.COLUMN_NAME_IS_CURRENT+"= 1";

        Cursor cursor = db.query(
                MenuContract.OrderEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                return cursor.getLong(cursor.getColumnIndex(MenuContract.OrderEntry._ID));
            }
        }
        return 0L;
    }

    public List<OrderItem> readCurrentOrderItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        long orderFk = this.readCurrentOrderPk();
        String selection = MenuContract.OrderItemEntry.COLUMN_NAME_ORDER_FK+"= ?";
        String[] selectionArgs = {String.valueOf(orderFk)};
        Cursor cursor = db.query(
                MenuContract.OrderItemEntry.TABLE_NAME,
                MenuContract.ORDER_ITEM_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);

        List<OrderItem> orderItems = new ArrayList<>();
        if(cursor != null){
            while (cursor.moveToNext()){
                orderItems.add(this.cursorToOrderItem(cursor));
            }
        }
        return orderItems;
    }

    public List<OrderItemEntity> readCurrentOrderItemEntities() {
        SQLiteDatabase db = this.getReadableDatabase();

        long orderFk = this.readCurrentOrderPk();
        String selection = MenuContract.OrderItemEntry.COLUMN_NAME_ORDER_FK+"= ?";
        String[] selectionArgs = {String.valueOf(orderFk)};
        Cursor cursor = db.query(
                MenuContract.OrderItemEntry.TABLE_NAME,
                MenuContract.ORDER_ITEM_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);

        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        if(cursor != null){
            while (cursor.moveToNext()){
                orderItemEntities.add(this.cursorToOrderItemEntity(cursor));
            }
        }
        return orderItemEntities;
    }

    public OrderEntity readOrderEntity(){

        SQLiteDatabase db = this.getReadableDatabase();

        long orderFk = this.readCurrentOrderPk();
        String selection = MenuContract.OrderItemEntry._ID+"= ?";
        String[] selectionArgs = {String.valueOf(orderFk)};
        Cursor cursor = db.query(
                MenuContract.OrderEntry.TABLE_NAME,
                MenuContract.ORDER_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);

        OrderEntity orderEntity = new OrderEntity();

        if(cursor != null){
            if(cursor.moveToFirst()){
                orderEntity.setPaymentId(cursor.getString(cursor.getColumnIndex(MenuContract.OrderEntry.COLUMN_NAME_PAYMENT_ID)));
            }
        }
        orderEntity.setAddress("123 fake street");
        orderEntity.setPhoneNumber("04 1234 1234");
        orderEntity.setRegistrationToken("4rt89ednf9839rwd");
        orderEntity.setOrderItemEntities(readCurrentOrderItemEntities());

        return orderEntity;
    }

    public int readOrderStatus(long orderPk) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = MenuContract.OrderItemEntry._ID+"= ?";
        String[] selectionArgs = {String.valueOf(orderPk)};
        Cursor cursor = db.query(
                MenuContract.OrderEntry.TABLE_NAME,
                MenuContract.ORDER_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int status = 0;
        if(cursor != null){
            if(cursor.moveToFirst()){
                status = cursor.getInt(cursor.getColumnIndex(MenuContract.OrderEntry.COLUMN_NAME_STATUS));
            }
        }
        return status;
    }

    public long deleteCurrentOrder(){
        SQLiteDatabase db = this.getWritableDatabase();
        deleteOrderItems(this.readCurrentOrderPk());
        String selection = MenuContract.OrderEntry.COLUMN_NAME_IS_CURRENT +" = 1";
        Log.i(TAG, "deleting all current orders");
        return db.delete(MenuContract.OrderEntry.TABLE_NAME, selection, null);
    }

    public long deleteOrderItems(long orderFk){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = MenuContract.OrderItemEntry.COLUMN_NAME_ORDER_FK +" = ?";
        String[] selectionArgs = {String.valueOf(orderFk)};
        long numDeleted = db.delete(MenuContract.OrderItemEntry.TABLE_NAME, selection, selectionArgs);
        Log.i(TAG, "deleting items: " + numDeleted + " of Order: " + orderFk);
        return numDeleted;
    }

    private OrderItem cursorToOrderItem(Cursor cursor) {
        OrderItem orderItem = new OrderItem();
        Gson gson = new Gson();
        orderItem.setOrderFk(cursor.getLong(cursor.getColumnIndex(MenuContract.OrderItemEntry.COLUMN_NAME_ORDER_FK)));
        orderItem.setMenuItemFk(cursor.getLong(cursor.getColumnIndex(MenuContract.OrderItemEntry.COLUMN_NAME_MENU_ITEM_FK)));
        orderItem.setAmount(cursor.getInt(cursor.getColumnIndex(MenuContract.OrderItemEntry.COLUMN_NAME_AMOUNT)));
        if(cursor.getString(cursor.getColumnIndex(MenuContract.OrderItemEntry.COLUMN_NAME_INGREDIENTS_EXCLUDED)) != null){
            orderItem.setIngredientsExcluded(gson.fromJson(cursor.getString(cursor.getColumnIndex(MenuContract.OrderItemEntry.COLUMN_NAME_INGREDIENTS_EXCLUDED)), ArrayList.class));
        }
        orderItem.setMenuItemKeyString(cursor.getString(cursor.getColumnIndex(MenuContract.OrderItemEntry.COLUMN_NAME_MENU_ITEM_KEY_STRING)));
        return orderItem;
    }

    private OrderItemEntity cursorToOrderItemEntity(Cursor cursor){
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        Gson gson = new Gson();
        orderItemEntity.setAmount(cursor.getInt(cursor.getColumnIndex(MenuContract.OrderItemEntry.COLUMN_NAME_AMOUNT)));
        if(cursor.getString(cursor.getColumnIndex(MenuContract.OrderItemEntry.COLUMN_NAME_INGREDIENTS_EXCLUDED)) != null){
            orderItemEntity.setIngredientsExcluded(gson.fromJson(cursor.getString(cursor.getColumnIndex(MenuContract.OrderItemEntry.COLUMN_NAME_INGREDIENTS_EXCLUDED)), ArrayList.class));

        }
        orderItemEntity.setMenuItemKeyString(cursor.getString(cursor.getColumnIndex(MenuContract.OrderItemEntry.COLUMN_NAME_MENU_ITEM_KEY_STRING)));
        return orderItemEntity;
    }
}