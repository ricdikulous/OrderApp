var list = document.getElementById("types");
var processing = false;
var sortable = new Sortable(list,
                {
                    filter: ".js-remove",
                    onFilter: function (/**Event*/evt) {
                        if(!processing){
                           processing = true;
                           var key = evt.item.dataset.id;
                           console.log(evt);
                           $("#loading-div").show();
                           $("#menuItemContainer").hide();
                           $.post("/removetype", {key:key, elId:evt.item.id, name:evt.item.dataset.name})
                               .done(handleRemove);
                       }
                    },
                });

$("#saveList").click(function(){
    console.log(sortable.toArray());
    console.log("click");
    $types = $("#types");
    namesMap = {};
    orderedKeys = sortable.toArray();
    for(var i=0;i<$types.children().length;i++){
        //console.log($types.children()[i]);
        console.log($types.children()[i].dataset.name);
        console.log(orderedKeys[i]);
        namesMap[orderedKeys[i]] = $types.children()[i].dataset.name;
    }
    console.log(namesMap);
    if(!processing){
        processing = true;
        $("#loading-div").show();
        $.post("/updatetypes", {orderedKeys:JSON.stringify(sortable.toArray()), namesMap:JSON.stringify(namesMap)}).done(handleUpdate);
    }
});

$("#addCategory").click(function(){
    if(!processing){
        $typeInput = $("#typeInput");
        name = $typeInput.val();
        if(name.length){
            totalCategories++;
            html = '<li id="category-'+totalCategories+'" class="list-group-item" data-name="'+name+'" data-id="'+totalCategories+'">'+name+'<i class="js-remove">✖</i></li>'
            $("#types").append(html);
            $typeInput.val('');
        }
    }
});

var handleUpdate = function(data){
    processing = false;
    $("#loading-div").hide();
    heading="Update successful!";
    message="Page will now refresh"
    html= '<div id="menuItemContainer">';
    html += makeAlert("success", heading, message);
    html+= '</div>'
    $("#menuItemContainer").replaceWith(html);
    location.reload(false);
}

var handleRemove = function(data){
    console.log(data);
    categoryName = data.name
    processing = false;
    $("#loading-div").hide();

    html= '<div id="menuItemContainer">';

    if(!data.delete){
        heading = 'Deleting '+categoryName+' is not possible'
        message = categoryName+' can not be deleted as the menu items below reference it. Please either delete these items or edit them so they are in a different category';

        html += makeAlert('warning', heading, message);

        html += '<div class="list-group">';
        for(var i=0;i<data.menuItems.length;i++){
            html += '<a class="list-group-item" href="/upload.jsp?menuItemKeyString='+data.menuItems[i].menuKeyString+'">'+data.menuItems[i].name+'</a>';
        }
        html += '</div>';
    } else {
        $("#"+data.id).remove();
        heading = 'Successfully delete '+categoryName;
        message = 'Please click the "Save" button below to confirm changes';

        html += makeAlert('info', heading, message);

        /*html = '<ul id="types" class="list-group">';
        var orderedKeys = data.orderedKeys
        for(var key in orderedKeys){
            if(orderedKeys.hasOwnProperty(key)){
                name = orderedKeys[key];
                html += '<li id="category-<%=key%>" class="list-group-item" data-id="'+key+'">'+name+'<i class="js-remove">✖</i></li>'
            }
        }
        html += '</ul>';
        $("#types").replaceWith(html);*/
    }
    html += '</div>';
    $("#menuItemContainer").replaceWith(html);


}

function makeAlert(level, heading, message){
        return '<div class="alert alert-'+level+' alert-dismissible fade in" role="alert">'+
                    '<button type="button" class="close" data-dismiss="alert" aria-label="Close">'+
                        '<span aria-hidden="true">×</span>'+
                    '</button>'+
                    '<h4>'+heading+'</h4>'+
                    '<p>'+message+'</p>'+
                '</div>';
}
