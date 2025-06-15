package com.example.easybank.web.controller;

import com.example.easybank.application.dto.request.TransactionRequestDTO;
import com.example.easybank.application.service.TransactionService;
import com.example.easybank.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.metamodel.internal.AbstractPojoInstantiator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.easybank.util.Constant.API;
import static com.example.easybank.util.Constant.TRANSACTION;

@RequestMapping(API + TRANSACTION)
@RequiredArgsConstructor
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<GenericResponse> create(TransactionRequestDTO transaction) throws Exception {
        transactionService.createTransaction(transaction);
        return GenericResponse.builder()
                .data(transaction)
                .message("Transaction successfully")
                .status(HttpStatus.CREATED)
                .build().buildResponse();
    }
}
