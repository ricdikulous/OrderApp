<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreUtil" %>
<%@ page import="com.example.ric.myapplication.backend.util.AccountsUtil" %>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreMenuUtil" %>
<%@ page import="com.example.ric.myapplication.backend.model.MenuItemEntity" %>
<%@ page import="com.example.ric.myapplication.backend.util.FormatUtil" %>
<%@ page import="com.example.ric.myapplication.backend.util.Globals" %>
<%@ page import="java.util.HashMap" %>



<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    HashMap<Long, String> menuTypes = DatastoreMenuUtil.readMenuTypes();
    String uploadUrl = blobstoreService.createUploadUrl("/uploadmenuitem");
    String menuItemKeyString = request.getParameter("menuItemKeyString");
    MenuItemEntity menuItem = new MenuItemEntity();
    if(menuItemKeyString != null && !menuItemKeyString.equals("")){
        menuItem = DatastoreMenuUtil.readMenuItemEntity(menuItemKeyString);
    }
    String name = "";
    if(menuItem.getName() != null){
       name = menuItem.getName();
    }
    String description = "";
    if(menuItem.getDescription() != null){
       description = menuItem.getDescription();
    }
    String keyString = "";
    if(menuItem.getKeyString() != null){
       keyString = menuItem.getKeyString();
    }

%>


<html>
    <head>
        <title>Add Item to Menu</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    </head>
    <body style="padding-top: 70px;">
        <t:navbar signInLink="<%=AccountsUtil.getSignInString(request.getRequestURI())%>"/>
        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1">
                   <form action="<%=uploadUrl%>" method="post" enctype="multipart/form-data" class="form-horizontal">
                       <div class="form-group">
                           <label for="nameInput">Name</label>
                           <input id="nameInput" type="text" maxlength="<%=Globals.MAX_NAME_LENGTH%>" required name="name" value="<%=name%>"class="form-control">
                       </div>
                       <div class="form-group">
                           <label for="descriptionInput">Description</label>
                           <textArea id="descriptionInput" type="text" maxlength="<%=Globals.MAX_DESCRIPTION_LENGTH%>" name="description" class="form-control" rows="3"><%=description%></textArea>
                       </div>
                       <div class="form-group">
                           <label for="ingredientsInput">Ingredients</label>
                           <textArea id="ingredientsInput" type="text" name="ingredients" class="form-control" rows="3" placeholder="Enter a comma separated list of ingredients"><%=FormatUtil.listToString(menuItem.getIngredients())%></textArea>
                       </div>
                       <div class="form-group">
                           <label for="allergensInput">Allergens</label>
                           <textArea id="allergensInput" type="text" name="allergens" class="form-control" rows="3" placeholder="Enter a comma separated list of allergens"><%=FormatUtil.listToString(menuItem.getAllergens())%></textArea>
                       </div>
                       <div class="form-group">
                           <label for="priceInput">Price</label>
                           <div class="input-group">
                               <span class="input-group-addon">$</span>
                               <input id="priceInput" type="number" name="price" value="<%=FormatUtil.longCentsToStringSansSymbol(menuItem.getPrice())%>" min="0" max="9999" step="0.01" size="4" class="form-control">
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
                       <input type="hidden" name="menuItemKeyString" value="<%=keyString%>">
                       <input type="submit" value="Submit" class="btn btn-default">
                   </form>
                   <form id="deleteMenuItemForm" action="/deletemenuitem" method="post" class="form-horizontal">
                      <input type="hidden" name="menuItemKeyString" value="<%=keyString%>">
                   </form>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1">
                    <input id="deleteButton" type="submit" value="Delete" class="btn btn-danger">
                </div>
            </div>
            <div class="row">
                <div class="col-lg-4 col-lg-offset-1">
                <%
                    if(menuItem.getServingUrl()!=null){
                %>
                    <img id="imagePreview" src="<%=menuItem.getServingUrl()%>">
                <%
                    }
                %>
                </div>
                <div class="col-lg-4 col-lg-offset-1">
                    <img id="imagePreview" src="">
                </div>
            </div>
        </div>
        <script src="/js/menuItemUpload.js"></script>
    </body>
</html>