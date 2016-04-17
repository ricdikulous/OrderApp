package com.example.ric.myapplication.backend.util;

import com.example.ric.myapplication.backend.model.MenuItemEntity;
import com.example.ric.myapplication.backend.model.DatastoreContract;
import com.example.ric.myapplication.backend.model.OrderEntity;
import com.example.ric.myapplication.backend.model.OrderItemEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ric on 4/04/16.
 */
public class DatastoreUtil {

    public static void saveChannelKey(String channelKey) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity entity;
        List<String> keys;
        try {
            entity = datastore.get(KeyFactory.createKey(DatastoreContract.ChannelKeysEntry.KIND, DatastoreContract.ChannelKeysEntry.KEY));
            if(entity.getProperty(DatastoreContract.ChannelKeysEntry.COLUMN_NAME_KEYS) != null) {
                keys = (List) entity.getProperty(DatastoreContract.ChannelKeysEntry.COLUMN_NAME_KEYS);
            } else {
                keys = new ArrayList<>();
            }
        } catch (EntityNotFoundException e) {
            //e.printStackTrace();
            entity = new Entity(DatastoreContract.ChannelKeysEntry.KIND, DatastoreContract.ChannelKeysEntry.KEY);
            keys = new ArrayList<>();
        }
        keys.add(channelKey);
        entity.setProperty(DatastoreContract.ChannelKeysEntry.COLUMN_NAME_KEYS, keys);
        datastore.put(entity);
    }

    public static List<String> readChannelKeys() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity entity;
        List<String> keys = new ArrayList<>();
        try {
            entity = datastore.get(KeyFactory.createKey(DatastoreContract.ChannelKeysEntry.KIND, DatastoreContract.ChannelKeysEntry.KEY));
            keys = (List) entity.getProperty(DatastoreContract.ChannelKeysEntry.COLUMN_NAME_KEYS);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return keys;
    }

    public static void deleteChannelKey(String channelKey) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastore.get(KeyFactory.createKey(DatastoreContract.ChannelKeysEntry.KIND, DatastoreContract.ChannelKeysEntry.KEY));
            if(entity.getProperty(DatastoreContract.ChannelKeysEntry.COLUMN_NAME_KEYS) != null) {
                List<String> keys = (List) entity.getProperty(DatastoreContract.ChannelKeysEntry.COLUMN_NAME_KEYS);
                keys.remove(channelKey);
                entity.setProperty(DatastoreContract.ChannelKeysEntry.COLUMN_NAME_KEYS, keys);
                datastore.put(entity);
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Long, String> readMenuTypes(){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query(DatastoreContract.MenuTypesEntry.KIND);
        PreparedQuery pq = datastore.prepare(q);
        HashMap<Long, String> typesMap = new HashMap<>();
        for(Entity result:pq.asIterable()){
            ArrayList<EmbeddedEntity> types = (ArrayList)result.getProperty("types");
            for(EmbeddedEntity type:types){
                typesMap.put((Long) type.getProperty("key"), (String) type.getProperty("name"));
            }
        }
        return typesMap;
    }

    public static OrderEntity readOrderEntity(Key key){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        OrderEntity orderEntity = new OrderEntity();
        try {
            Entity entity = datastore.get(key);
            orderEntity = entityToOrderEntity(entity);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return orderEntity;
    }

    public static List<OrderEntity> readOrderEntities() {
        Logger log = Logger.getLogger("Read Order");
        log.setLevel(Level.INFO);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query(DatastoreContract.OrdersEntry.KIND);
        Query.Filter statusFilter = new Query.FilterPredicate(DatastoreContract.OrdersEntry.COLUMN_NAME_STATUS, Query.FilterOperator.LESS_THAN, Globals.ORDER_COMPLETED);
        q.setFilter(statusFilter);
        PreparedQuery pq = datastore.prepare(q);
        List<OrderEntity> orderEntities = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            log.info("adding one");
            orderEntities.add(entityToOrderEntity(result));
        }
        log.info("finished adding");
        return orderEntities;
    }

    public static MenuItemEntity readMenuItemEntity(String keyString){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastore.get(KeyFactory.stringToKey(keyString));
            MenuItemEntity menuItemEntity = entityToMenuItemEntity(entity);
            return menuItemEntity;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OrderEntity entityToOrderEntity(Entity entity){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setPhoneNumber((String) entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_PHONE_NUMBER));
        orderEntity.setAddress((String) entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_ADDRESS));
        orderEntity.setRegistrationToken((String) entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_REGISTRATION_TOKEN));
        orderEntity.setCreatedAt((Long) entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_CREATED_AT));
        orderEntity.setStatus(((Long)entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_STATUS)).intValue());
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        for(EmbeddedEntity embeddedEntity:(List<EmbeddedEntity>)entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_ORDER_ITEMS)){
            orderItemEntities.add(embeddedEntityToOrderItemEntity(embeddedEntity));
        }
        orderEntity.setOrderItemEntities(orderItemEntities);
        return orderEntity;
    }

    public static OrderItemEntity embeddedEntityToOrderItemEntity(EmbeddedEntity entity){
        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setAmount(((Long) entity.getProperty(DatastoreContract.OrderItemEmbeddedEntry.COLUMN_NAME_AMOUNT)).intValue());
        orderItem.setMenuItemKeyString(KeyFactory.keyToString((Key) entity.getProperty(DatastoreContract.OrderItemEmbeddedEntry.COLUMN_NAME_MENU_ITEM_KEY)));
        if(entity.getProperty(DatastoreContract.OrderItemEmbeddedEntry.COLUMN_NAME_INGREDIENTS_EXCLUDED) != null) {
            orderItem.setIngredientsExcluded((List<String>) entity.getProperty(DatastoreContract.OrderItemEmbeddedEntry.COLUMN_NAME_INGREDIENTS_EXCLUDED));
        }
        return orderItem;
    }

    public static MenuItemEntity entityToMenuItemEntity(Entity entity){
        MenuItemEntity menuItem = new MenuItemEntity();
        menuItem.setName((String) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_NAME));
        menuItem.setAllergens((List<String>) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_ALLERGENS));
        menuItem.setIngredients((List<String>) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_INGREDIENTS));
        menuItem.setType((Long) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_TYPE));
        menuItem.setDescription((String) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_DESCRIPTION));
        menuItem.setPrice((Long) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_PRICE));
        menuItem.setServingUrl((String) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_SERVING_URL));
        menuItem.setKeyString(KeyFactory.keyToString(entity.getKey()));
        return menuItem;
    }
}
