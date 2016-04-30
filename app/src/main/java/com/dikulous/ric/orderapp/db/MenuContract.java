package com.dikulous.ric.orderapp.db;

import android.provider.BaseColumns;

import com.dikulous.ric.orderapp.model.OrderItem;

/**
 * Created by ric on 31/03/16.
 */
public class MenuContract {
    public MenuContract() {
    }

    public static abstract class MenuEntry implements BaseColumns{
        public static final String TABLE_NAME = "menuItem";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DATASTORE_KEY_STRING = "datastoreKeyString";
        public static final String COLUMN_NAME_SERVING_URL = "servingUrl";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_TYPE_FK = "type_fk";
        public static final String COLUMN_NAME_INGREDIENTS = "ingredients";
        public static final String COLUMN_NAME_ALLERGENS = "allergens";
    }

    public static abstract class MenuTypesEntry implements BaseColumns{
        public static final String TABLE_NAME = "menuTypes";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_PRETTY_NAME = "prettyName";
    }

    public static abstract class OrderEntry implements BaseColumns{
        public static final String TABLE_NAME = "orders";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_IS_CURRENT = "isCurrent";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_PAYMENT_ID = "paymentId";
        public static final String COLUMN_NAME_USER_GENERATED_KEY = "userGeneratedKey";
        public static final String COLUMN_NAME_RECEIVED_SUCCESS = "receivedSuccess";
    }

    public static abstract class OrderItemEntry implements BaseColumns{
        public static final String TABLE_NAME = "orderItems";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_ORDER_FK= "orderFk";
        public static final String COLUMN_NAME_MENU_ITEM_FK= "menuItemFk";
        public static final String COLUMN_NAME_AMOUNT= "amount";
        public static final String COLUMN_NAME_INGREDIENTS_EXCLUDED = "ingredientsExcluded";
        public static final String COLUMN_NAME_SPECIAL_REQUEST = "specialRequest";
        public static final String COLUMN_NAME_MENU_ITEM_KEY_STRING = "menuItemKeyString";
    }

    public static abstract class AddressEntry implements BaseColumns{
        public static final String TABLE_NAME = "addresses";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_ORDER_FK = "orderFk";
        public static final String COLUMN_NAME_UNIT_NUMBER = "unitNumber";
        public static final String COLUMN_NAME_STREET_NUMBER = "streetNumber";
        public static final String COLUMN_NAME_STREET_NAME = "streetName";
        public static final String COLUMN_NAME_SUBURB = "suburb";
        public static final String COLUMN_NAME_POSTCODE = "postcode";
        public static final String COLUMN_NAME_CONTACT_NUMBER = "contactNumber";

    }

    public static final String SQL_CREATE_MENU_TYPES =
            "CREATE TABLE " + MenuTypesEntry.TABLE_NAME +
                    "(" +
                    MenuTypesEntry._ID + " INTEGER PRIMARY KEY," +
                    MenuTypesEntry.COLUMN_NAME_PRETTY_NAME + " TEXT NOT NULL "+
                    ")";

    public static final String SQL_CREATE_MENU =
            "CREATE TABLE " + MenuEntry.TABLE_NAME +
                    "(" +
                    MenuEntry._ID + " INTEGER PRIMARY KEY," +
                    MenuEntry.COLUMN_NAME_NAME + " TEXT NOT NULL,"+
                    MenuEntry.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL,"+
                    MenuEntry.COLUMN_NAME_SERVING_URL + " TEXT NOT NULL,"+
                    MenuEntry.COLUMN_NAME_DATASTORE_KEY_STRING + " TEXT NOT NULL,"+
                    MenuEntry.COLUMN_NAME_INGREDIENTS + " TEXT NOT NULL,"+
                    MenuEntry.COLUMN_NAME_ALLERGENS + " TEXT,"+
                    MenuEntry.COLUMN_NAME_TYPE_FK + " INTEGER NOT NULL,"+
                    MenuEntry.COLUMN_NAME_PRICE + " FLOAT NOT NULL,"+
                    "FOREIGN KEY ("+MenuEntry.COLUMN_NAME_TYPE_FK+") REFERENCES "+ MenuTypesEntry.TABLE_NAME+"("+MenuTypesEntry._ID+")"+
                    ")";

    public static final String SQL_CREATE_ORDERS =
            "CREATE TABLE " + OrderEntry.TABLE_NAME +
                    "(" +
                    OrderEntry._ID + " INTEGER PRIMARY KEY," +
                    OrderEntry.COLUMN_NAME_CREATED_AT + " TIMESTAMP NOT NULL,"+
                    OrderEntry.COLUMN_NAME_IS_CURRENT + " BOOLEAN NOT NULL,"+
                    OrderEntry.COLUMN_NAME_USER_GENERATED_KEY + " TEXT NOT NULL,"+
                    OrderEntry.COLUMN_NAME_PAYMENT_ID + " TEXT,"+
                    OrderEntry.COLUMN_NAME_STATUS + " INTEGER NOT NULL"
                    +")";

