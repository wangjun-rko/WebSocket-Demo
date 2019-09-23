var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    console.log('socket: ' + socket);
    console.log('#id: ' + $("#id").val());
    stompClient = Stomp.over(socket);
    // 在stompClient.connect()方法的第一个参数中传入header头信息，该头部信息必须设置id字段的值，
    // 因为服务端会读取该ID值，该值最终会取自URL中的参数。
    stompClient.connect({"id": $("#id").val()}, function (frame) { //客户端ID
        setConnected(true);
        console.log('------Connected: ' + frame);
        stompClient.subscribe('/topic/chat/' + $("#id").val(), function (greeting) { //表明客户端地址
            showGreeting(greeting.body);
        }, {"id": "Host_" + $("#id").val()});
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.send("/app/chatOut", {},$("#id").val());
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/chat", {}, JSON.stringify({
        'content': $("#content").val(),
        'id': $("#id").val(),
        'pid': $("#pid").val()
    }));
    showGreeting("我:" + $("#content").val())
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
});