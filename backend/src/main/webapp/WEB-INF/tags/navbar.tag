<%@tag description="Nav bar template" pageEncoding="UTF-8"%>
<%@attribute name="signInLink" required="true"%>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="/index">Order App</a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="/menu.jsp" class="navbar-link">Menu</a></li>
                <li><a href="/orders.jsp" class="navbar-link">Orders</a></li>
                <li><a href="/upload.jsp" class="navbar-link">Add to menu</a></li>
            </ul>
            <p class="navbar-text navbar-right">${signInLink}</p>
        </div>
    </div>
</div>