<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreUtil" %>
<%@ page import="com.example.ric.myapplication.backend.util.AccountsUtil" %>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreMenuUtil" %>
<%@ page import="com.example.ric.myapplication.backend.model.MenuItemEntity" %>
<%@ page import="java.util.HashMap" %>



<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    HashMap<Long, String> menuTypes = DatastoreUtil.readMenuTypes();
    String uploadUrl = blobstoreService.createUploadUrl("/uploadmenuitem");
    String menuItemKeyString = request.getParameter("menuItemKeyString");
    MenuItemEntity menuItem = new MenuItemEntity();
    if(menuItemKeyString != null && !menuItemKeyString.equals("")){
        menuItem = DatastoreMenuUtil.readMenuItemEntity(menuItemKeyString);
    }
%>


<html>
    <head>
        <title>Add Item to Menu</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    </head>
    <body style="padding-top: 70px;">
        <t:navbar signInLink="<%=AccountsUtil.getSignInString(request.getRequestURI())%>"/>
        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1">
                    <t:menuitemform menuItem="<%=menuItem%>" uploadUrl="<%=uploadUrl%>" menuTypes="<%=menuTypes%>"/>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1">
                <%
                    if(menuItem.getServingUrl()!=null){
                %>
                    <img src="<%=menuItem.getServingUrl()%>">
                <%
                    }
                %>
                </div>
            </div>
        </div>
    </body>
</html>