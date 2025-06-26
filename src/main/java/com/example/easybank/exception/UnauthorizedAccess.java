package com.example.easybank.exception;

public class UnauthorizedAccess extends RuntimeException {
    public UnauthorizedAccess(String message) {
        super(message);
    }
}
