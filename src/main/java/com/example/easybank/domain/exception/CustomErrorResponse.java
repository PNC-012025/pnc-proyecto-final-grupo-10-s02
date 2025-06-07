package com.example.easybank.domain.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public record CustomErrorResponse (
        LocalDate timestamp,
        String message,
        String path
){
}