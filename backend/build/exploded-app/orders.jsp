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
    Long timestamp = new Date().getTime();
    String channelKey = "1"+timestamp;
    String token = ChannelUtil.getToken(channelKey);
%>

<html>
    <head>
        <title>Upload Test</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
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
                        </ul>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="preparing">
                        <br>
                        <ul class="list-group" id="preparingList">
                        </ul>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="cooking">
                        <br>
                        <ul class="list-group" id="cookingList">
                        </ul>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="dispatched">
                        <br>
                        <ul class="list-group" id="dispatchedList">
                        </ul>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="completed">
                        <br>
                        <ul class="list-group" id="completedList">
                        </ul>
                    </div>
                  </div>
            </div>
        </div>
        <script>
        var token = "<%=token%>"
        var channelKey = "<%=channelKey%>"
        </script>
        <script src="/js/orders.js"></script>
        <script src="https://apis.google.com/js/client.js?onload=initEndpoints"></script>
    </body>
</html>