<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="com.example.ric.myapplication.backend.model.OrderEntity" %>
<%@ page import="com.example.ric.myapplication.backend.model.OrderItemEntity" %>
<%@ page import="com.example.ric.myapplication.backend.model.MenuItemEntity" %>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreUtil"%>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreMenuUtil"%>
<%@ page import="com.example.ric.myapplication.backend.util.ChannelUtil"%>
<%@ page import="com.example.ric.myapplication.backend.util.AccountsUtil"%>
<%@ page import="com.example.ric.myapplication.backend.util.Globals"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>



<%
    List<MenuItemEntity> menuItems = DatastoreMenuUtil.readMenuItems();
%>

<html>
    <head>
        <title>Menu</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="/_ah/channel/jsapi"></script>
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.css"/>
        <script type="text/javascript" src="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.js"></script>
    </head>
    <body style="padding-top: 70px;">
        <t:navbar signInLink="<%=AccountsUtil.getSignInString(request.getRequestURI())%>"/>
        <div class="container">
            <table id="example" class="display" cellspacing="0" width="100%">
                <t:menugridheader/>
                <tbody>
                <% for(MenuItemEntity menuItem:menuItems){ %>
                    <t:menugriditem menuItem="<%=menuItem%>"/>
                <% } %>
                </tbody>
            </table>
        </div>
        <script src="/js/menuTable.js"></script>
    </body>
</html>