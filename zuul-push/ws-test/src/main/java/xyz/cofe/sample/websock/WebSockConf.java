package xyz.cofe.sample.websock;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSockConf implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers( WebSocketHandlerRegistry registry ){
        if( registry==null )throw new IllegalArgumentException("registry==null");
        registry.addHandler(new WebSockHandler(), "/ws/start").setAllowedOrigins("*");
    }
}
