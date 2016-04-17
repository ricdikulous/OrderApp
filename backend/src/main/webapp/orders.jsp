<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="com.example.ric.myapplication.backend.model.OrderEntity" %>
<%@ page import="com.example.ric.myapplication.backend.model.OrderItemEntity" %>
<%@ page import="com.example.ric.myapplication.backend.model.MenuItemEntity" %>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreUtil"%>
<%@ page import="com.example.ric.myapplication.backend.util.ChannelUtil"%>
<%@ page import="com.example.ric.myapplication.backend.util.Globals"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>



<%
    List<OrderEntity> orderEntities = DatastoreUtil.readOrderEntities();
    Long timestamp = new Date().getTime();
    String channelKey = "1"+timestamp;
    String token = ChannelUtil.getToken(channelKey);
%>

<html>
    <head>
        <title>Upload Test</title>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="/_ah/channel/jsapi"></script>

    </head>
    <body>
        <br>
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active"><a href="#received" aria-controls="received" role="tab" data-toggle="tab">Received</a></li>
                    <li role="presentation"><a href="#preparing" aria-controls="preparing" role="tab" data-toggle="tab">Preparing</a></li>
                    <li role="presentation"><a href="#cooking" aria-controls="cooking" role="tab" data-toggle="tab">Cooking</a></li>
                    <li role="presentation"><a href="#dispatched" aria-controls="dispatched" role="tab" data-toggle="tab">Dispatched</a></li>
                    <li role="presentation"><a href="#completed" aria-controls="completed" role="tab" data-toggle="tab">Completed</a></li>
                  </ul>

                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active" id="received">
                        <br>
                        <ul class="list-group" id="receivedList">
                        <% for(OrderEntity orderEntity:orderEntities){ %>
                            <%
                                if(orderEntity.getStatus().equals(Globals.ORDER_RECEIVED)){
                            %>
                                <li  class="list-group-item">
                                    <h4 class="list-group-item-heading">Order 123432</h4>
                                    <ul class="list-group">
                                <%
                                    for(OrderItemEntity orderItem:orderEntity.getOrderItemEntities()){
                                        MenuItemEntity menuItem = DatastoreUtil.readMenuItemEntity(orderItem.getMenuItemKeyString());
                                %>
                                        <t:menulistitem menuItem="<%=menuItem%>" orderItem="<%=orderItem%>"/>
                                <%
                                    }
                                %>
                                    </ul>

                                </li>
                            <%
                                }
                            %>
                        <% } %>
                        </ul>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="preparing">
                    <br>
                    <% for(OrderEntity orderEntity:orderEntities){ %>
                        <%
                            if(orderEntity.getStatus().equals(Globals.ORDER_PREPARING)){
                                for(OrderItemEntity orderItem:orderEntity.getOrderItemEntities()){
                                    MenuItemEntity menuItem = DatastoreUtil.readMenuItemEntity(orderItem.getMenuItemKeyString());
                         %>
                                    <t:menulistitem menuItem="<%=menuItem%>" orderItem="<%=orderItem%>"/>
                        <%
                                }
                            }
                        %>
                    <% } %>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="cooking">
                    <br>
                    <% for(OrderEntity orderEntity:orderEntities){ %>
                        <%
                            if(orderEntity.getStatus().equals(Globals.ORDER_COOKING)){
                                for(OrderItemEntity orderItem:orderEntity.getOrderItemEntities()){
                                    MenuItemEntity menuItem = DatastoreUtil.readMenuItemEntity(orderItem.getMenuItemKeyString());
                         %>
                                    <t:menulistitem menuItem="<%=menuItem%>" orderItem="<%=orderItem%>"/>
                        <%
                                }
                            }
                        %>
                    <% } %>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="dispatched">
                    <br>
                    <% for(OrderEntity orderEntity:orderEntities){ %>
                        <%
                            if(orderEntity.getStatus().equals(Globals.ORDER_DISPATCHED)){
                                for(OrderItemEntity orderItem:orderEntity.getOrderItemEntities()){
                                    MenuItemEntity menuItem = DatastoreUtil.readMenuItemEntity(orderItem.getMenuItemKeyString());
                         %>
                                    <t:menulistitem menuItem="<%=menuItem%>" orderItem="<%=orderItem%>"/>
                        <%
                                }
                            }
                        %>
                    <% } %>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="completed">
                    <br>
                    <% for(OrderEntity orderEntity:orderEntities){ %>
                        <%
                            if(orderEntity.getStatus().equals(Globals.ORDER_COMPLETED)){
                        %>
                        <%
                                for(OrderItemEntity orderItem:orderEntity.getOrderItemEntities()){
                                    MenuItemEntity menuItem = DatastoreUtil.readMenuItemEntity(orderItem.getMenuItemKeyString());
                         %>
                                    <t:menulistitem menuItem="<%=menuItem%>" orderItem="<%=orderItem%>"/>
                        <%
                                }
                            }
                        %>
                    <% } %>
                    </div>
                  </div>


                <%=orderEntities.size()%>
                <% for(OrderEntity orderEntity:orderEntities){ %>
                    <%
                        for(OrderItemEntity orderItem:orderEntity.getOrderItemEntities()){
                            MenuItemEntity menuItem = DatastoreUtil.readMenuItemEntity(orderItem.getMenuItemKeyString());
                     %>
                            <t:menulistitem menuItem="<%=menuItem%>" orderItem="<%=orderItem%>"/>
                    <%
                        }
                    %>
                <% } %>
            </div>
        </div>
        <button id="sendMessage">
            Send Message
        </button>
        <script>
        var token = "<%=token%>"
        var channelKey = "<%=channelKey%>"
        </script>
        <script src="/js/orders.js"></script>
    </body>
</html>