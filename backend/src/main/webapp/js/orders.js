    var makeNew = true;
    var channelConnected = false;
    var endpointConnected = false;
    var orders = {};

    onOpened = function() {
      channelConnected = true;
      callEndpoint();
      console.log("onConnected!");
      console.log("ChannelKey: "+channelKey);
    };

    onMessage = function(message){
        var messageObject = JSON.parse(message.data);
        console.log("onMessage!")
        console.log(messageObject);

        addOrderToList(messageObject);
    }

    onError = function(){
        console.log("onError!");
        makeNew = true;
    }

    onClose = function(){
        console.log("onClose!");
        var xhr = new XMLHttpRequest();
        xhr.open('POST', "channelclose", true);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        var params;
        if(makeNew){
            newKey = makeNewKey();
            params = "channelKey="+channelKey+"&makeNew="+makeNew+"&newKey="+newKey;
            channelKey = newKey;
        } else {
            params = "channelKey="+channelKey+"&makeNew="+makeNew;
        }
        xhr.send(params);
        if(makeNew){
            xhr.onreadystatechange = function() {
                if (xhr.readyState == XMLHttpRequest.DONE) {
                    token = xhr.responseText;
                    makeNewChannel();
                }
            }
        }
    }

    handleWindowClose = function(){
        makeNew = false;
        socket.close();
    }

    function makeNewKey(){
        return 1+Date.now();
    }

    function makeNewChannel(){
        channel = new goog.appengine.Channel(token);
        socket = channel.open();
        socket.onopen = onOpened;
        socket.onmessage = onMessage;
        socket.onerror = onError;
        socket.onclose = onClose;
    }


    function addOrderToList(order){
        orders[order.orderKeyString] = order;
        if($("#list-item-"+order.orderKeyString).length){
            $("#list-item-"+order.orderKeyString).fadeOut("normal", function() {
                                                      $(this).remove();
                                                  });
        }
        var listContent = '<li id="list-item-'+order.orderKeyString+'" class="list-group-item">'+
                               '<h4 class="list-group-item-heading">Order: '+order.orderKeyString+'</h4>'+
                               '<p>Received: '+new Date(Number(order.createdAt))+'</p>'+
                               '<ul class="list-group">';
        for(var i=0;i<order.orderItemEntities.length;i++){
            listContent+= '<a href="#"  class="list-group-item">'+
                                '<h4 class="list-group-item-heading">'+order.orderItemEntities[i].name+'</h4>'+
                                '<p class="list-group-item-text">'+
                                'Amount: '+order.orderItemEntities[i].amount+'<br>';
            if(order.orderItemEntities[i].ingredientsExcluded != undefined){
                listContent += getIngredientsExcludedList(order.orderItemEntities[i].ingredientsExcluded);
            }
            listContent+= '</p></a>';
        }
        listContent += '</ul><button class="btn btn-default advance-status-button" data-key="'+order.orderKeyString+'" data-status="'+order.status+'">Advance</button>'+
                '<div id="loading-div-'+order.orderKeyString+'" hidden ><img src="http://smallenvelop.com/wp-content/uploads/2014/08/Preloader_8.gif"> Sending status update... </div></li>'
        switch(order.status){
            case 5:
                $(listContent).prependTo("#receivedList");
                break;
            case 6:
                $(listContent).prependTo("#preparingList");
                break;
            case 7:
                $(listContent).prependTo("#cookingList");
                break;
            case 8:
                $(listContent).prependTo("#dispatchedList");
                break;
            case 9:
                $(listContent).prependTo("#completedList");
                break;
            default:
                console.log("Order status is invalid, status: "+order.status);
        }
        updateBadges();
    }

    function getIngredientsExcludedList(ingredientsExcluded){
        var html = '<b>Ingredient Excluded: '
        for(var i=0;i<ingredientsExcluded.length;i++){
            html += ingredientsExcluded[i]+", ";
        }
        html = html.replace(/,\s*$/, "");
        html += '</b><br>';
        return html;
    }

    function updateBadges(){
        var received = 0;
        var preparing = 0;
        var cooking = 0;
        var dispatched = 0;
        var completed = 0;
        console.log(orders);
        for (var property in orders) {
            console.log(property);
            if (orders.hasOwnProperty(property)) {
                switch(orders[property].status){
                        case 5:
                            received++;
                            break;
                        case 6:
                            preparing++;
                            break;
                        case 7:
                            cooking++;
                            break;
                        case 8:
                            dispatched++;
                            break;
                        case 9:
                            completed++;
                            break;
                    }
                }
        }
        if(received>0){
            $("#recevied-list-empty-text").hide();
            $("#received-badge").text(received);
        } else {
            $("#recevied-list-empty-text").show();
            $("#received-badge").text("");
        }
        if(preparing>0){
            $("#preparing-list-empty-text").hide();
            $("#preparing-badge").text(preparing);
        } else {
            $("#preparing-list-empty-text").show();
            $("#preparing-badge").text("");
        }
        if(cooking>0){
            $("#cooking-list-empty-text").hide();
            $("#cooking-badge").text(cooking);
        } else {
            $("#cooking-list-empty-text").show();
            $("#cooking-badge").text("");
        }
        if(dispatched>0){
            $("#dispatched-list-empty-text").hide();
            $("#dispatched-badge").text(dispatched);
        } else {
            $("#dispatched-list-empty-text").show();
            $("#dispatched-badge").text("");
        }
        if(completed>0){
            $("#completed-list-empty-text").hide();
            $("#completed-badge").text(completed);
        } else {
            $("#completed-list-empty-text").show();
            $("#completed-badge").text("");
        }
        /*
        $("#preparing-badge").text(preparing);
        $("#cooking-badge").text(cooking);
        $("#dispatched-badge").text(dispatched);
        $("#completed-badge").text(completed);*/
    }

    $(document).ready(function(){
        makeNewChannel();
    });
    window.onbeforeunload = handleWindowClose;

    function initEndpoints(){
        console.log("initEnpoints");
        var ROOT = 'https://endpointstutorial-1119.appspot.com/_ah/api';
        gapi.client.load('orderApi', 'v1', function() {
            endpointConnected = true;
            callEndpoint();
        }, ROOT);
    }

    function callEndpoint(){
    //console.log(gapi.client);
        if(channelConnected && endpointConnected){
            console.log(gapi.client);
            gapi.client.orderApi.getOrders().execute(function(resp) {
                $("#page-loading-div").hide();
                if(resp != null && resp.items != null){
                    console.log(resp);
                    for(var i=0;i<resp.items.length;i++){
                        addOrderToList(resp.items[i]);
                    }
                }
            });
        } else {
            console.log("BOTH ARE NOT CONNECTED");
        }
    }

    $(document).on("click",".advance-status-button", function(e){
        var $button = $(e.target);
        //$button.prop('disabled', true);
        var key  = $button.data("key");
        var status  = $button.data("status");
        var $orderListItem = $("#list-item-"+key);
        var $loadingDiv = $("#loading-div-"+key);
        var $alertDiv = $("#alert-div-"+key);
        if($alertDiv.length){
            $alertDiv.alert('close');
        }
        $loadingDiv.show();
        gapi.client.orderApi.advanceStatus({"orderKeyString":key, "currentStatusString":status}).execute(function(resp){
            console.log(resp);
            console.log(key);
            $loadingDiv.hide();
            var html;
            if(resp.code != undefined){
                html = makeErrorAlert(key, resp.message);
            } else {
                if(resp.success != undefined && resp.success == true){
                    html = makeSuccessAlert(key);
                } else {
                    html = makeServerErrorAlert(key, resp.message);
                }
            }
            $orderListItem.append(html);

        });
        /*$orderListItem.fadeOut("normal", function() {
            $(this).remove();
        });*/
        console.log($("#"+key));
        console.log($button);
    });

    function makeErrorAlert(key, message){
        return '<div id="alert-div-'+key+'" class="alert alert-danger alert-dismissible fade in" role="alert">'+
                    '<button type="button" class="close" data-dismiss="alert" aria-label="Close">'+
                        '<span aria-hidden="true">×</span>'+
                    '</button>'+
                    '<h4>Error sending status update</h4>'+
                    '<p>'+message+'</p>'+
                    '<p>'+
                        'Click above to try again'+
                    '</p>'+
                '</div>';
    }

    function makeSuccessAlert(key){
            return '<div id="alert-div-'+key+'" class="alert alert-success alert-dismissible fade in" role="alert">'+
                        '<button type="button" class="close" data-dismiss="alert" aria-label="Close">'+
                            '<span aria-hidden="true">×</span>'+
                        '</button>'+
                        '<h4>Successfully updated</h4>'+
                        '<p>The order will be moved to the correct tab in a moment</p>'+
                    '</div>';
    }

    function makeServerErrorAlert(key, message){
                return '<div id="alert-div-'+key+'" class="alert alert-warning alert-dismissible fade in" role="alert">'+
                            '<button type="button" class="close" data-dismiss="alert" aria-label="Close">'+
                                '<span aria-hidden="true">×</span>'+
                            '</button>'+
                            '<h4>Server Error</h4>'+
                            '<p>'+message+'</p>'+
                        '</div>';
    }



