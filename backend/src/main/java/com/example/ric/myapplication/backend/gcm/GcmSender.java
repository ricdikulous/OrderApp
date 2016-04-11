package com.example.ric.myapplication.backend.gcm;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ric on 10/04/16.
 */
public class GcmSender {
    public static final String API_KEY = System.getProperty("gcm.api.key");
    private static final int STATUS = 1;


    public static void sendStatus(int status, String registrationId) {
        JSONObject jData = new JSONObject();
        JSONObject jGcmData = new JSONObject();
        try {
            jData.put("type", STATUS);
            jData.put("status", status);
            jGcmData.put("to", registrationId);
            jGcmData.put("data", jData);
            Logger log = Logger.getLogger("ChatLog");
            log.setLevel(Level.INFO);
            log.info("Location Push");
            log.info(jGcmData.toString());
            sendMessage(jGcmData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void sendMessage(JSONObject jGcmData){
        Logger log = Logger.getLogger("MessageLog");
        log.setLevel(Level.INFO);
        try {
            log.info(jGcmData.toString());

            // Create connection to send GCM Message request.
            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = convertStreamToString(inputStream);
            System.out.println(resp);
            log.info(resp);
            try {
                JSONObject jsonObject = new JSONObject(resp);
                log.info(jsonObject.getString("failure")+" fails");
            } catch (JSONException e) {
                log.info("jsonObjectfailed");
            }
            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
        } catch (IOException e) {
            System.out.println("Unable to send GCM message.");
            System.out.println("Please ensure that API_KEY has been replaced by the server " +
                    "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
            log.info("error sending");
            log.info(e.getMessage());
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


}
