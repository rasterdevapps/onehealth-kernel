package com.onehealth.lis.service;

import com.onehealth.erp.common.audit.AuditLogger;
import com.onehealth.erp.common.dto.LabOrderDTO;
import com.onehealth.erp.common.event.LabOrderCreatedEvent;
import com.onehealth.erp.common.event.LabResultReadyEvent;
import com.onehealth.kernel.auth.TenantContext;
import com.onehealth.lis.domain.LabOrder;
import com.onehealth.lis.repository.LabOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LabOrderService {

    private static final Logger log = LoggerFactory.getLogger(LabOrderService.class);

    private final LabOrderRepository labOrderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final AuditLogger auditLogger;

    @Value("${lis.rabbitmq.exchange}")
    private String exchange;

    @Value("${lis.rabbitmq.routing-key}")
    private String routingKey;

    @Value("${lis.rabbitmq.result-routing-key}")
    private String resultRoutingKey;

    public LabOrderService(LabOrderRepository labOrderRepository,
                           RabbitTemplate rabbitTemplate,
                           AuditLogger auditLogger) {
        this.labOrderRepository = labOrderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.auditLogger = auditLogger;
    }

    public LabOrderDTO createLabOrder(LabOrderDTO dto) {
        LabOrder order = new LabOrder();
        order.setPatientId(dto.getPatientId());
        order.setEncounterId(dto.getEncounterId());
        order.setTestType(dto.getTestType());
        order.setStatus("ORDERED");
        order.setOrderedAt(Instant.now());

        LabOrder saved = labOrderRepository.save(order);

        LabOrderCreatedEvent event = new LabOrderCreatedEvent(
                saved.getId(),
                saved.getPatientId(),
                saved.getTenantId(),
                saved.getTestType()
        );
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.info("Published LabOrderCreated event for order {}", saved.getId());

        auditLogger.log("LAB_ORDER_CREATED", "LabOrder", saved.getId().toString(), "system");

        return toDto(saved);
    }

    public LabOrderDTO executeLabTest(UUID orderId) {
        LabOrder order = labOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Lab order not found: " + orderId));
        order.setStatus("COMPLETED");
        order.setResult("Test completed successfully");
        order.setCompletedAt(Instant.now());

        LabOrder saved = labOrderRepository.save(order);

        LabResultReadyEvent resultEvent = new LabResultReadyEvent(
                saved.getId(),
                saved.getPatientId(),
                saved.getTenantId(),
                saved.getTestType(),
                saved.getResult()
        );
        rabbitTemplate.convertAndSend(exchange, resultRoutingKey, resultEvent);
        log.info("Published LabResultReady event for order {}", saved.getId());

        auditLogger.log("LAB_TEST_EXECUTED", "LabOrder", saved.getId().toString(), "system");

        return toDto(saved);
    }

    public List<LabOrderDTO> getOrdersByTenant() {
        String tenantId = TenantContext.getTenantId();
        return labOrderRepository.findByTenantId(tenantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private LabOrderDTO toDto(LabOrder order) {
        LabOrderDTO dto = new LabOrderDTO();
        dto.setId(order.getId());
        dto.setTenantId(order.getTenantId());
        dto.setPatientId(order.getPatientId());
        dto.setEncounterId(order.getEncounterId());
        dto.setTestType(order.getTestType());
        dto.setStatus(order.getStatus());
        dto.setResult(order.getResult());
        dto.setOrderedAt(order.getOrderedAt());
        dto.setCompletedAt(order.getCompletedAt());
        return dto;
    }
}