    public static final String SQL_CREATE_ORDER_ITEMS =
            "CREATE TABLE " + OrderItemEntry.TABLE_NAME +
                    "(" +
                    OrderItemEntry._ID + " INTEGER PRIMARY KEY," +
                    OrderItemEntry.COLUMN_NAME_MENU_ITEM_FK + " INTEGER NOT NULL,"+
                    OrderItemEntry.COLUMN_NAME_ORDER_FK + " INTEGER NOT NULL,"+
                    OrderItemEntry.COLUMN_NAME_AMOUNT + " INTEGER NOT NULL, "+
                    OrderItemEntry.COLUMN_NAME_INGREDIENTS_EXCLUDED + " TEXT, "+
                    OrderItemEntry.COLUMN_NAME_SPECIAL_REQUEST + " TEXT, "+
                    OrderItemEntry.COLUMN_NAME_MENU_ITEM_KEY_STRING + " TEXT NOT NULL, "+
                    "FOREIGN KEY ("+OrderItemEntry.COLUMN_NAME_MENU_ITEM_FK+") REFERENCES "+ MenuEntry.TABLE_NAME+"("+MenuEntry._ID+"),"+
                    "FOREIGN KEY ("+OrderItemEntry.COLUMN_NAME_ORDER_FK+") REFERENCES "+ OrderEntry.TABLE_NAME+"("+OrderEntry._ID+")"
                    +")";

    public static final String SQL_CREATE_ADDRESSES =
            "CREATE TABLE " + AddressEntry.TABLE_NAME +
                    "(" +
                    AddressEntry._ID + " INTEGER PRIMARY KEY," +
                    AddressEntry.COLUMN_NAME_ORDER_FK + " INTEGER NOT NULL UNIQUE,"+
                    AddressEntry.COLUMN_NAME_UNIT_NUMBER + " TEXT,"+
                    AddressEntry.COLUMN_NAME_STREET_NUMBER + " TEXT NOT NULL,"+
                    AddressEntry.COLUMN_NAME_STREET_NAME + " TEXT NOT NULL,"+
                    AddressEntry.COLUMN_NAME_SUBURB + " TEXT NOT NULL,"+
                    AddressEntry.COLUMN_NAME_POSTCODE + " TEXT NOT NULL,"+
                    AddressEntry.COLUMN_NAME_CONTACT_NUMBER + " TEXT NOT NULL,"+
                    "FOREIGN KEY ("+AddressEntry.COLUMN_NAME_ORDER_FK+") REFERENCES "+ OrderEntry.TABLE_NAME+"("+OrderEntry._ID+")"
                    +")";

    public static final String SQL_DELETE_MENU_TYPES =
            "DROP TABLE IF EXISTS " + MenuTypesEntry.TABLE_NAME;

    public static final String SQL_DELETE_MENU =
            "DROP TABLE IF EXISTS " + MenuEntry.TABLE_NAME;

    public static final String SQL_DELETE_ORDERS =
            "DROP TABLE IF EXISTS " + OrderEntry.TABLE_NAME;

    public static final String SQL_DELETE_ORDER_ITEMS =
            "DROP TABLE IF EXISTS " + OrderItemEntry.TABLE_NAME;

    public static final String SQL_DELETE_ADDRESSES =
            "DROP TABLE IF EXISTS " + AddressEntry.TABLE_NAME;

    public static final String[] MENU_TYPES_PROJECTION = {
            MenuTypesEntry._ID,
            MenuTypesEntry.COLUMN_NAME_PRETTY_NAME,
    };

    public static final String[] MENU_ITEM_PROJECTION = {
            MenuEntry._ID,
            MenuEntry.COLUMN_NAME_NAME,
            MenuEntry.COLUMN_NAME_DESCRIPTION,
            MenuEntry.COLUMN_NAME_DATASTORE_KEY_STRING,
            MenuEntry.COLUMN_NAME_INGREDIENTS,
            MenuEntry.COLUMN_NAME_ALLERGENS,
            MenuEntry.COLUMN_NAME_PRICE,
            MenuEntry.COLUMN_NAME_SERVING_URL,
            MenuEntry.COLUMN_NAME_TYPE_FK,
    };

    public static final String[] ORDER_PROJECTION = {
            OrderEntry._ID,
            OrderEntry.COLUMN_NAME_CREATED_AT,
            OrderEntry.COLUMN_NAME_IS_CURRENT,
            OrderEntry.COLUMN_NAME_PAYMENT_ID,
            OrderEntry.COLUMN_NAME_STATUS,
            OrderEntry.COLUMN_NAME_USER_GENERATED_KEY
    };

    public static final String[] ORDER_ITEM_PROJECTION = {
            OrderItemEntry._ID,
            OrderItemEntry.COLUMN_NAME_MENU_ITEM_FK,
            OrderItemEntry.COLUMN_NAME_ORDER_FK,
            OrderItemEntry.COLUMN_NAME_AMOUNT,
            OrderItemEntry.COLUMN_NAME_INGREDIENTS_EXCLUDED,
            OrderItemEntry.COLUMN_NAME_SPECIAL_REQUEST,
            OrderItemEntry.COLUMN_NAME_MENU_ITEM_KEY_STRING,
    };

    public static final String[] ADDRESS_PROJECTION = {
            AddressEntry._ID,
            AddressEntry.COLUMN_NAME_ORDER_FK,
            AddressEntry.COLUMN_NAME_UNIT_NUMBER,
            AddressEntry.COLUMN_NAME_STREET_NUMBER,
            AddressEntry.COLUMN_NAME_STREET_NAME,
            AddressEntry.COLUMN_NAME_SUBURB,
            AddressEntry.COLUMN_NAME_POSTCODE,
            AddressEntry.COLUMN_NAME_CONTACT_NUMBER
    };
}
