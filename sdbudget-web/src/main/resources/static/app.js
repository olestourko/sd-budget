var stompClient = null;

function setConnected(connected) {
    jQuery("#connect").prop("disabled", connected);
    jQuery("#disconnect").prop("disabled", !connected);
    if (connected) {
        jQuery("#conversation").show();
    } else {
        jQuery("#conversation").hide();
    }
    jQuery("#responses").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/example', function (response) {
            showResponse(JSON.parse(response.body).content);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.send("/app/example", {}, JSON.stringify({'content': jQuery("#message").val()}));
}

function showResponse(message) {
    jQuery("#responses").append("<tr><td>" + message + "</td></tr>");
}

jQuery(function () {
    jQuery("form").on('submit', function (e) {
        e.preventDefault();
    });
    jQuery("#connect").click(function () {
        connect();
    });
    jQuery("#disconnect").click(function () {
        disconnect();
    });
    jQuery("#send").click(function () {
        sendMessage();
    });
});