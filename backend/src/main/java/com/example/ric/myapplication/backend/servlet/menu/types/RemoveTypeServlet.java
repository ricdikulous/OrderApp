package com.example.ric.myapplication.backend.servlet.menu.types;

import com.example.ric.myapplication.backend.model.DatastoreContract;
import com.example.ric.myapplication.backend.util.DatastoreMenuUtil;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ric on 5/05/16.
 */
public class RemoveTypeServlet extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String key = req.getParameter("key");
        String id = req.getParameter("elId");
        String name = req.getParameter("name");

        Logger log = Logger.getLogger("Removing type");
        log.setLevel(Level.INFO);
        log.info(key);

        List<Entity> menuItems = DatastoreMenuUtil.readMenuItemsByType(Long.valueOf(key));
        JSONArray jsonArray = menuItemsToJsonArray(menuItems);
        JSONObject jsonObject = new JSONObject();

        resp.setContentType("application/json");

        boolean delete;
        if(menuItems.size()==0){
            //DatastoreMenuUtil.removeMenuType(Long.valueOf(key));
            delete = true;
        } else {
            delete = false;
        }

        //HashMap<Long, String> menuTypes = DatastoreMenuUtil.readMenuTypes();

        try {
            jsonObject.put("menuItems", jsonArray);
            jsonObject.put("delete", delete);
            jsonObject.put("id", id);
            jsonObject.put("name", name);
            //jsonObject.put("types", menuTypes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        resp.getWriter().println(jsonObject);
    }

    private JSONArray menuItemsToJsonArray(List<Entity> menuItems) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        for(Entity menuItem:menuItems){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(DatastoreContract.MenuItemsEntry.COLUMN_NAME_NAME, menuItem.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_NAME));
                jsonObject.put("menuKeyString", KeyFactory.keyToString(menuItem.getKey()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonObjects.add(jsonObject);
        }
        return new JSONArray(jsonObjects);
    }
}
