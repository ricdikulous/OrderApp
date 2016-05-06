package com.example.ric.myapplication.backend.api;

import com.example.ric.myapplication.backend.gcm.GcmSender;
import com.example.ric.myapplication.backend.model.DatastoreContract;
import com.example.ric.myapplication.backend.model.MenuItemEntity;
import com.example.ric.myapplication.backend.model.MenuTypesEntity;
import com.example.ric.myapplication.backend.model.MenuVersionEntity;
import com.example.ric.myapplication.backend.util.DatastoreMenuUtil;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ric on 30/03/16.
 */
@Api(
    name = "menuApi",
    version = "v1",
    namespace = @ApiNamespace(
            ownerDomain = "api.backend.myapplication.ric.example.com",
            ownerName = "api.backend.myapplication.ric.example.com",
            packagePath=""
    )
)
public class MenuEndpoint {

    @ApiMethod(name="getMenuTypes")
    public MenuTypesEntity getMenuTypes(){
        MenuTypesEntity menuTypes = new MenuTypesEntity();
        menuTypes.setMenuTypes(DatastoreMenuUtil.readMenuTypes());
        return menuTypes;
    }

    @ApiMethod(name="getMenuItems")
    public List<MenuItemEntity> getMenuItems(){
        List<MenuItemEntity> menuItemEntities = new ArrayList<>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query(DatastoreContract.MenuItemsEntry.KIND);
        PreparedQuery pq = datastore.prepare(q);
        for(Entity result:pq.asIterable()){
            if(result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_INGREDIENTS) != null) {
                MenuItemEntity menuItemEntity = new MenuItemEntity();
                menuItemEntity.setName((String) result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_NAME));
                menuItemEntity.setDescription((String) result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_DESCRIPTION));
                menuItemEntity.setServingUrl((String) result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_SERVING_URL));
                menuItemEntity.setPrice((Long) result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_PRICE));
                menuItemEntity.setType((Long) result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_TYPE));
                menuItemEntity.setIngredients((ArrayList<String>) result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_INGREDIENTS));
                if(result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_ALLERGENS) != null &&
                        result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_ALLERGENS).getClass().equals(ArrayList.class)) {

                    menuItemEntity.setAllergens((ArrayList<String>) result.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_ALLERGENS));
                }
                menuItemEntity.setKeyString(KeyFactory.keyToString(result.getKey()));
                menuItemEntities.add(menuItemEntity);
            }
        }
        return menuItemEntities;
    }

    @ApiMethod(name="getMenuVersion")
    public MenuVersionEntity getMenuVersion(){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        MenuVersionEntity menuVersion = new MenuVersionEntity();
        menuVersion.setVersion(-1);
        try {
            Entity entity = datastore.get(KeyFactory.createKey(DatastoreContract.MenuVersionEntry.KIND, DatastoreContract.MenuVersionEntry.KEY_DOWNLOAD));
            menuVersion.setVersion(((Long) entity.getProperty(DatastoreContract.MenuVersionEntry.COLUMN_NAME_VERSION)).intValue());
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return menuVersion;
    }
}
