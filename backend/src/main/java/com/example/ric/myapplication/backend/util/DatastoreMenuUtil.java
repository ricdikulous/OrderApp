package com.example.ric.myapplication.backend.util;

import com.example.ric.myapplication.backend.model.DatastoreContract;
import com.example.ric.myapplication.backend.model.MenuItemEntity;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
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
 * Created by ric on 2/05/16.
 */
public class DatastoreMenuUtil {

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

    public static List<MenuItemEntity> readMenuItems(){
        List<MenuItemEntity> menuItemEntities = new ArrayList<>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query(DatastoreContract.MenuItemsEntry.KIND);
        PreparedQuery pq = datastore.prepare(q);
        for(Entity result:pq.asIterable()){
            if(result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_INGREDIENTS) != null) {
                menuItemEntities.add(entityToMenuItemEntity(result));
            }
        }
        return menuItemEntities;
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

    public static Entity readMenuItem(String menuKeyString) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastore.get(KeyFactory.stringToKey(menuKeyString));
            return entity;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteMenuItem(String menuKeyString){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
        Key menuKey = KeyFactory.stringToKey(menuKeyString);
        try {
            Entity menuItem = datastore.get(menuKey);
            if(menuItem.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_BLOB_KEY) != null){
                blobstore.delete((BlobKey) menuItem.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_BLOB_KEY));
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        datastore.delete(menuKey);
    }

    public static MenuItemEntity entityToMenuItemEntity(Entity entity){
        MenuItemEntity menuItem = new MenuItemEntity();
        menuItem.setName((String) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_NAME));
        if(entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_ALLERGENS) != null && entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_ALLERGENS).getClass().equals(ArrayList.class)) {
            menuItem.setAllergens((List<String>) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_ALLERGENS));
        }
        menuItem.setIngredients((List<String>) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_INGREDIENTS));
        menuItem.setType((Long) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_TYPE));
        menuItem.setDescription((String) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_DESCRIPTION));
        menuItem.setPrice((Long) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_PRICE));
        menuItem.setServingUrl((String) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_SERVING_URL));
        menuItem.setCreatedAt((Long) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_CREATED_AT));
        menuItem.setUpdatedAt((Long) entity.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_UPDATED_AT));
        menuItem.setKeyString(KeyFactory.keyToString(entity.getKey()));
        return menuItem;
    }
}
