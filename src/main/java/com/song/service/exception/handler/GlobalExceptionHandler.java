package com.song.service.exception.handler;


import com.song.service.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", ex.getMessage());
        errorResponse.put("errorCode", "404");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", "Validation error");
        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> details.put(error.getField(), error.getDefaultMessage()));
        errorResponse.put("details", details);
        errorResponse.put("errorCode", "400");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}