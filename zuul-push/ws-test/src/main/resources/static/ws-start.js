const messageWindow = document.getElementById("messages");
const sendButton = document.getElementById("send");
const messageInput = document.getElementById("message");

messageInput.addEventListener( 'keydown', (e)=>{
    if( e.keyCode == 13 && !e.altKey && !e.ctrlKey && !e.shiftKey ){
        sendMessage(messageInput.value);
        messageInput.value = '';
    }
});

// Создание WebSocket. Открытие соединения
const socket = new WebSocket("ws://localhost:8081/ws/start");
socket.binaryType = "arraybuffer";

// Обработчик открытия соединения
socket.onopen = function () {
    addMessageToWindow("Соединение установлено");
};

// Обработчик получения сообщения от сервера
socket.onmessage = function (event) {
    addMessageToWindow(`Получено: ${event.data}`);
};

sendButton.onclick = function () {
    sendMessage(messageInput.value);
    messageInput.value = "";
};

// Функция для отправки сообщения на сервер
function sendMessage(message) {
    socket.send(message);
    addMessageToWindow(`Отправлено: ${message}`);
}

function addMessageToWindow(message) {
    messageWindow.innerHTML += `<div>${message}</div>`
}