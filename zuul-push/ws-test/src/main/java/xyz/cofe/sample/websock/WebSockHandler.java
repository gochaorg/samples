package xyz.cofe.sample.websock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSockHandler extends TextWebSocketHandler {
    private static Logger log = LoggerFactory.getLogger(WebSockHandler.class);

    private List<WebSocketSession> establishedSessions = new CopyOnWriteArrayList<>();
    public List<WebSocketSession> getEstablishedSessions(){ return establishedSessions; }

    // Вызывается после установки соединения. Добавляет клиента в общий список.
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        establishedSessions.add(session);
    }

    // Вызывается после прерывания соединения. Удаляет клиента из списка.
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        establishedSessions.remove(session);
    }

    // Вызывается после получения сообщения. Рассылает его всем подключенным клиентам.
    @Override
    protected void handleTextMessage(WebSocketSession source, TextMessage message) {
        establishedSessions.stream().filter( target->target!=source ).forEach(
            target -> sendMessageToClient(message, target)
        );
    }

    private void sendMessageToClient(TextMessage message,
                                     WebSocketSession establishedSession) {
        try {
            establishedSession.sendMessage(new TextMessage(message.getPayload()));
        } catch ( IOException e) {
            log.warn("sendMessageToClient fail",e);
        }
    }
}
