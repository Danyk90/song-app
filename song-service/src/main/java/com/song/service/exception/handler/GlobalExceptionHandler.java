package com.song.service.exception.handler;


import com.song.service.dto.ErrorResponseDto;
import com.song.service.exception.CSVStringException;
import com.song.service.exception.ResourceAlreadyExistException;
import com.song.service.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getMessage(), null, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                "Validation error",
                ex.getBindingResult().getFieldErrors().stream()
                        .collect(HashMap::new, (map, error) -> map.put(error.getField(), error.getDefaultMessage()),
                                HashMap::putAll),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleResourceAlreadyExistException(ResourceAlreadyExistException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                ex.getMessage(),
                null,
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CSVStringException.class)
    public ResponseEntity<?> handleCSVStringException(CSVStringException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                "CSV string is too long: received 208 characters, " + ex.getMessage(),
                null,
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleNumberFormatException(NumberFormatException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                "Invalid ID format: " + ex.getMessage() + ". Only positive integers are allowed", null,
                HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

}