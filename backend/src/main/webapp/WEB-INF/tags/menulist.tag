<%@tag description="A list item for orders" pageEncoding="UTF-8"%>
<%@attribute name="menuItem" required="true" type="com.example.ric.myapplication.backend.model.MenuItemEntity"%>
<%@attribute name="orderItem" required="true" type="com.example.ric.myapplication.backend.model.OrderItemEntity"%>

<% for(OrderEntity orderEntity:orderEntities){ %>
    <%
        if(orderEntity.getStatus().equals(Globals.ORDER_RECEIVED)){
            for(OrderItemEntity orderItem:orderEntity.getOrderItemEntities()){
                MenuItemEntity menuItem = DatastoreUtil.readMenuItemEntity(orderItem.getMenuItemKeyString());
     %>
                <t:menulistitem menuItem="<%=menuItem%>" orderItem="<%=orderItem%>"/>
    <%
            }
        }
    %>
<% } %>