const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws/chat'
});
stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);

    stompClient.subscribe('/topic/hello', (message) => {
        const content = message.body;
        // const content = (JSON.parse(message.body).content);
        showMessage(content);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function sendMessageOnClick() {
    const input = document.getElementById('chat-input').value;
    if (input.trim() !== "") {
        stompClient.publish({
            destination: "/app/hello",  // 서버의 메시지 처리 경로
            body: JSON.stringify({ 'content': input })
        });
    }
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function sendName() {
    stompClient.publish({
        destination: "/app",
        body: JSON.stringify({'name': $("#name").val()})
    });
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function showMessage(message) {
    $("#showMessage").append("<tr><td>" + message + "</td></tr>");
}

function exitChat() {
    stompClient.deactivate();
    setConnected(false);
    console.log("방을 나가셨습니다.");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendName());
});