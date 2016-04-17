    sendMessage = function() {
      //path += '?g=' + state.game_key;
      //if (opt_param) {
      //  path += '&' + opt_param;
      //}
      var xhr = new XMLHttpRequest();
      xhr.open('POST', "message", true);
      xhr.send();
    };

    onOpened = function() {
      connected = true;
      console.log("onConnected!")
      //sendMessage();
      //updateBoard();
    };

    onMessage = function(message){
        var messageObject = JSON.parse(message.data);
        console.log("onMessage!")
        console.log(messageObject);

        addOrderToList(messageObject);
    }

    onError = function(){
        console.log("onError!");
    }

    onClose = function(){
        console.log("onColose!");
        var xhr = new XMLHttpRequest();
        xhr.open('POST', "channelclose", true);
        xhr.send();
    }


channel = new goog.appengine.Channel(token);
    socket = channel.open();
    socket.onopen = onOpened;
    socket.onmessage = onMessage;
    socket.onerror = onError;
    socket.onclose = onClose;

    $("#sendMessage").click(function(){
        console.log("button clciked");
        sendMessage();
    });

    function addOrderToList(order){
        var listContent = '<li  class="list-group-item">'+
                               '<h4 class="list-group-item-heading">NEW ORDER!</h4>'+
                               '<ul class="list-group">';
        for(var i=0;i<order.orderItemEntities.length;i++){
        listContent+= '<a href="#"  class="list-group-item">'+
                            '<h4 class="list-group-item-heading"></h4>'+
                            '<p class="list-group-item-text">'+
                                'Name: '+order.orderItemEntities[i].menuItem.name+'<br>'+
                                'Amount: '+order.orderItemEntities[i].amount+'<br>'+
                            '</p>'+
                       '</a>'
        }
        listContent += '</ul></li>'
        console.log(listContent);
        $(listContent).prependTo("#receivedList");
    }


