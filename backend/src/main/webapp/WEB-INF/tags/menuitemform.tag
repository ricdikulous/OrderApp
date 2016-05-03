<%@tag description="Form used for menu items" pageEncoding="UTF-8"%>
<%@attribute name="menuItem" required="true" type="com.example.ric.myapplication.backend.model.MenuItemEntity"%>
<%@attribute name="uploadUrl" required="true" type="java.lang.String"%>
<%@attribute name="menuTypes" required="true" type="java.util.HashMap"%>
<form action="<%=uploadUrl%>" method="post" enctype="multipart/form-data" class="form-horizontal">
<%
        String name = "";
        if(menuItem.getName() != null){
            name = menuItem.getName();
        }
        String description = "";
        if(menuItem.getDescription() != null){
            description = menuItem.getDescription();
        }

    %>
    <div class="form-group">
        <label for="nameInput">Name</label>
        <input id="nameInput" type="text" name="name" value="<%=name%>"class="form-control">
    </div>
    <div class="form-group">
        <label for="descriptionInput">Description</label>
        <textArea id="descriptionInput" type="text" name="description" class="form-control" rows="3"><%=description%></textArea>
    </div>
    <div class="form-group">
        <label for="ingredientsInput">Ingredients</label>
        <textArea id="ingredientsInput" type="text" name="ingredients" class="form-control" rows="3" placeholder="Enter a comma separated list of ingredients"><%=menuItem.getIngredientsString()%></textArea>
    </div>
    <div class="form-group">
        <label for="allergensInput">Allergens</label>
        <textArea id="allergensInput" type="text" name="allergens" class="form-control" rows="3" placeholder="Enter a comma separated list of allergens"><%=menuItem.getAllergensString()%></textArea>
    </div>
    <div class="form-group">
        <label for="priceInput">Price</label>
        <div class="input-group">
            <span class="input-group-addon">$</span>
            <input id="priceInput" type="number" name="price" value="<%=menuItem.getPriceString()%>" min="0" max="9999" step="0.01" size="4" class="form-control">
        </div>
    </div>
    <div class="form-group">
        <label for="typeInput">Category</label>
        <select id="typeInput" name="type" class="form-control">
        <%
            for(Object key:menuTypes.keySet()){
                String typeName = (String) menuTypes.get(key);
                String selected = "";
                if(menuItem.getType()!= null && menuItem.getType().equals(key)){
                    selected = "selected";
                }
        %>
            <option value="<%=key%>" <%=selected%> > <%=typeName%> </option>
        <%
            }
        %>
        </select>
    </div>
    <div class="form-group">
        <label for="fileInput">Photo</label>
        <input id="fileInput" type="file" name="myFile">
    </div>
    <%
        String keyString = "";
        if(menuItem.getKeyString() != null){
            keyString = menuItem.getKeyString();
        }
    %>
    <input type="hidden" name="menuItemKeyString" value="<%=keyString%>">
    <input type="submit" value="Submit" class="btn btn-default">
</form>