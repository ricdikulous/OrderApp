package com.example.ric.myapplication.backend.servlet;

import com.example.ric.myapplication.backend.model.DatastoreContract;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ric on 9/04/16.
 */
public class PayPalServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        //resp.setContentType("text/plain");
        //resp.getWriter().println("Please use the form to POST to this url");
        String paymentId = "PAY-11S38651SP486871UK4EK6PQ";
        InputStream is = PayPalServlet.class.getResourceAsStream("/sdk_config.properties");
        OAuthTokenCredential tokenCredential = null;
        try {
            tokenCredential = Payment.initConfig(is);
            String accessToken = tokenCredential.getAccessToken();
            //resp.getWriter().print(accessToken);
            Payment payment = Payment.get(accessToken, paymentId);
            if(payment!=null){
                resp.getWriter().println("payment not null");
                //resp.getWriter().println(payment.toString());
            } else {
                resp.getWriter().println("payment null");
            }
            resp.getWriter().println("amount: " + payment.getTransactions().get(0).getAmount().getTotal());
        } catch (PayPalRESTException e) {
            resp.getWriter().print("catch");
            resp.getWriter().print(e.toString());
            e.printStackTrace();
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query(DatastoreContract.OrdersEntry.KIND);
        Query.Filter paymentIdFilter = new Query.FilterPredicate(DatastoreContract.OrdersEntry.COLUMN_NAME_PAYMENT_ID, Query.FilterOperator.EQUAL, paymentId);
        q.setFilter(paymentIdFilter);
        PreparedQuery pq = datastore.prepare(q);
        int i =0;
        Key entityKey = null;
        Key givenKey = null;
        for(Entity e: pq.asIterable()){
            entityKey = e.getKey();
            givenKey = e.getKey();
            i++;
        }
        if(i == 1 && entityKey != null && entityKey.equals(givenKey)){
            resp.getWriter().println("Payment is legit!!");
        }
        resp.getWriter().println("occurance: "+i);

    }
}
