package com.dikulous.ric.orderapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.dikulous.ric.orderapp.util.Globals;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuTypesEntity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ric on 31/03/16.
 */
public class MenuDbHelper extends SQLiteOpenHelper{

    public static final String TAG = "MenuDbHelper";

    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = "Menu.db";

    public Context mContext;

    public MenuDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MenuContract.SQL_CREATE_MENU_TYPES);
        db.execSQL(MenuContract.SQL_CREATE_MENU);
        db.execSQL(MenuContract.SQL_CREATE_ORDERS);
        db.execSQL(MenuContract.SQL_CREATE_ORDER_ITEMS);
        db.execSQL(MenuContract.SQL_CREATE_ADDRESSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "did upgrade");
        db.execSQL(MenuContract.SQL_DELETE_ADDRESSES);
        db.execSQL(MenuContract.SQL_DELETE_ORDER_ITEMS);
        db.execSQL(MenuContract.SQL_DELETE_ORDERS);
        db.execSQL(MenuContract.SQL_DELETE_MENU);
        db.execSQL(MenuContract.SQL_DELETE_MENU_TYPES);
        onCreate(db);
    }

    public void insertMenuTypes(MenuTypesEntity menuTypes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.beginTransaction();
        for(String id:menuTypes.getMenuTypes().keySet()){
            values.put(MenuContract.MenuTypesEntry._ID, Long.valueOf(id));
            values.put(MenuContract.MenuTypesEntry.COLUMN_NAME_PRETTY_NAME, (String) menuTypes.getMenuTypes().get(id));
            db.insert(MenuContract.MenuTypesEntry.TABLE_NAME, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public long insertMenuItem(MenuItemEntity menuItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Gson gson = new Gson();

        values.put(MenuContract.MenuEntry.COLUMN_NAME_NAME, menuItem.getName());
        values.put(MenuContract.MenuEntry.COLUMN_NAME_DESCRIPTION, menuItem.getDescription());
        values.put(MenuContract.MenuEntry.COLUMN_NAME_SERVING_URL, menuItem.getServingUrl());
        values.put(MenuContract.MenuEntry.COLUMN_NAME_DATASTORE_KEY_STRING, menuItem.getKeyString());
        values.put(MenuContract.MenuEntry.COLUMN_NAME_INGREDIENTS, gson.toJson(menuItem.getIngredients()));
        if(menuItem.getAllergens() != null && menuItem.getAllergens().size() > 0){
            values.put(MenuContract.MenuEntry.COLUMN_NAME_ALLERGENS, gson.toJson(menuItem.getAllergens()));
        }
        values.put(MenuContract.MenuEntry.COLUMN_NAME_PRICE, menuItem.getPrice());
        values.put(MenuContract.MenuEntry.COLUMN_NAME_TYPE_FK, menuItem.getType());

        long newRowId = db.insert(
                MenuContract.MenuEntry.TABLE_NAME, null, values
        );
        return newRowId;
    }

    public void insertMenuItems(List<MenuItemEntity> menuItems){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Gson gson = new Gson();
        db.beginTransaction();
        for(MenuItemEntity menuItem:menuItems) {
            values.put(MenuContract.MenuEntry.COLUMN_NAME_NAME, menuItem.getName());
            values.put(MenuContract.MenuEntry.COLUMN_NAME_DESCRIPTION, menuItem.getDescription());
            values.put(MenuContract.MenuEntry.COLUMN_NAME_SERVING_URL, menuItem.getServingUrl());
            values.put(MenuContract.MenuEntry.COLUMN_NAME_DATASTORE_KEY_STRING, menuItem.getKeyString());
            values.put(MenuContract.MenuEntry.COLUMN_NAME_INGREDIENTS, gson.toJson(menuItem.getIngredients()));
            if(menuItem.getAllergens() != null){
                values.put(MenuContract.MenuEntry.COLUMN_NAME_ALLERGENS, gson.toJson(menuItem.getAllergens()));
            }
            values.put(MenuContract.MenuEntry.COLUMN_NAME_PRICE, menuItem.getPrice());
            values.put(MenuContract.MenuEntry.COLUMN_NAME_TYPE_FK, menuItem.getType());

            db.insert(
                    MenuContract.MenuEntry.TABLE_NAME, null, values
            );
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public HashMap<Long, String> readMenuTypes(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                MenuContract.MenuTypesEntry.TABLE_NAME,
                MenuContract.MENU_TYPES_PROJECTION,
                null,
                null,
                null,
                null,
                null
        );
        HashMap<Long, String> typesMap = new HashMap<>();
        if(cursor != null){
            while (cursor.moveToNext()){
                Long id = cursor.getLong(cursor.getColumnIndex(MenuContract.MenuTypesEntry._ID));
                String name = cursor.getString(cursor.getColumnIndex(MenuContract.MenuTypesEntry.COLUMN_NAME_PRETTY_NAME));
                typesMap.put(id, name);
            }
        }
        return typesMap;
    }

    public MenuItemEntity readMenuItemByPk(long pk) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = MenuContract.MenuEntry._ID+"= ?";
        String[] selectionArgs = {String.valueOf(pk)};

        Cursor cursor = db.query(
                MenuContract.MenuEntry.TABLE_NAME,
                MenuContract.MENU_ITEM_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                return cursorToMenuEntity(cursor);
            }
        }
        return null;
    }

    public List<MenuItemEntity> readMenuItemsByType(long type) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = MenuContract.MenuEntry.COLUMN_NAME_TYPE_FK+"= ?";
        String[] selectionArgs = {String.valueOf(type)};

        Cursor cursor = db.query(
                MenuContract.MenuEntry.TABLE_NAME,
                MenuContract.MENU_ITEM_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);

        List<MenuItemEntity> menuItems = new ArrayList<>();
        if(cursor != null){
            while(cursor.moveToNext()){
                menuItems.add(cursorToMenuEntity(cursor));
            }
        }
        return menuItems;
    }

    public long readMenuItemPkByKeyString(String keyString) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                MenuContract.MenuEntry._ID,
        };

        String selection = MenuContract.MenuEntry.COLUMN_NAME_DATASTORE_KEY_STRING+"= ?";
        String[] selectionArgs = {keyString};

        Cursor cursor = db.query(
                MenuContract.MenuEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                return cursor.getLong(cursor.getColumnIndex(MenuContract.MenuEntry._ID));
            }
        }
        return 0;
    }

    public int deleteAllMenuTypes() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i(TAG, "deleting all Menu Types");
        return db.delete(MenuContract.MenuTypesEntry.TABLE_NAME, null, null);
    }

    public int deleteAllMenuItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i(TAG, "deleting all Menu Items");
        return db.delete(MenuContract.MenuEntry.TABLE_NAME, null, null);
    }

    private static MenuItemEntity cursorToMenuEntity(Cursor cursor){
        Gson gson = new Gson();
        MenuItemEntity menuItem = new MenuItemEntity();
        menuItem.setName(cursor.getString(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_NAME_NAME)));
        menuItem.setDescription(cursor.getString(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_NAME_DESCRIPTION)));
        menuItem.setKeyString(cursor.getString(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_NAME_DATASTORE_KEY_STRING)));
        menuItem.setIngredients(gson.fromJson(cursor.getString(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_NAME_INGREDIENTS)), ArrayList.class));
        if(cursor.getString(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_NAME_ALLERGENS)) != null) {
            menuItem.setAllergens(gson.fromJson(cursor.getString(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_NAME_ALLERGENS)), ArrayList.class));
        }
        menuItem.setPrice(cursor.getLong(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_NAME_PRICE)));
        menuItem.setServingUrl(cursor.getString(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_NAME_SERVING_URL)));
        menuItem.setType(cursor.getLong(cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_NAME_TYPE_FK)));
        return menuItem;
    }
}
