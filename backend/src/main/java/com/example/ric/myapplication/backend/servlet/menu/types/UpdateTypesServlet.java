package com.example.ric.myapplication.backend.servlet.menu.types;

import com.example.ric.myapplication.backend.util.DatastoreMenuUtil;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ric on 5/05/16.
 */
public class UpdateTypesServlet extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String keys = req.getParameter("orderedKeys");
        String namesMapParam = req.getParameter("namesMap");
        List<String> orderedKeysString = new Gson().fromJson(keys, List.class);
        Map<String, String> namesMapString = new Gson().fromJson(namesMapParam, Map.class);
        Logger log = Logger.getLogger("Updating types");
        log.setLevel(Level.INFO);
        log.info(keys);
        log.info(namesMapParam);
        log.info(namesMapString.toString());
        List<Long> orderedKeys = new ArrayList<>();
        for(String key:orderedKeysString){
            orderedKeys.add(Long.valueOf(key));
        }
        Map<Long, String> namesMap = new HashMap<>();
        for(String key:namesMapString.keySet()){
            namesMap.put(Long.valueOf(key), namesMapString.get(key).trim());
        }
        DatastoreMenuUtil.updateMenuTypes(orderedKeys, namesMap);
    }
}
