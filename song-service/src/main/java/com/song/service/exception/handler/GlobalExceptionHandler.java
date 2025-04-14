package com.song.service.exception.handler;


import com.song.service.dto.ErrorResponseDto;
import com.song.service.exception.CSVStringException;
import com.song.service.exception.ResourceAlreadyExistException;
import com.song.service.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getMessage(), null, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                "Validation error",
                //extracting field names from constraint violations
                ex.getConstraintViolations().stream()
                        .collect(HashMap::new, (map, violation) -> {
                            Path.Node leafNode = null;
                            for (Path.Node node : violation.getPropertyPath()) {
                                leafNode = node;
                            }
                            map.put(leafNode != null ? leafNode.getName() : "unknown", violation.getMessage());
                        }, HashMap::putAll),
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
                "Invalid ID format: " + "'" +
                        ex.getMessage().replaceAll(".*\"(.*)\".*", "$1") + "'" +
                        ". Only positive integers are allowed", null,
                HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        log.error("--unexpected error occured: " + ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "An unexpected error occurred: ",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}