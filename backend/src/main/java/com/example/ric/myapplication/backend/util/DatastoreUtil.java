package com.example.ric.myapplication.backend.util;

import com.example.ric.myapplication.backend.model.DatastoreContract;
import com.example.ric.myapplication.backend.model.OrderEntity;
import com.example.ric.myapplication.backend.model.OrderItemEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
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

    public static OrderEntity entityToOrderEntity(Entity entity){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setPhoneNumber((String) entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_PHONE_NUMBER));
        orderEntity.setAddress((String) entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_ADDRESS));
        orderEntity.setRegistrationToken((String) entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_REGISTRATION_TOKEN));
        orderEntity.setCreatedAt((Long) entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_CREATED_AT));
        orderEntity.setStatus(((Long)entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_CREATED_AT)).intValue());
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
}
