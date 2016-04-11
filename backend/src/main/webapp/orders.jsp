<%@ page import="com.example.ric.myapplication.backend.model.OrderEntity" %>
<%@ page import="com.example.ric.myapplication.backend.model.OrderItemEntity" %>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreUtil"%>
<%@ page import="java.util.List" %>



<%
    List<OrderEntity> orderEntities = DatastoreUtil.readOrderEntities();
%>

<html>
    <head>
        <title>Upload Test</title>
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    </head>
    <body>
        <%=orderEntities.size()%>
        <% for(OrderEntity orderEntity:orderEntities){ %>
            <% for(OrderItemEntity orderItem:orderEntity.getOrderItemEntities()){ %>
                Amount: <%= orderItem.getAmount()%>
            <% } %>
        <% } %>
    </body>
</html>