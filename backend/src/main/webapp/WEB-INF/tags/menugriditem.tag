<%@tag description="A grid row for a menu item" pageEncoding="UTF-8"%>
<%@attribute name="menuItem" required="true" type="com.example.ric.myapplication.backend.model.MenuItemEntity"%>

<tr>
    <td><%=menuItem.getName()%></td>
    <td class="truncate"><%=menuItem.getDescription()%></td>
    <td><%=menuItem.getIngredients()%></td>
    <td><%=menuItem.getAllergens()%></td>
    <td><%=menuItem.getPrice()%></td>
    <td><%=menuItem.getType()%></td>
    <td></td>
    <td></td>
    <td><a href="/upload.jsp?menuItemKeyString=<%=menuItem.getKeyString()%>">Edit Delete</a></td>
</tr>
