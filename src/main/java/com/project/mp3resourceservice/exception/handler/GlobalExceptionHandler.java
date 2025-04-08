package com.project.mp3resourceservice.exception.handler;

import com.project.mp3resourceservice.exception.CSVStringException;
import com.project.mp3resourceservice.exception.ResourceNotFoundException;
import com.project.mp3resourceservice.exception.RestClientException;
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
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("errorMessage", "Invalid value " + ex.getValue() + " for " + ex.getName() + ".Must be a positive integer");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", "INTERNAL_SERVER_ERROR");
        errorResponse.put("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", HttpStatus.NOT_FOUND.value());
        errorResponse.put("errorMessage", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("errorMessage", "Invalid ID format: " + ex.getMessage() + ".Only  positive integers are allowed");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({MissingServletRequestPartException.class, MultipartException.class})
    public ResponseEntity<Object> handleInvalidResourceException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        if (ex instanceof MultipartException) {
            errorResponse.put("errorCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("errorMessage", "Invalid file format: application/json. Only MP3 files are allowed");
        } else {
            errorResponse.put("errorCode", "GENERIC_ERROR");
            errorResponse.put("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CSVStringException.class)
    public ResponseEntity<Object> handleCSVStringException(CSVStringException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("errorMessage", "CSV string is too long: received 208 characters, " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("errorMessage", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({IOException.class, TikaException.class, SAXException.class})
    public ResponseEntity<Object> handleFileProcessingExceptions(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("errorMessage", "Mp3 file processing erro: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Object> handleRestClientException(RestClientException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", ex.getStatusCode());
        errorResponse.put("errorMessage", ex.getMessage());
        errorResponse.put("details", ex.getResponseBody());
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }
}
