package com.onehealth.emr.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onehealth.erp.common.event.LabResultReadyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LabResultEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(LabResultEventConsumer.class);

    private final LabResultWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public LabResultEventConsumer(LabResultWebSocketHandler webSocketHandler,
                                  ObjectMapper objectMapper) {
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${emr.rabbitmq.lab-result-queue}")
    public void onLabResultReady(LabResultReadyEvent event) {
        log.info("Received LabResultReady event for order {} (patient: {})",
                event.getOrderId(), event.getPatientId());
        try {
            String json = objectMapper.writeValueAsString(event);
            webSocketHandler.broadcast(json);
        } catch (Exception e) {
            log.error("Failed to broadcast lab result event: {}", e.getMessage());
        }
    }
}
