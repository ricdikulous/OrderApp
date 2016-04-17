package com.example.ric.myapplication.backend.servlet;

import com.example.ric.myapplication.backend.util.ChannelUtil;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ric on 17/04/16.
 */
public class ChannelCloseServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Code to retrieve user id, check rules and update game omitted for brevity
        ChannelUtil.sendUpdateToUser("boner");
    }
}
