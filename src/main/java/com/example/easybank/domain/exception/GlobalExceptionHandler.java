package com.example.easybank.domain.exception;

import com.example.easybank.util.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.List;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<GenericResponse> handleNotFound(
            ModelNotFoundException ex,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return GenericResponse.builder()
                .data(errorResponse)
                .status(HttpStatus.NOT_FOUND)
                .build().buildResponse();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<GenericResponse> handleEntityAlreadyExists(
            AlreadyExistsException ex,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return GenericResponse.builder()
                .data(errorResponse)
                .status(HttpStatus.CONFLICT)
                .build().buildResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse> handleValueOfEntity(MethodArgumentNotValidException e) {
        List<String> errors = e.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return GenericResponse.builder()
                .data(errors)
                .build().buildResponse();
    }
}
