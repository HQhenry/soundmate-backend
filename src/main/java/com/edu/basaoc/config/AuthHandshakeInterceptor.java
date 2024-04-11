package com.edu.basaoc.config;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Extrahiert das Token aus den Query-Parametern
        String token = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams().getFirst("token");

        // Hier das Token validieren und die Authentifizierung durchführen...
        // Wenn die Authentifizierung fehlschlägt, können Sie false zurückgeben, um den Handshake abzubrechen

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
