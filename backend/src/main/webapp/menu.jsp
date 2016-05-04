<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ page import="com.example.ric.myapplication.backend.model.MenuItemEntity" %>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreMenuUtil"%>
<%@ page import="com.example.ric.myapplication.backend.util.AccountsUtil"%>
<%@ page import="com.example.ric.myapplication.backend.util.FormatUtil"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>



<%
    List<MenuItemEntity> menuItems = DatastoreMenuUtil.readMenuItems();
    HashMap<Long, String> types = DatastoreMenuUtil.readMenuTypes();
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
            <table id="menuTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Ingredients</th>
                        <th>Allergens</th>
                        <th>Price</th>
                        <th>Category</th>
                        <th>Created At</th>
                        <th>Updated At</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                <% for(MenuItemEntity menuItem:menuItems){ %>
                    <tr>
                        <td><%=menuItem.getName()%></td>
                        <td><%=FormatUtil.truncateDescription(menuItem.getDescription())%></td>
                        <td><%=FormatUtil.listToString(menuItem.getIngredients())%></td>
                        <td><%=FormatUtil.listToString(menuItem.getAllergens())%></td>
                        <td><%=FormatUtil.longCentsToCurrency(menuItem.getPrice())%></td>
                        <td><%=types.get(menuItem.getType())%></td>
                        <td><%=FormatUtil.timestampToDate(menuItem.getCreatedAt())%></td>
                        <td><%=FormatUtil.timestampToDate(menuItem.getUpdatedAt())%></td>
                        <td><a href="/upload.jsp?menuItemKeyString=<%=menuItem.getKeyString()%>">Edit Delete</a></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <script src="/js/menuTable.js"></script>
    </body>
</html>