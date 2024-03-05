const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/stomp'
});

stompClient.activate();
let out = document.getElementById("posts");

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    output('Connected');
    stompClient.subscribe('/post/feed/queue/reply', (message) => {
        console.log(message.body);
        output(message.body);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
    output('Error with websocket');
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
    output('Broker reported error');
};

function output(text) {
    let line = document.createElement("p");
    line.textContent = text;
    out.prepend(line);
}