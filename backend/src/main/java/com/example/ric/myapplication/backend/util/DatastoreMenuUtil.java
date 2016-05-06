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
import com.google.appengine.labs.repackaged.com.google.common.base.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ric on 2/05/16.
 */
public class DatastoreMenuUtil {

    public static void insertMenuTypes(ArrayList<Pair<Long, String>> types){
        List<EmbeddedEntity> typeEntities = new ArrayList<>();
        int i=0;
        for(Pair<Long, String>type:types) {
            Long key = type.getFirst();
            String name = type.getSecond();
            Integer position = i;
            EmbeddedEntity typeEntity = new EmbeddedEntity();
            typeEntity.setProperty(DatastoreContract.TypesEmbeddedEntry.COLUMN_NAME_KEY, key);
            typeEntity.setProperty(DatastoreContract.TypesEmbeddedEntry.COLUMN_NAME_NAME, name);
            //typeEntity.setProperty(DatastoreContract.TypesEmbeddedEntry.COLUMN_NAME_POSITION, position);
            typeEntities.add(typeEntity);
            i++;
        }
        Entity entity = new Entity(DatastoreContract.MenuTypesEntry.KIND, DatastoreContract.MenuTypesEntry.KEY);
        entity.setProperty(DatastoreContract.MenuTypesEntry.COLUMN_NAME_TYPES, typeEntities);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(entity);
        //put in datastore
    }

    public static void updateMenuTypes(List<Long> orderedKeys, Map<Long, String> namesMap){
        List<EmbeddedEntity> typeEntities = new ArrayList<>();
        List<Entity> updatedMenuItemEntities = new ArrayList<>();
        for(Integer i=0;i<orderedKeys.size();i++){
            Long oldKey = orderedKeys.get(i);
            String name = namesMap.get(oldKey);
            Long newKey = i.longValue()+1;
            updatedMenuItemEntities.addAll(updatedMenuItemEntities(oldKey, newKey));
            EmbeddedEntity typeEntity = new EmbeddedEntity();
            typeEntity.setProperty(DatastoreContract.TypesEmbeddedEntry.COLUMN_NAME_KEY, newKey);
            typeEntity.setProperty(DatastoreContract.TypesEmbeddedEntry.COLUMN_NAME_NAME, name);
            typeEntities.add(typeEntity);
        }
        Entity entity = new Entity(DatastoreContract.MenuTypesEntry.KIND, DatastoreContract.MenuTypesEntry.KEY);
        entity.setProperty(DatastoreContract.MenuTypesEntry.COLUMN_NAME_TYPES, typeEntities);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(entity);
        datastore.put(updatedMenuItemEntities);
    }

    public static void removeMenuType(Long keyToRemove){
        List<EmbeddedEntity> typeEntities = new ArrayList<>();
        List<Entity> updatedMenuItemEntities = new ArrayList<>();

        HashMap<Long, String> typesMap = readMenuTypes();
        SortedSet<Long> keys = new TreeSet<Long>(typesMap.keySet());
        Long newKey = 1L;
        Logger log = Logger.getLogger("Removing type: "+keyToRemove);
        log.setLevel(Level.INFO);
        for(Long key:keys){
            if(key !=keyToRemove){
                log.info(key+"=>"+newKey);
                typeEntities.add(createTypeEmbbededEntity(newKey, typesMap.get(key)));
                updatedMenuItemEntities.addAll(updatedMenuItemEntities(key, newKey));
                newKey++;
            }
        }
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(DatastoreContract.MenuTypesEntry.KIND, DatastoreContract.MenuTypesEntry.KEY);
        entity.setProperty(DatastoreContract.MenuTypesEntry.COLUMN_NAME_TYPES, typeEntities);
        datastore.put(entity);
        datastore.put(updatedMenuItemEntities);
    }

    public static EmbeddedEntity createTypeEmbbededEntity(Long key, String name){
        EmbeddedEntity typeEntity = new EmbeddedEntity();
        typeEntity.setProperty(DatastoreContract.TypesEmbeddedEntry.COLUMN_NAME_KEY, key);
        typeEntity.setProperty(DatastoreContract.TypesEmbeddedEntry.COLUMN_NAME_NAME, name);
        return typeEntity;
    }

    private static List<Entity> updatedMenuItemEntities(Long oldKey, Long newKey) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Logger log = Logger.getLogger("Updating types");
        log.setLevel(Level.INFO);
        log.info("updating: "+oldKey+"=>"+newKey);
        Query q = new Query(DatastoreContract.MenuItemsEntry.KIND);
        Query.Filter typeFilter = new Query.FilterPredicate(DatastoreContract.MenuItemsEntry.COLUMN_NAME_TYPE, Query.FilterOperator.EQUAL, oldKey);
        q.setFilter(typeFilter);
        PreparedQuery pq = datastore.prepare(q);
        List<Entity> entities = new ArrayList<>();
        for(Entity result:pq.asIterable()){
            log.info((String)result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_NAME));
            result.setProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_TYPE, newKey);
            entities.add(result);
        }
        return entities;
    }

    public static List<Entity> readMenuItemsByType(Long type) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Logger log = Logger.getLogger("Reading types");
        List<Entity> menuItems = new ArrayList<>();
        log.setLevel(Level.INFO);
        Query q = new Query(DatastoreContract.MenuItemsEntry.KIND);
        Query.Filter typeFilter = new Query.FilterPredicate(DatastoreContract.MenuItemsEntry.COLUMN_NAME_TYPE, Query.FilterOperator.EQUAL, type);
        q.setFilter(typeFilter);
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            menuItems.add(result);
        }
        return menuItems;
    }

    public static HashMap<Long, String> readMenuTypes(){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        HashMap<Long, String> typesMap = new HashMap<>();
        Key key = KeyFactory.createKey(DatastoreContract.MenuTypesEntry.KIND, DatastoreContract.MenuTypesEntry.KEY);
        try {
            Entity typesEntity = datastore.get(key);
            ArrayList<EmbeddedEntity> types = (ArrayList)typesEntity.getProperty("types");
            for(EmbeddedEntity type:types){
                typesMap.put((Long) type.getProperty("key"), (String) type.getProperty("name"));
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
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
