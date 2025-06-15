package com.example.easybank.domain.exception;

public class RecipientNameMismatchException extends RuntimeException {
    public RecipientNameMismatchException(String message) {
        super(message);
    }
}
