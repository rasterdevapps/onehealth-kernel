package com.onehealth.erp.common.audit;

import com.onehealth.kernel.api.AuditableAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditLogger {

    private static final Logger log = LoggerFactory.getLogger(AuditLogger.class);

    public void log(String actionType, String entityType, String entityId, String performedBy) {
        AuditableAction action = new AuditableAction() {
            @Override public String getActionType() { return actionType; }
            @Override public String getEntityType() { return entityType; }
            @Override public String getEntityId() { return entityId; }
            @Override public String getPerformedBy() { return performedBy; }
            @Override public Instant getTimestamp() { return Instant.now(); }
        };

        log.info("AUDIT | action={} | entity={} | id={} | user={} | time={}",
                action.getActionType(),
                action.getEntityType(),
                action.getEntityId(),
                action.getPerformedBy(),
                action.getTimestamp());
    }
}
