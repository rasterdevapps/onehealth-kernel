package com.onehealth.kernel.api;

import java.time.Instant;

/**
 * Standard API response wrapper for all ERP Kernel REST endpoints.
 */
public record ApiResponse<T>(
        String status,
        T data,
        String error,
        Instant timestamp
) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>("success", data, null, Instant.now());
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        return new ApiResponse<>("error", null, errorMessage, Instant.now());
    }
}
