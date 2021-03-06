package com.example.ric.myapplication.backend.api;

import com.example.ric.myapplication.backend.model.MenuItemEntity;
import com.example.ric.myapplication.backend.gcm.GcmSender;
import com.example.ric.myapplication.backend.model.DatastoreContract;
import com.example.ric.myapplication.backend.model.OrderItemEntity;
import com.example.ric.myapplication.backend.model.OrderEntity;
import com.example.ric.myapplication.backend.model.OrderReceiptEntity;
import com.example.ric.myapplication.backend.util.ChannelUtil;
import com.example.ric.myapplication.backend.util.DatastoreMenuUtil;
import com.example.ric.myapplication.backend.util.DatastoreUtil;
import com.example.ric.myapplication.backend.util.Globals;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * Created by ric on 7/04/16.
 */
@Api(
        name = "orderApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "api.backend.myapplication.ric.example.com",
                ownerName = "api.backend.myapplication.ric.example.com",
                packagePath=""
        )
)
public class OrderEndpoint {

    @ApiMethod(name="putOrder")
    public OrderReceiptEntity putOrder(OrderEntity orderEntity){
        Logger log = Logger.getLogger("Receiving Order");
        log.setLevel(Level.INFO);
        OrderReceiptEntity orderReceipt = new OrderReceiptEntity();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //DELETE THIS "true" WHEN NO LONGER USING NO NETWORK FOR PAYPAL !!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        orderReceipt.setSuccess(true);//set this to false
        ///ALSO USE THIS WHEN IMPLEMENTING PROPER ORDER PROTOCOL
        if(DatastoreUtil.userGeneratedKeyAlreadyExists(orderEntity.getUserGeneratedKey())){
            log.info("KEY ALREADY EXISTS");
        }
        if(!paymentIdAlreadyExists(orderEntity.getPaymentId()) || true) {
            log.info("Payment Id does not already exist");
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            List<EmbeddedEntity> orderItems = new ArrayList<>();
            for (OrderItemEntity orderItemEntity : orderEntity.getOrderItemEntities()) {
                EmbeddedEntity orderItem = new EmbeddedEntity();
                MenuItemEntity menuItemEntity = DatastoreMenuUtil.readMenuItemEntity(orderItemEntity.getMenuItemKeyString());
                if(menuItemEntity != null && orderItemEntity.getAmount() > 0) {
                    orderItem.setProperty(DatastoreContract.OrderItemEmbeddedEntry.COLUMN_NAME_MENU_NAME, menuItemEntity.getName());
                    orderItem.setProperty(DatastoreContract.OrderItemEmbeddedEntry.COLUMN_NAME_AMOUNT, orderItemEntity.getAmount());
                    orderItem.setProperty(DatastoreContract.OrderItemEmbeddedEntry.COLUMN_NAME_MENU_ITEM_KEY, KeyFactory.stringToKey(orderItemEntity.getMenuItemKeyString()));
                    if (orderItemEntity.getIngredientsExcluded() != null) {
                        orderItem.setProperty(DatastoreContract.OrderItemEmbeddedEntry.COLUMN_NAME_INGREDIENTS_EXCLUDED, orderItemEntity.getIngredientsExcluded());
                    }
                    if(orderItemEntity.getSpecialRequest() != null) {
                        log.info(orderItemEntity.getSpecialRequest());
                        orderItem.setProperty(DatastoreContract.OrderItemEmbeddedEntry.COLUMN_NAME_SPECIAL_REQUEST, orderItemEntity.getSpecialRequest());
                    } else {
                        log.info("spcial request null!");
                    }
                    orderItems.add(orderItem);
                } else {
                    log.info("MENU ITEM NOT FOUND OR AMOUNT 0");
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    // ISSUE WITH ORDER CAN NOT FIND THE MENU ITEM OF THE ORDER ITEM
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                }
            }
            if(orderItems.size() > 0) {
                Entity order = new Entity(DatastoreContract.OrdersEntry.KIND);
                order.setProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_ORDER_ITEMS, orderItems);
                order.setProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_ADDRESS, orderEntity.getAddress());
                order.setProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_PAYMENT_ID, orderEntity.getPaymentId());
                order.setProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_PHONE_NUMBER, orderEntity.getPhoneNumber());
                order.setProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_REGISTRATION_TOKEN, orderEntity.getRegistrationToken());
                order.setProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_CREATED_AT, new Date().getTime());
                order.setProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_USER_GENERATED_KEY,orderEntity.getUserGeneratedKey());
                order.setProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_STATUS, Globals.ORDER_RECEIVED);

                InputStream is = OrderEndpoint.class.getResourceAsStream("/sdk_config.properties");
                OAuthTokenCredential tokenCredential = null;
                try {
                    tokenCredential = Payment.initConfig(is);
                    String accessToken = tokenCredential.getAccessToken();
                    Payment payment = Payment.get(accessToken, orderEntity.getPaymentId());
                    //double check amount
                    if (isCorrectAmount(payment.getTransactions().get(0).getAmount(), orderEntity)) {
                        log.info("is correct amount");
                        orderReceipt.setSuccess(true);
                        //datastore.put(order);
                    } else {
                        log.info("not correct");
                    }
                } catch (PayPalRESTException e) {
                    e.printStackTrace();
                }
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //DELETE THIS WHEN NO LONGER USING NO NETWORK FOR PAYPAL !!!!!!!!!!!!!!!!!!!
                //USE THE COMMENTED OUT ONE JUST ABOVE
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                Key key = datastore.put(order);
                OrderEntity orderEntity1 = DatastoreUtil.readOrderEntity(key);
                ChannelUtil.sendUpdateToAllUsers(orderEntity1);
            } else {
                log.info("No valid order Items");
            }

        } else {
            log.info("rejected from server cause Payment ID already assigned to another order");
            //ID already exists, possibly fraud? Most like fuck up
        }
        return orderReceipt;
    }

    @ApiMethod(name="getOrders")
    public List<OrderEntity> getOrders(){
        return DatastoreUtil.readOrderEntities();
    }

    @ApiMethod(name="advanceStatus")
    public OrderReceiptEntity advanceStatus(@Named("orderKeyString") String keyString, @Named("currentStatusString") String currentStatusString){
        Logger log = Logger.getLogger("Receiving Order");
        log.setLevel(Level.INFO);
        Long currentStatus = Long.valueOf(currentStatusString);
        OrderReceiptEntity orderReceiptEntity = new OrderReceiptEntity();
        orderReceiptEntity.setSuccess(false);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastore.get(KeyFactory.stringToKey(keyString));
            long status = (long) entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_STATUS);
            if(status <= currentStatus) {
                currentStatus++;
                //currentStatus = 5L;
                entity.setProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_STATUS, currentStatus);
                datastore.put(entity);
                orderReceiptEntity.setSuccess(true);
                orderReceiptEntity.setMessage("Successfully updated");
                ChannelUtil.sendUpdateToAllUsers(DatastoreUtil.entityToOrderEntity(entity));
                GcmSender.sendStatus(currentStatus.intValue(), (String)entity.getProperty(DatastoreContract.OrdersEntry.COLUMN_NAME_REGISTRATION_TOKEN));

            } else {
                orderReceiptEntity.setMessage("The status of order is higher in datastore, this request is probably old!");
                log.warning("Request has been ignored, status is higher in datastore request is probably old!");
            }
        } catch (EntityNotFoundException e) {
            orderReceiptEntity.setMessage("Could not find the order in the datastore");
            e.printStackTrace();
        }

        return orderReceiptEntity;
    }

    private static boolean paymentIdAlreadyExists(String paymentId){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query(DatastoreContract.OrdersEntry.KIND);
        Query.Filter paymentIdFilter = new Query.FilterPredicate(DatastoreContract.OrdersEntry.COLUMN_NAME_PAYMENT_ID, Query.FilterOperator.EQUAL, paymentId);
        q.setFilter(paymentIdFilter);
        PreparedQuery pq = datastore.prepare(q);
        int i =0;
        for(Entity e: pq.asIterable()){
            i++;
        }
        if(i == 0){
            return false;
        }
        return true;
    }

    private static boolean isCorrectAmount(Amount amount, OrderEntity orderEntity){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        BigDecimal total = new BigDecimal(amount.getTotal());

        Logger log = Logger.getLogger("Calculating amount");
        log.setLevel(Level.INFO);

        if(!amount.getCurrency().equals("AUD")){
            log.info("wrong currency: "+amount.getCurrency());
            return false;
        }
        long totalInCents = 0;
        for(OrderItemEntity orderItemEntity:orderEntity.getOrderItemEntities()){
            try {
                Entity item = datastore.get(KeyFactory.stringToKey(orderItemEntity.getMenuItemKeyString()));
                long priceInCents = (long) item.getProperty(DatastoreContract.MenuItemsEntry.COLUMN_NAME_PRICE);
                totalInCents += priceInCents*orderItemEntity.getAmount();
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
        BigDecimal calculatedTotal = new BigDecimal(totalInCents).divide(new BigDecimal(100));
        log.info("Given amount: "+calculatedTotal);
        log.info("Calc  amount: "+total);

        if(calculatedTotal.equals(total)){
            return true;
        }
        return false;
    }
}
