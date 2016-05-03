package com.example.ric.myapplication.backend.servlet;

import com.example.ric.myapplication.backend.util.DatastoreMenuUtil;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ric on 31/03/16.
 */
public class UploadMenuItem extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        //String name = req.getParameter("name");
        resp.getWriter().println(req.getParameterNames().toString());
        Enumeration paramNames = req.getParameterNames();


        //UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser();
        //if(user!= null) {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("myFile");


        while(paramNames.hasMoreElements()){
            String paramName = (String) paramNames.nextElement();
            //newEntity.setProperty(paramName, req.getParameter(paramName));
            resp.getWriter().println(paramName + ": " + req.getParameter(paramName));
        }
        //advertisementEntity.setUserId(user.getUserId());

        Logger log = Logger.getLogger("Input");
        log.setLevel(Level.INFO);

        Entity newEntity = new Entity("MenuItem");

        if(!req.getParameter("menuItemKeyString").equals("")){
            newEntity = DatastoreMenuUtil.readMenuItem(req.getParameter("menuItemKeyString"));
        }

        if(newEntity ==null){
            //has a key string but couldnt be found
            //throw some error
        }

        String[] ingredients = req.getParameter("ingredients").split(",");

        List<String> ingredientsList = new ArrayList<>();
        for(String s:ingredients) {
            if(!s.trim().equals("")) {
                ingredientsList.add(s.trim());
            }
        }


        String[] allergens = req.getParameter("allergens").split(",");
        List<String> allergensList = new ArrayList<>();
        for(String s:allergens) {
            if(!s.trim().equals("")) {
                allergensList.add(s.trim());
            }
        }

        BigDecimal bigDecimal = new BigDecimal(req.getParameter("price"));
        long priceInCents = bigDecimal.multiply(new BigDecimal(100)).longValue();

        newEntity.setProperty("name", req.getParameter("name"));
        newEntity.setProperty("description", req.getParameter("description"));
        newEntity.setProperty("ingredients", ingredientsList);
        if(allergensList.size() > 0) {
            newEntity.setProperty("allergens", allergensList);
        }
        if(newEntity.getProperty("createdAt") == null){
            newEntity.setProperty("createdAt", new Date().getTime());
        }
        newEntity.setProperty("updatedAt", new Date().getTime());
        newEntity.setProperty("price", priceInCents);
        newEntity.setProperty("type", Integer.valueOf(req.getParameter("type")));

        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        BlobKey blobKey= blobKeys.get(0);
        final BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
        long size = blobInfo.getSize();
        if(size > 0){
            if(newEntity.getProperty("blobKey") != null){
                blobstoreService.delete( (BlobKey) newEntity.getProperty("blobKey"));
            }
            newEntity.setProperty("blobKey", blobKey);
            ServingUrlOptions servingUrlOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);
            String url = imagesService.getServingUrl(servingUrlOptions);
            newEntity.setProperty("servingUrl", url);
        }else{
            blobstoreService.delete(blobKey);
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(newEntity);

        resp.getWriter().println("<br><a href=\"/upload.jsp\">Add another!</a>");

        /*if(req.getParameter("advertisementKeyString") != ""){
            //doing update
            try {
                Entity entityToUpdate = datastore.get(KeyFactory.stringToKey(req.getParameter("advertisementKeyString")));
                if(entityToUpdate.getProperty("userId") != null && entityToUpdate.getProperty("userId").equals(user.getUserId())){
                    //updateAdvertisementEntity(entityToUpdate, newEntity, datastore);
                    resp.sendRedirect("success.jsp?type=updated");
                } else {
                    resp.sendRedirect("problem.html");
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            newEntity.setProperty("createdAt", new Date().getTime());
            //insertAdvertisementEntity(newEntity, datastore);
            resp.sendRedirect("success.jsp?type=created");
        }*/

    }
}
