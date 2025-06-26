package com.example.easybank.exception;

import com.example.easybank.util.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.List;

@RestControllerAdvice
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

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<GenericResponse> handleInvalidCredentials(
            InvalidCredentialsException ex,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return GenericResponse.builder()
                .data(errorResponse)
                .status(HttpStatus.UNAUTHORIZED)
                .build().buildResponse();
    }


    @ExceptionHandler(UnauthorizedAccess.class)
    public ResponseEntity<GenericResponse> handleUnauthorizedAccess(
            UnauthorizedAccess ex,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return GenericResponse.builder()
                .data(errorResponse)
                .status(HttpStatus.UNAUTHORIZED)
                .build().buildResponse();
    }


    @ExceptionHandler(StorageException.class)
    public ResponseEntity<GenericResponse> handleStorage(
            StorageException ex,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return GenericResponse.builder()
                .data(errorResponse)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
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

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<GenericResponse> handleInvalidAmount(
            InvalidAmountException ex,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return GenericResponse.builder()
                .data(errorResponse)
                .status(HttpStatus.BAD_REQUEST)
                .build().buildResponse();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GenericResponse> handleAccessDenied(
            AccessDeniedException ex,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return GenericResponse.builder()
                .data(errorResponse)
                .status(HttpStatus.FORBIDDEN)
                .build().buildResponse();
    }

    @ExceptionHandler(BillAlreadyPaidException.class)
    public ResponseEntity<GenericResponse> handleBillAlreadyPaid(
            BillAlreadyPaidException ex,
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

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<GenericResponse> handleInsufficientFunds(
            InsufficientFundsException ex,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return GenericResponse.builder()
                .data(errorResponse)
                .status(HttpStatus.BAD_REQUEST)
                .build().buildResponse();
    }

    @ExceptionHandler(InvalidTransferException.class)
    public ResponseEntity<GenericResponse> handleInvalidTransfer(
            InvalidTransferException ex,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return GenericResponse.builder()
                .data(errorResponse)
                .status(HttpStatus.BAD_REQUEST)
                .build().buildResponse();
    }

    @ExceptionHandler(RecipientNameMismatchException.class)
    public ResponseEntity<GenericResponse> handleRecipientNameMismatch(
            RecipientNameMismatchException ex,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return GenericResponse.builder()
                .data(errorResponse)
                .status(HttpStatus.BAD_REQUEST)
                .build().buildResponse();
    }





    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse> handleValueOfEntity(MethodArgumentNotValidException e) {
        List<String> errors = e.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return GenericResponse.builder()
                .data(errors)
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation failed")
                .build().buildResponse();
    }

}
