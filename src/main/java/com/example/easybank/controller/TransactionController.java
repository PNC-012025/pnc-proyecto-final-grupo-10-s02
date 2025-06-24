package com.example.easybank.controller;

import com.example.easybank.domain.dto.request.TransactionRequestDTO;
import com.example.easybank.domain.dto.response.AdminTransactionResponseDTO;
import com.example.easybank.domain.dto.response.TransactionResponseDTO;
import com.example.easybank.service.TransactionService;
import com.example.easybank.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.easybank.util.Constant.*;

@RequestMapping(API + TRANSACTION)
@RequiredArgsConstructor
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping(CREATE)
    public ResponseEntity<GenericResponse> create(@RequestBody TransactionRequestDTO transaction) throws Exception {
        transactionService.createTransaction(transaction);
        return GenericResponse.builder()
                .message("Transaction successfully")
                .status(HttpStatus.CREATED)
                .build().buildResponse();
    }

    @GetMapping(FIND_OWN)
    public ResponseEntity<GenericResponse> findOwnTransaction() throws Exception {
        List<TransactionResponseDTO> transactions = transactionService.findMyOwnTransactions();
        return GenericResponse.builder()
                .data(transactions)
                .message("Transactions found")
                .status(HttpStatus.OK)
                .build().buildResponse();
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<GenericResponse> findAllTransactions() throws Exception {
        List<AdminTransactionResponseDTO> transactions = transactionService.findAllTransactions();
        return GenericResponse.builder()
                .data(transactions)
                .message("Transactions found")
                .status(HttpStatus.OK)
                .build().buildResponse();
    }
}
