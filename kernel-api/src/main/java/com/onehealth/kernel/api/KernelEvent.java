package com.onehealth.kernel.api;

import java.time.Instant;

/**
 * Base interface for all ERP Kernel domain events.
 */
public interface KernelEvent {

    String getEventType();

    String getTenantId();

    Instant getOccurredAt();
}
