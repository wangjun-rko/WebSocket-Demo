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
    // 新建SockjS对象，并设置服务端的连接点（/gs-guide-websocket），这里的连接点由服务端提供。
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({"id": "header"}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        // 注册客户端地址
        // 注意前戳必须是/topic开头，因为在前面服务端已经配置了目的地前戳。与@SendTo中的地址对应。
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    // 客户端发送消息
    // 第一个参数是服务端@MessageMapping地址并且加了指定的/app前戳
    // 第二个参数为header头部信息，第三个是发送的消息内容。
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});