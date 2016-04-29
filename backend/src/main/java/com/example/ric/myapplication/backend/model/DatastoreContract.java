package com.example.ric.myapplication.backend.model;

/**
 * Created by ric on 9/04/16.
 */
public class DatastoreContract {

    public static final class MenuItemsEntry{
        public static final String KIND = "MenuItem";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_SERVING_URL = "servingUrl";
        public static final String COLUMN_NAME_INGREDIENTS = "ingredients";
        public static final String COLUMN_NAME_ALLERGENS = "allergens";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
    }

    public static final class MenuTypesEntry{
        public static final String KIND = "MenuTypes";
        public static final String COLUMN_NAME_TYPES = "types";
    }

    public static final class MenuVersionEntry{
        public static final String KIND = "MenuVersion";
        public static final String KEY_DOWNLOAD = "download";
        public static final String COLUMN_NAME_VERSION = "version";
    }

    public static final class OrdersEntry{
        public static final String KIND = "Orders";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_ORDER_ITEMS = "orderItems";
        public static final String COLUMN_NAME_PAYMENT_ID = "paymentId";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phoneNumber";
        public static final String COLUMN_NAME_REGISTRATION_TOKEN = "registrationToken";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_STATUS = "status";
    }

    public static final class OrderItemEmbeddedEntry{
        public static final String COLUMN_NAME_MENU_ITEM_KEY = "itemKey";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_INGREDIENTS_EXCLUDED = "ingredientsExcluded";
        public static final String COLUMN_NAME_SPECIAL_REQUEST = "specialRequest";
        public static final String COLUMN_NAME_MENU_NAME = "name";
    }

    public static final class TypesEmbeddedEntry{
        public static final String COLUMN_NAME_KEY = "key";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static final class ChannelKeysEntry{
        public static final String KIND = "ChannelKeys";
        public static final String KEY = "keys";
        public static final String COLUMN_NAME_KEYS = "keys";
    }
}
