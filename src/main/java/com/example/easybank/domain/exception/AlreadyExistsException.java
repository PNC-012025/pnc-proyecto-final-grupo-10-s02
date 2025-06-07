package com.example.easybank.domain.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String entityName, String detail) {
        super(entityName + " already exist: " + detail);
    }

    public AlreadyExistsException(String message) {
        super(message);
    }
}
