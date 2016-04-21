package com.example.ric.myapplication.backend.util;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Created by ric on 21/04/16.
 */
public class AccountsUtil {
    public static String getSignInString(String requestUri){
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String link;
        String signInLink;
        if (user != null) {
            link = userService.createLogoutURL("/index");
            signInLink = "Hi "+user.getNickname()+", <a href=\""+link+"\" class=\"navbar-link\">click here to logout</a>";
        } else {
            link = userService.createLoginURL(requestUri);
            signInLink = "<a href=\""+link+"\" class=\"navbar-link\">Sign in</a>";
        }
        return signInLink;
    }
}
