<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.example.ric.myapplication.backend.util.DatastoreUtil"%>
<%@ page import="java.util.HashMap" %>



<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    HashMap<Long, String> menuTypes = DatastoreUtil.readMenuTypes();
%>


<html>
    <head>
        <title>Upload Test</title>
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="row">
            <div class="col-md-10 col-lg-offset-1">
                <form action="<%= blobstoreService.createUploadUrl("/uploadmenuitem") %>" method="post" enctype="multipart/form-data" class="form-horizontal">
                        <div class="form-group">
                            <label for="nameInput">Name</label>
                            <input id="nameInput" type="text" name="name" class="form-control">
                        </div>
                        <div class="form-group">
                            <label for="descriptionInput">Description</label>
                            <textArea id="descriptionInput" type="text" name="description" class="form-control" rows="3"></textArea>
                        </div>
                        <div class="form-group">
                            <label for="ingredientsInput">Ingredients</label>
                            <textArea id="ingredientsInput" type="text" name="ingredients" class="form-control" rows="3" placeholder="Enter a comma separated list of ingredients"></textArea>
                        </div>
                        <div class="form-group">
                            <label for="allergensInput">Allergens</label>
                            <textArea id="allergensInput" type="text" name="allergens" class="form-control" rows="3" placeholder="Enter a comma separated list of allergens"></textArea>
                        </div>
                        <div class="form-group">
                            <label for="priceInput">Price</label>
                            <div class="input-group">
                                <span class="input-group-addon">$</span>
                                <input id="priceInput" type="number" name="price" min="0" max="9999" step="0.01" size="4" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="typeInput">Category</label>
                            <select id="typeInput" name="type" class="form-control">
                            <%
                                for(Long key:menuTypes.keySet()){
                                    String name = menuTypes.get(key);
                            %>
                                <option value="<%=key%>"><%=name%></option>
                            <%
                                }
                            %>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="fileInput">Photo</label>
                            <input id="fileInput" type="file" name="myFile">
                        </div>
                        <input type="submit" value="Submit" class="btn btn-default">
                </form>
            </div>
        </div>
    </body>
</html>