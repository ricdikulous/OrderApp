package com.example.ric.myapplication.backend.util;

import com.example.ric.myapplication.backend.model.MenuItemEntity;
import com.example.ric.myapplication.backend.model.OrderEntity;
import com.example.ric.myapplication.backend.model.OrderItemEntity;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.repackaged.com.google.api.client.json.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ric on 15/04/16.
 */
public class ChannelUtil {
    public static String getToken(String channelKey) {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        DatastoreUtil.saveChannelKey(channelKey);
        return channelService.createChannel(channelKey);
    }

    public static void sendUpdateToUser(String user) {
        //if (user != null) {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        String channelKey = "token";
        JSONObject message = new JSONObject();
        try {
            message.put("keyString", "fghfghfghf");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        channelService.sendMessage(new ChannelMessage(channelKey, message.toString()));
        //}
    }

    public static void sendUpdateToAllUsers(){
        List<String> channelKeys = DatastoreUtil.readChannelKeys();
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        JSONObject message = new JSONObject();
        try {
            message.put("keyString", "fghfghfghf");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger log = Logger.getLogger("SendUpdate");
        log.setLevel(Level.INFO);
        for(String channelKey:channelKeys){
            log.info(channelKey);
            channelService.sendMessage(new ChannelMessage(channelKey, message.toString()));
        }
    }

    public static void sendUpdateToAllUsers(OrderEntity orderEntity){
        List<String> channelKeys = DatastoreUtil.readChannelKeys();
        ChannelService channelService = ChannelServiceFactory.getChannelService();

        JSONObject message = new JSONObject(orderEntity);
        List<JSONObject> orderItemsJsonList = new ArrayList<>();
        for(OrderItemEntity orderItem:orderEntity.getOrderItemEntities()){
            JSONObject orderItemJson = new JSONObject(orderItem);
            MenuItemEntity menuItem = DatastoreUtil.readMenuItemEntity(orderItem.getMenuItemKeyString());
            JSONObject menuItemJson = new JSONObject(menuItem);
            try {
                orderItemJson.put("menuItem", menuItemJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            orderItemsJsonList.add(orderItemJson);
        }
        JSONArray orderItemsJson = new JSONArray(orderItemsJsonList);
        try {
            message.put("orderItemEntities", orderItemsJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger log = Logger.getLogger("SendUpdate");
        log.setLevel(Level.INFO);
        for(String channelKey:channelKeys){
            log.info(channelKey);
            channelService.sendMessage(new ChannelMessage(channelKey, message.toString()));
        }
    }
}
