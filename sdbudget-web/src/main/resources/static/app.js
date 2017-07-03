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
//            showResponse(JSON.parse(response.body).content);
            showResponse(response);
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

/*
 * Month: int
 * Type: "revenue", "expense", "adjustment"
 */
function addBugetItem(month, name, amount, type) {
    stompClient.send("/app/add-budget-item", {}, JSON.stringify({
        month: month,
        name: name,
        amount: amount,
        type: type
    }));
}

function updateBudgetItem(id, name, amount) {
    stompClient.send("/app/update-budget-item", {}, JSON.stringify({
        id: id,
        name: name,
        amount: amount
    }));
}

function removeBudgetItem(id) {
    stompClient.send("/app/remove-budget-item", {}, JSON.stringify({
        id: id
    }));
}
