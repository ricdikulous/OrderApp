    var makeNew = true;
    var channelConnected = false;
    var endpointConnected = false;

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
        var listContent = '<li  class="list-group-item">'+
                               '<h4 class="list-group-item-heading">NEW ORDER!</h4>'+
                               '<ul class="list-group">';
        for(var i=0;i<order.orderItemEntities.length;i++){
        listContent+= '<a href="#"  class="list-group-item">'+
                            '<h4 class="list-group-item-heading">'+order.orderItemEntities[i].name+'</h4>'+
                            '<p class="list-group-item-text">'+
                                'Amount: '+order.orderItemEntities[i].amount+'<br>'+
                            '</p>'+
                       '</a>'
        }
        listContent += '</ul><button>Advance</button></li>'
        console.log(listContent);
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


