package com.song.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * ErrorResponse is a DTO class that represents an error response.
 * It contains an error message, optional details, and an error code.
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {
    private final String errorMessage;
    private final Map<String, String> details; // nullable if not needed
    private final int errorCode;

    public ErrorResponseDto(String errorMessage, int errorCode) {
        this(errorMessage, null, errorCode);
    }
}
