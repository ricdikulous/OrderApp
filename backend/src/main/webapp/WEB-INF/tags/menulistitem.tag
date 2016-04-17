<%@tag description="A list item for orders" pageEncoding="UTF-8"%>
<%@attribute name="menuItem" required="true" type="com.example.ric.myapplication.backend.model.MenuItemEntity"%>
<%@attribute name="orderItem" required="true" type="com.example.ric.myapplication.backend.model.OrderItemEntity"%>


<a href="#"  class="list-group-item">
    <h4 class="list-group-item-heading"><%=menuItem.getName()%></h4>
    <p class="list-group-item-text">
        Name: <%=menuItem.getName() %><br>
        Amount: <%=orderItem.getAmount()%><br>
    </p>
</a>