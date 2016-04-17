package com.example.ric.myapplication.backend.servlet;

import com.example.ric.myapplication.backend.util.ChannelUtil;
import com.example.ric.myapplication.backend.util.DatastoreUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ric on 17/04/16.
 */
public class ChannelCloseServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String channelKey = req.getParameter("channelKey");
        Logger log = Logger.getLogger("Channel CLose");
        log.setLevel(Level.INFO);
        log.info(channelKey);
        DatastoreUtil.deleteChannelKey(channelKey);
    }
}
