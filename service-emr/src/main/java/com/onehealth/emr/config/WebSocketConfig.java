package com.onehealth.emr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.onehealth.emr.messaging.LabResultWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final LabResultWebSocketHandler labResultWebSocketHandler;

    public WebSocketConfig(LabResultWebSocketHandler labResultWebSocketHandler) {
        this.labResultWebSocketHandler = labResultWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(labResultWebSocketHandler, "/ws/lab-results")
                .setAllowedOrigins("*");
    }
}
