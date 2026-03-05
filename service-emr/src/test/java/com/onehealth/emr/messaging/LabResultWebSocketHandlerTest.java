package com.onehealth.emr.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LabResultWebSocketHandlerTest {

    @Test
    void shouldTrackConnectedSessions() throws Exception {
        LabResultWebSocketHandler handler = new LabResultWebSocketHandler();
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn("session-1");
        when(session.isOpen()).thenReturn(true);

        handler.afterConnectionEstablished(session);

        handler.broadcast("test message");
        verify(session, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    void shouldRemoveSessionOnClose() throws Exception {
        LabResultWebSocketHandler handler = new LabResultWebSocketHandler();
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn("session-1");

        handler.afterConnectionEstablished(session);
        handler.afterConnectionClosed(session, null);

        handler.broadcast("test message");
        verify(session, never()).sendMessage(any());
    }

    @Test
    void shouldNotSendToClosedSessions() throws Exception {
        LabResultWebSocketHandler handler = new LabResultWebSocketHandler();
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn("session-1");
        when(session.isOpen()).thenReturn(false);

        handler.afterConnectionEstablished(session);
        handler.broadcast("test message");

        verify(session, never()).sendMessage(any());
    }
}
