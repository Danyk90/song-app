package com.project.resourceservice.exception.handler;

import com.project.resourceservice.dto.ErrorResponseDto;
import com.project.resourceservice.exception.CSVStringException;
import com.project.resourceservice.exception.ResourceNotFoundException;
import com.project.resourceservice.exception.RestClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.apache.tika.exception.TikaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),
                "Invalid value " + ex.getValue() +
                        " for " + ex.getName() + ".Must be a positive integer", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        log.error("--unexpected error occured: " + ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred: " + ex.getMessage(), null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: " + ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid ID format: " + ex.getMessage() +
                        ". Only positive integers are allowed", null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({MissingServletRequestPartException.class, MultipartException.class})
    public ResponseEntity<Object> handleInvalidResourceException(Exception ex) {
        ErrorResponseDto errorResponse;
        if (ex instanceof MultipartException) {
            errorResponse = new ErrorResponseDto(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid file format: application/json. Only MP3 files are allowed", null
            );
        } else {
            errorResponse = new ErrorResponseDto(
                    HttpStatus.BAD_REQUEST.value(),
                    "An unexpected error occurred: " + ex.getMessage(), null
            );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CSVStringException.class)
    public ResponseEntity<Object> handleCSVStringException(CSVStringException ex) {
        log.error("CSVStringException occurred: " + ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "CSV string is too long: received 208 characters, " + ex.getMessage(), null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        log.error("BadRequestException occurred: " + ex.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("errorMessage", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({IOException.class, TikaException.class, SAXException.class})
    public ResponseEntity<Object> handleFileProcessingExceptions(Exception ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Mp3 file processing error: " + ex.getMessage(), null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Object> handleRestClientException(RestClientException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                ex.getStatusCode(),
                ex.getMessage(),
                ex.getResponseBody()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }
}
