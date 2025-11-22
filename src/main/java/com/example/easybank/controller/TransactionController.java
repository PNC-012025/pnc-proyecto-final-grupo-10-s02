package com.example.easybank.controller;

import com.example.easybank.domain.dto.request.TransactionRequestDTO;
import com.example.easybank.domain.dto.response.TransactionResponseDTO;
import com.example.easybank.service.TransactionService;
import com.example.easybank.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

//    @GetMapping(FIND_OWN)
//    public ResponseEntity<GenericResponse> findOwnTransaction() throws Exception {
//        List<TransactionResponseDTO> transactions = transactionService.findMyOwnTransactions();
//        return GenericResponse.builder()
//                .data(transactions)
//                .message("Transactions found")
//                .status(HttpStatus.OK)
//                .build().buildResponse();
//    }

    @GetMapping("FIND")
    public ResponseEntity<GenericResponse> findTransaction(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @RequestParam(required = false) UUID accountId,
        @RequestParam(required = false) UUID originTransactionId,
        @RequestParam(required = false) UUID destinationTransactionId,
        Pageable pageable
    ) throws Exception {
        Page<TransactionResponseDTO> response = transactionService.getAllTransactions(
                type, from, to, originTransactionId, destinationTransactionId, accountId, pageable
        );
        return GenericResponse.builder()
                .data(response)
                .message("Transactions found")
                .status(HttpStatus.OK)
                .build().buildResponse();
    }
}
