package com.dikulous.ric.orderapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dikulous.ric.orderapp.model.Address;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderEntity;

/**
 * Created by ric on 24/04/16.
 */
public class AddressDbHelper extends OrderDbHelper {
    public AddressDbHelper(Context context) {
        super(context);
    }

    public long insertNewAddress(Address address){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MenuContract.AddressEntry.COLUMN_NAME_ORDER_FK, address.getOrderFk());
        if(address.getUnitNumber() != null){
            values.put(MenuContract.AddressEntry.COLUMN_NAME_UNIT_NUMBER, address.getUnitNumber());
        }
        values.put(MenuContract.AddressEntry.COLUMN_NAME_STREET_NUMBER, address.getStreetNumber());
        values.put(MenuContract.AddressEntry.COLUMN_NAME_STREET_NAME, address.getSteetName());
        values.put(MenuContract.AddressEntry.COLUMN_NAME_SUBURB, address.getSuburb());
        values.put(MenuContract.AddressEntry.COLUMN_NAME_POSTCODE, address.getPostcode());
        values.put(MenuContract.AddressEntry.COLUMN_NAME_CONTACT_NUMBER, address.getContactNumber());


        long newRowId = db.insert(
                MenuContract.AddressEntry.TABLE_NAME, null, values
        );
        return newRowId;
    }

    public Address readAddress(long orderFk){
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = MenuContract.AddressEntry.COLUMN_NAME_ORDER_FK+"= ?";
        String[] selectionArgs = {String.valueOf(orderFk)};
        Cursor cursor = db.query(
                MenuContract.AddressEntry.TABLE_NAME,
                MenuContract.ADDRESS_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);
        Address address = null;
        if(cursor != null){
            if (cursor.moveToFirst()){
                address = cursorToAddress(cursor);
            }
        }
        return address;
    }

    public OrderEntity readFullOrderEntity(long orderPk) {
        OrderEntity orderEntity = readOrderEntity(orderPk);
        Address address = readAddress(orderPk);
        String addressString = "";
        if(address.getUnitNumber() != null){
            addressString += address.getUnitNumber()+"/";
        }
        addressString += address.getStreetNumber()+" "+address.getSteetName()+", "+address.getSuburb()+" "+address.getPostcode();
        orderEntity.setAddress(addressString);
        orderEntity.setPhoneNumber(address.getContactNumber());
        return orderEntity;
    }

    public int deleteAllAddresses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i(TAG, "deleting all Addresses");
        return db.delete(MenuContract.AddressEntry.TABLE_NAME, null, null);
    }

    private Address cursorToAddress(Cursor cursor) {
        Address address;
        long orderFk = cursor.getLong(cursor.getColumnIndex(MenuContract.AddressEntry.COLUMN_NAME_ORDER_FK));
        String unitNumber = null;
        if(cursor.getString(cursor.getColumnIndex(MenuContract.AddressEntry.COLUMN_NAME_UNIT_NUMBER)) != null){
            unitNumber =  cursor.getString(cursor.getColumnIndex(MenuContract.AddressEntry.COLUMN_NAME_UNIT_NUMBER));
        }
        String streetNumber =  cursor.getString(cursor.getColumnIndex(MenuContract.AddressEntry.COLUMN_NAME_STREET_NUMBER));
        String streetName =  cursor.getString(cursor.getColumnIndex(MenuContract.AddressEntry.COLUMN_NAME_STREET_NAME));
        String suburb =  cursor.getString(cursor.getColumnIndex(MenuContract.AddressEntry.COLUMN_NAME_SUBURB));
        String postcode =  cursor.getString(cursor.getColumnIndex(MenuContract.AddressEntry.COLUMN_NAME_POSTCODE));
        String contactNumber =  cursor.getString(cursor.getColumnIndex(MenuContract.AddressEntry.COLUMN_NAME_CONTACT_NUMBER));
        if(unitNumber == null){
            address = new Address(orderFk, streetNumber, streetName, suburb, postcode, contactNumber);
        } else {
            address = new Address(orderFk, unitNumber, streetNumber, streetName, suburb, postcode, contactNumber);
        }
        return address;
    }
}
