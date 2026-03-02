package com.onehealth.kernel.api;

import java.time.Instant;

public interface AuditableAction {

    String getActionType();

    String getEntityType();

    String getEntityId();

    String getPerformedBy();

    Instant getTimestamp();
}
