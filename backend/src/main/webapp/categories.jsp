<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ page import="com.example.ric.myapplication.backend.model.MenuItemEntity" %>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreMenuUtil"%>
<%@ page import="com.example.ric.myapplication.backend.util.AccountsUtil"%>
<%@ page import="com.example.ric.myapplication.backend.util.FormatUtil"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>



<%
    HashMap<Long, String> types = DatastoreMenuUtil.readMenuTypes();
%>

<html>
    <head>
        <title>Categories</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.css"/>
        <script type="text/javascript" src="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/Sortable/1.4.2/Sortable.min.js"></script>

    </head>
    <body style="padding-top: 70px;">
        <t:navbar signInLink="<%=AccountsUtil.getSignInString(request.getRequestURI())%>"/>
        <div class="container">
            <div class="row">
                <div class="col-lg-6 col-lg-offset-3">
                    <ul id="types" class="list-group">
                    <%
                        for(Object key:types.keySet()){
                            String name = (String) types.get(key);
                    %>
                        <li id="category-<%=key%>" class="list-group-item" data-name="<%=name%>" data-id="<%=key%>"><%=name%><i class="js-remove">✖</i></li>
                    <%
                        }
                    %>
                    </ul>
                    <div id="loading-div" hidden><img src="/img/loader.gif"> Loading... </div>
                    <div id="menuItemContainer"></div>
                   <input id="typeInput" type="text" maxlength="100" required name="type" placeholder="Enter a new category" class="form-control">
                   <button id="addCategory" class="btn btn-success">Add</button>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-lg-offset-6">
                    <button id="saveList" class="btn btn-primary">Save</button>
                </div>
            </div>
        </div>
        <script>
         var totalCategories = <%=types.size()%>;
        </script>
        <script src="/js/categories.js"></script>
    </body>
</html>