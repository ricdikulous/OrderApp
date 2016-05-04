package com.example.ric.myapplication.backend.servlet;

import com.example.ric.myapplication.backend.util.DatastoreMenuUtil;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ric on 3/05/16.
 */
public class DeleteMenuItem extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if(req.getParameter("menuItemKeyString") != null){
            DatastoreMenuUtil.deleteMenuItem(req.getParameter("menuItemKeyString"));
        }
        resp.getWriter().println("Item deleted!");

    }
}
