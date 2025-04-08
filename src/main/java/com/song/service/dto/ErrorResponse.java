package com.song.service.dto;

import java.util.Map;

public class ErrorResponse {
    private final String errorMessage;
    private final Map<String, String> details; // nullable if not needed
    private final String errorCode;

    public ErrorResponse(String errorMessage, Map<String, String> details, String errorCode) {
        this.errorMessage = errorMessage;
        this.details = details;
        this.errorCode = errorCode;
    }

    public ErrorResponse(String errorMessage, String errorCode) {
        this(errorMessage, null, errorCode);
    }

    // Getters and setters
}
