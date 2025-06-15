package com.example.easybank.application.mapper;

import com.example.easybank.application.dto.request.TransactionRequestDTO;
import com.example.easybank.domain.entity.Transaction;

public class TransactionMapper {
    public static Transaction toEntity(TransactionRequestDTO transactionRequestDTO) {
        return Transaction.builder()
                .description(transactionRequestDTO.getDescription())
                .amount(transactionRequestDTO.getAmount())
                .build();
    }
}
